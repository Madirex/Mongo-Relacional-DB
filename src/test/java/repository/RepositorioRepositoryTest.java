package repository;

import dao.*;
import database.MongoDBController;
import org.bson.types.ObjectId;
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

@DisplayName("JUnit 5 Test CRUD Repositorio")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RepositorioRepositoryTest {

    private RepositorioRepository repositorioRepository = new RepositorioRepository();

    private Repositorio r1;
    private Repositorio r2;
    private Proyecto proyecto;
    private Programador programador;
    private Departamento departamento;
    private Login login;
    private AccessHistory accessHistory;

    private final void initData() {
        Date date = new Date();
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.proyecto = new Proyecto(programador, "Ciencia", 20000.00, date,
                date, technologies, r1, false, departamento);
        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.login = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);
        this.accessHistory = new AccessHistory(new ObjectId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        this.r1 = new Repositorio("Ciencia", date, proyecto);
        this.r2 = new Repositorio((String) null, null, null);
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
    @DisplayName("Respository Insert Reporsitorio")
    @Order(2)
    void insertTest() throws SQLException {
        Optional<Repositorio> repositorio = repositorioRepository.insert(r1);
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(r1.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(r1.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(r1.getProyecto(), repositorio.get().getProyecto())
        );
    }

    @Test
    @DisplayName("Get all Reporsitorio")
    @Order(3)
    void getAllTest() throws SQLException {
        repositorioRepository.insert(r1);
        Optional<List<Repositorio>> repositorios = repositorioRepository.findAll();
        assumeTrue(repositorios.isPresent());
        Assertions.assertAll( "Comprobaciones test getAll",
                () -> Assertions.assertEquals(1, repositorios.get().size()),
                () -> Assertions.assertEquals(r1.getId(), repositorios.get().get(0).getId()),
                () -> Assertions.assertEquals(r1.getCreationDate(), repositorios.get().get(0).getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorios.get().get(0).getProyecto().getId())
        );
    }

    @Test
    @DisplayName("Find Reporsitorio By ID")
    @Order(4)
    void getById() throws SQLException {
        repositorioRepository.insert(r1);
        Optional<Repositorio> repositorio = repositorioRepository.getById(r1.getId());
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(r1.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(r1.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorio.get().getProyecto().getId())
        );
    }

    @Test
    @DisplayName("Update Reporsitorio")
    @Order(5)
    void update() throws SQLException {
        repositorioRepository.insert(r1);
        Proyecto proyecto1 = new Proyecto();
        r1.setProyecto(proyecto1);
        Optional<Repositorio> repositorio = repositorioRepository.update(r1);
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(r1.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(r1.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(proyecto1.getId(), repositorio.get().getProyecto().getId())
        );
    }

    @Test
    @DisplayName("Delete Reporsitorio")
    @Order(6)
    void delete() throws SQLException {
        repositorioRepository.insert(r1);
        Optional<Repositorio> repositorio = repositorioRepository.delete(r1.getId());
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(r1.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(r1.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorio.get().getProyecto().getId())
        );
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(7)
    void getByIdException() {
        Exception exception = assertThrows(SQLException.class,() -> {
            repositorioRepository.getById(r2.getId());
        });

        String expectedMessage = "Error RepositorioRepository no se ha encontrado la ID " + r2.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}