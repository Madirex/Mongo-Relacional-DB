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

@DisplayName("JUnit 5 Test CRUD Issue")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IssueRepositoryTest {

    private IssueRepository issueRepository = new IssueRepository();

    private Issue i1;
    private Issue i2;
    private Proyecto proyecto;
    private Repositorio repositorio;
    private Commit commit;
    private Programador programador;
    private AccessHistory accessHistory;
    private Departamento departamento;
    private Login login;

    private final void initData() {
        Date date = new Date();
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.repositorio = new Repositorio("Ciencia", date, proyecto);
        this.proyecto = new Proyecto(programador, "Ciencia", 20000.00, date,
                date, technologies, repositorio, false, departamento);
        this.commit = new Commit("Repositorios", "Repositorios realizados", date, repositorio, proyecto, programador, i1);
        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.accessHistory = new AccessHistory(programador.getId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        this.login = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);

        this.i1 = new Issue("Primera issue", "Esta es la primera issue", date, proyecto, repositorio, commit, false);
        this.i2 = new Issue(null, null, null, null, null, null, false);
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
    @DisplayName("Respository Insert Issue")
    @Order(2)
    void insertTest() throws SQLException {
        Optional<Issue> issue = issueRepository.insert(i1);
        assumeTrue(issue.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(i1.getId(), issue.get().getId()),
                () -> Assertions.assertEquals(i1.getTitle(), issue.get().getTitle()),
                () -> Assertions.assertEquals(i1.getText(), issue.get().getText()),
                () -> Assertions.assertEquals(i1.getFecha(), issue.get().getFecha()),
                () -> Assertions.assertEquals(i1.getProyecto(), issue.get().getProyecto()),
                () -> Assertions.assertEquals(i1.getRepositorioAsignado(), issue.get().getRepositorioAsignado()),
                () -> Assertions.assertEquals(i1.getCommit(), issue.get().getCommit()),
                () -> Assertions.assertEquals(i1.isAcabado(), issue.get().isAcabado())
        );
    }

    @Test
    @DisplayName("Get all Issue")
    @Order(3)
    void getAllTest() throws SQLException {
        issueRepository.insert(i1);
        Optional<List<Issue>> issues = issueRepository.findAll();
        assumeTrue(issues.isPresent());
        Assertions.assertAll( "Comprobaciones test getAll",
                () -> Assertions.assertEquals(1, issues.get().size()),
                () -> Assertions.assertEquals(i1.getId(), issues.get().get(0).getId()),
                () -> Assertions.assertEquals(i1.getTitle(), issues.get().get(0).getTitle()),
                () -> Assertions.assertEquals(i1.getText(), issues.get().get(0).getText()),
                () -> Assertions.assertEquals(i1.getFecha(), issues.get().get(0).getFecha()),
                () -> Assertions.assertEquals(proyecto.getId(), issues.get().get(0).getProyecto().getId()),
                () -> Assertions.assertEquals(repositorio.getId(), issues.get().get(0).getRepositorioAsignado().getId()),
                () -> Assertions.assertEquals(commit.getId(), issues.get().get(0).getCommit().getId()),
                () -> Assertions.assertEquals(i1.isAcabado(), issues.get().get(0).isAcabado())
        );
    }

    @Test
    @DisplayName("Find Issue By ID")
    @Order(4)
    void getById() throws SQLException {
        issueRepository.insert(i1);
        Optional<Issue> issue = issueRepository.getById(i1.getId());
        assumeTrue(issue.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(i1.getId(), issue.get().getId()),
                () -> Assertions.assertEquals(i1.getTitle(), issue.get().getTitle()),
                () -> Assertions.assertEquals(i1.getText(), issue.get().getText()),
                () -> Assertions.assertEquals(i1.getFecha(), issue.get().getFecha()),
                () -> Assertions.assertEquals(proyecto.getId(), issue.get().getProyecto().getId()),
                () -> Assertions.assertEquals(repositorio.getId(), issue.get().getRepositorioAsignado().getId()),
                () -> Assertions.assertEquals(commit.getId(), issue.get().getCommit().getId()),
                () -> Assertions.assertEquals(i1.isAcabado(), issue.get().isAcabado())
        );
    }

    @Test
    @DisplayName("Update Issue")
    @Order(5)
    void update() throws SQLException {
        issueRepository.insert(i1);
        i1.setText("Nuevo texto");
        i1.setTitle("Nuevo titulo");
        Optional<Issue> issue = issueRepository.update(i1);
        assumeTrue(issue.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(i1.getId(), issue.get().getId()),
                () -> Assertions.assertEquals(i1.getTitle(), issue.get().getTitle()),
                () -> Assertions.assertEquals(i1.getText(), issue.get().getText()),
                () -> Assertions.assertEquals(i1.getFecha(), issue.get().getFecha()),
                () -> Assertions.assertEquals(proyecto.getId(), issue.get().getProyecto().getId()),
                () -> Assertions.assertEquals(repositorio.getId(), issue.get().getRepositorioAsignado().getId()),
                () -> Assertions.assertEquals(commit.getId(), issue.get().getCommit().getId()),
                () -> Assertions.assertEquals(i1.isAcabado(), issue.get().isAcabado())
        );
    }

    @Test
    @DisplayName("Delete Issue")
    @Order(6)
    void delete() throws SQLException {
        issueRepository.insert(i1);
        Optional<Issue> issue = issueRepository.delete(i1.getId());
        assumeTrue(issue.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(i1.getId(), issue.get().getId()),
                () -> Assertions.assertEquals(i1.getTitle(), issue.get().getTitle()),
                () -> Assertions.assertEquals(i1.getText(), issue.get().getText()),
                () -> Assertions.assertEquals(i1.getFecha(), issue.get().getFecha()),
                () -> Assertions.assertEquals(proyecto.getId(), issue.get().getProyecto().getId()),
                () -> Assertions.assertEquals(repositorio.getId(), issue.get().getRepositorioAsignado().getId()),
                () -> Assertions.assertEquals(commit.getId(), issue.get().getCommit().getId()),
                () -> Assertions.assertEquals(i1.isAcabado(), issue.get().isAcabado())
        );
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(7)
    void getByIdException() {
        Exception exception = assertThrows(SQLException.class,() -> {
            issueRepository.getById(i2.getId());
        });

        String expectedMessage = "Error IssueRepository no se ha encontrado la ID " + i2.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}