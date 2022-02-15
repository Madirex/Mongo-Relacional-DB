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

@DisplayName("JUnit 5 Test CRUD Login")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginRepositoryTest {

    private LoginRepository loginRepository = new LoginRepository();

    private Login l1;
    private Login l2;
    private Programador programador;
    private AccessHistory accessHistory;
    private Departamento departamento;

    private final void initData() {
        Date date = new Date();
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", l1);
        this.accessHistory = new AccessHistory(programador.getId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);

        this.l1 = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);
        this.l2 = new Login(null, null, null, null, false);
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
    @DisplayName("Respository Insert Login")
    @Order(2)
    void insertTest() throws SQLException {
        Optional<Login> login = loginRepository.insert(l1);
        assumeTrue(login.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(l1.getId(), login.get().getId()),
                () -> Assertions.assertEquals(l1.getProgramador(), login.get().getProgramador()),
                () -> Assertions.assertEquals(l1.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(l1.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(l1.getAccessHistory(), login.get().getAccessHistory()),
                () -> Assertions.assertEquals(l1.isConectado(), login.get().isConectado())
        );
    }

    @Test
    @DisplayName("Get all Login")
    @Order(3)
    void getAllTest() throws SQLException {
        loginRepository.insert(l1);
        Optional<List<Login>> logins = loginRepository.findAll();
        assumeTrue(logins.isPresent());
        Assertions.assertAll( "Comprobaciones test getAll",
                () -> Assertions.assertEquals(1, logins.get().size()),
                () -> Assertions.assertEquals(l1.getId(), logins.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), logins.get().get(0).getProgramador().getId()),
                () -> Assertions.assertEquals(l1.getInstante(), logins.get().get(0).getInstante()),
                () -> Assertions.assertEquals(l1.getToken(), logins.get().get(0).getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), logins.get().get(0).getAccessHistory().getId()),
                () -> Assertions.assertEquals(l1.isConectado(), logins.get().get(0).isConectado())
        );
    }

    @Test
    @DisplayName("Find Login By ID")
    @Order(4)
    void getById() throws SQLException {
        loginRepository.insert(l1);
        Optional<Login> login = loginRepository.getById(l1.getId());
        assumeTrue(login.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(l1.getId(), login.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), login.get().getProgramador().getId()),
                () -> Assertions.assertEquals(l1.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(l1.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), login.get().getAccessHistory().getId()),
                () -> Assertions.assertEquals(l1.isConectado(), login.get().isConectado())
        );
    }

    @Test
    @DisplayName("Update Login")
    @Order(5)
    void update() throws SQLException {
        loginRepository.insert(l1);
        l1.setToken("Nuevo token");
        Optional<Login> login = loginRepository.update(l1);
        assumeTrue(login.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(l1.getId(), login.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), login.get().getProgramador().getId()),
                () -> Assertions.assertEquals(l1.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(l1.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), login.get().getAccessHistory().getId()),
                () -> Assertions.assertEquals(l1.isConectado(), login.get().isConectado())
        );
    }

    @Test
    @DisplayName("Delete Login")
    @Order(6)
    void delete() throws SQLException {
        loginRepository.insert(l1);
        Optional<Login> login = loginRepository.delete(l1.getId());
        assumeTrue(login.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(l1.getId(), login.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), login.get().getProgramador().getId()),
                () -> Assertions.assertEquals(l1.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(l1.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), login.get().getAccessHistory().getId()),
                () -> Assertions.assertEquals(l1.isConectado(), login.get().isConectado())
        );
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(7)
    void getByIdException() {
        Exception exception = assertThrows(SQLException.class,() -> {
            loginRepository.getById(l2.getId());
        });

        String expectedMessage = "Error LoginRepository no se ha encontrado la ID " + l2.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}