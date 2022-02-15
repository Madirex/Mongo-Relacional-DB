package repository;

import dao.*;
import database.MongoDBController;
import org.junit.jupiter.api.*;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("JUnit 5 Test CRUD AccessHistory")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccessHistoryRepositoryTest {

    private AccessHistoryRepository accessHistoryRepository = new AccessHistoryRepository();

    private AccessHistory h1;
    private AccessHistory h2;
    private Programador programador;
    private Departamento departamento;
    private Login login;

    private final void initData() {
        Date date = new Date();
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.login = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", h1, false);

        this.h1 = new AccessHistory(programador.getId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        this.h2 = new AccessHistory(null, null);
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        initData();
    }

    @Test
    @DisplayName("True is true")
    @Order(1)
    void trueIsTrue() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Respository Insert Test")
    @Order(2)
    void insertTest() throws SQLException {
        Optional<AccessHistory> accessHistory = accessHistoryRepository.insert(h1);
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(h1.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(h1.getProgramador(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(h1.getInstante(), accessHistory.get().getInstante())
        );
    }

    @Test
    @DisplayName("Get all AccessHistory")
    @Order(3)
    void getAllTest() throws SQLException {
        accessHistoryRepository.insert(h1);
        Optional<List<AccessHistory>> accessHistorys = accessHistoryRepository.findAll();
        assumeTrue(accessHistorys.isPresent());
        Assertions.assertAll("Comprobaciones test getAll",
                () -> Assertions.assertEquals(1, accessHistorys.get().size()),
                () -> Assertions.assertEquals(h1.getId(), accessHistorys.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistorys.get().get(0).getProgramador()),
                () -> Assertions.assertEquals(h1.getInstante(), accessHistorys.get().get(0).getInstante())
        );
    }

    @Test
    @DisplayName("Find AccessHistory By ID")
    @Order(4)
    void getById() throws SQLException {
        accessHistoryRepository.insert(h1);
        Optional<AccessHistory> accessHistory = accessHistoryRepository.getById(h1.getId());
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(h1.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(h1.getInstante(), accessHistory.get().getInstante())
        );
    }

    @Test
    @DisplayName("Update AccessHistory")
    @Order(5)
    void update() throws SQLException {
        accessHistoryRepository.insert(h1);
        Programador programador1 = new Programador();
        h1.setProgramador(programador1.getId());
        Optional<AccessHistory> accessHistory = accessHistoryRepository.update(h1);
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(h1.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(programador1.getId(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(h1.getInstante(), accessHistory.get().getInstante())
        );
    }

    @Test
    @DisplayName("Delete AccessHistory")
    @Order(6)
    void delete() throws SQLException {
        accessHistoryRepository.insert(h1);
        Optional<AccessHistory> accessHistory = accessHistoryRepository.delete(h1.getId());
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(h1.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(h1.getInstante(), accessHistory.get().getInstante())
        );
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(7)
    void getByIdException() {
        Exception exception = assertThrows(SQLException.class,() -> {
            accessHistoryRepository.getById(h2.getId());
        });

        String expectedMessage = "Error AccessHistoryRepository no se ha encontrado la ID " + h2.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}