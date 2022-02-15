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

@DisplayName("JUnit 5 Test CRUD Tarea")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TareaRepositoryTest {

    private TareaRepository tareaRepository = new TareaRepository();

    private Tarea t1;
    private Tarea t2;
    private Programador programador;
    private Issue issue;
    private Repositorio repositorio;
    private Commit commit;
    private Proyecto proyecto;
    private Departamento departamento;
    private Login login;
    private AccessHistory accessHistory;

    private final void initData() {
        Date date = new Date();
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.proyecto = new Proyecto(programador, "Ciencia", 20000.00, date,
                date, technologies, repositorio, false, departamento);
        this.issue = new Issue("Nueva issue", "Issue creada", date, proyecto, repositorio, commit, false);
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.commit = new Commit("Nuevo commit", "Commit creado", date,repositorio, proyecto, programador, issue);
        this.login = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);
        this.accessHistory = new AccessHistory(new ObjectId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        this.repositorio = new Repositorio("Ciencia", date, proyecto);

        this.t1 = new Tarea(programador, issue);
        this.t2 = new Tarea(null, null);
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
    @DisplayName("Respository Insert Tarea")
    @Order(2)
    void insertTest() throws SQLException {
        Optional<Tarea> tarea = tareaRepository.insert(t1);
        assumeTrue(tarea.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(t1.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(t1.getProgramador(), tarea.get().getProgramador()),
                () -> Assertions.assertEquals(t1.getIssue(), tarea.get().getIssue())
        );
    }

    @Test
    @DisplayName("Get all Tarea")
    @Order(3)
    void getAllTest() throws SQLException {
        tareaRepository.insert(t1);
        Optional<List<Tarea>> tareas = tareaRepository.findAll();
        assumeTrue(tareas.isPresent());
        Assertions.assertAll( "Comprobaciones test getAll",
                () -> Assertions.assertEquals(1, tareas.get().size()),
                () -> Assertions.assertEquals(t1.getId(), tareas.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), tareas.get().get(0).getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tareas.get().get(0).getIssue().getId())
        );
    }

    @Test
    @DisplayName("Find Tarea By ID")
    @Order(4)
    void getById() throws SQLException {
        tareaRepository.insert(t1);
        Optional<Tarea> tarea = tareaRepository.getById(t1.getId());
        assumeTrue(tarea.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(t1.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), tarea.get().getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tarea.get().getIssue().getId())
        );
    }

    @Test
    @DisplayName("Update Tarea")
    @Order(5)
    void update() throws SQLException {
        tareaRepository.insert(t1);
        Programador programador1 = new Programador();
        t1.setProgramador(programador1);
        Optional<Tarea> tarea = tareaRepository.update(t1);
        assumeTrue(tarea.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(t1.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(programador1.getId(), tarea.get().getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tarea.get().getIssue().getId())
        );
    }

    @Test
    @DisplayName("Delete Tarea")
    @Order(6)
    void delete() throws SQLException {
        tareaRepository.insert(t1);
        Optional<Tarea> tarea = tareaRepository.delete(t1.getId());
        assumeTrue(tarea.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(t1.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), tarea.get().getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tarea.get().getIssue().getId())
        );
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(7)
    void getByIdException() {
        Exception exception = assertThrows(SQLException.class,() -> {
            tareaRepository.getById(t2.getId());
        });

        String expectedMessage = "Error TareaRepository no se ha encontrado la ID " + t2.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}