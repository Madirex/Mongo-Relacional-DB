package repository;

import dao.*;
import database.MongoDBController;
import org.junit.jupiter.api.*;
import util.ApplicationProperties;

import static org.junit.jupiter.api.Assumptions.assumeTrue;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JUnit 5 Test CRUD Commit")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommitRepositoryTest {
    private CommitRepository commitRepository = new CommitRepository();

    private Commit c1;
    private Commit c2;
    private Repositorio repositorio;
    private Proyecto proyecto;
    private Programador programador;
    private Issue issue;
    private Departamento departamento;
    private Login login;
    private AccessHistory accessHistory;

    private final void initData() {
        Date date = new Date();
        //Date date1 = Date.valueOf("2022-01-21");
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.repositorio = new Repositorio("Ciencia", date, proyecto);
        this.proyecto = new Proyecto(programador, "Ciencia", 20000.00, date,
                date, technologies, repositorio, false, departamento);
        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.issue = new Issue("Primera issue", "Issue inicial", date, proyecto, repositorio, c1, false);
        this.departamento = new Departamento("primer departamento",programador,
                10000.0, 10000000.0);
        this.login = new Login(programador, LocalDateTime.now(), "Token", accessHistory, false);
        this.accessHistory = new AccessHistory(programador.getId(), LocalDateTime.now());

        this.c1 = new Commit("Repositorios", "Repositorios realizados", date, repositorio, proyecto, programador, issue);
        this.c2 = new Commit(null,null,null,null,null,null,null);
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
        Optional<Commit> commit = commitRepository.insert(c1);
        assumeTrue(commit.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(c1.getId(), commit.get().getId()),
                () -> Assertions.assertEquals(c1.getTitle(), commit.get().getTitle()),
                () -> Assertions.assertEquals(c1.getDate(), commit.get().getDate()),
                () -> Assertions.assertEquals(c1.getRepositorio(), commit.get().getRepositorio()),
                () -> Assertions.assertEquals(c1.getProyecto(), commit.get().getProyecto()),
                () -> Assertions.assertEquals(c1.getAutorCommit(), commit.get().getAutorCommit()),
                () -> Assertions.assertEquals(c1.getIssueProcedente(), commit.get().getIssueProcedente())
        );
    }

    @Test
    @DisplayName("Get all Commit")
    @Order(3)
    void getAllTest() throws SQLException {
        commitRepository.insert(c1);
        Optional<List<Commit>> commits = commitRepository.findAll();
        assumeTrue(commits.isPresent());
        Assertions.assertAll( "Comprobaciones test getAll",
                () -> Assertions.assertEquals(1, commits.get().size()),
                () -> Assertions.assertEquals(c1.getId(), commits.get().get(0).getId()),
                () -> Assertions.assertEquals(c1.getTitle(), commits.get().get(0).getTitle()),
                () -> Assertions.assertEquals(c1.getDate(), commits.get().get(0).getDate()),
                () -> Assertions.assertEquals(repositorio.getId(), commits.get().get(0).getRepositorio().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), commits.get().get(0).getProyecto().getId()),
                () -> Assertions.assertEquals(programador.getId(), commits.get().get(0).getAutorCommit().getId()),
                () -> Assertions.assertEquals(issue.getId(), commits.get().get(0).getIssueProcedente().getId())
//                () -> Assertions.assertEquals(c1.getRepositorio(), commits.get().get(0).getRepositorio()),
//                () -> Assertions.assertEquals(c1.getProyecto(), commits.get().get(0).getProyecto()),
//                () -> Assertions.assertEquals(c1.getAutorCommit(), commits.get().get(0).getAutorCommit()),
//                () -> Assertions.assertEquals(c1.getIssueProcedente(), commits.get().get(0).getIssueProcedente())
        );
    }

    @Test
    @DisplayName("Find Commit By ID")
    @Order(4)
    void getById() throws SQLException {
        commitRepository.insert(c1);
        Optional<Commit> commit = commitRepository.getById(c1.getId());
        assumeTrue(commit.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(c1.getId(), commit.get().getId()),
                () -> Assertions.assertEquals(c1.getTitle(), commit.get().getTitle()),
                () -> Assertions.assertEquals(c1.getDate(), commit.get().getDate()),
                () -> Assertions.assertEquals(repositorio.getId(), commit.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), commit.get().getProyecto().getId()),
                () -> Assertions.assertEquals(programador.getId(), commit.get().getAutorCommit().getId()),
                () -> Assertions.assertEquals(issue.getId(), commit.get().getIssueProcedente().getId())
        );
    }

    @Test
    @DisplayName("Update Commit")
    @Order(5)
    void update() throws SQLException {
        commitRepository.insert(c1);
        c1.setText("Texto modificado");
        c1.setTitle("Titulo modificado");
        Optional<Commit> commit = commitRepository.update(c1);
        assumeTrue(commit.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(c1.getId(), commit.get().getId()),
                () -> Assertions.assertEquals(c1.getTitle(), commit.get().getTitle()),
                () -> Assertions.assertEquals(c1.getDate(), commit.get().getDate()),
                () -> Assertions.assertEquals(repositorio.getId(), commit.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), commit.get().getProyecto().getId()),
                () -> Assertions.assertEquals(programador.getId(), commit.get().getAutorCommit().getId()),
                () -> Assertions.assertEquals(issue.getId(), commit.get().getIssueProcedente().getId())
        );
    }

    @Test
    @DisplayName("Delete Commit")
    @Order(6)
    void delete() throws SQLException {
        commitRepository.insert(c1);
        Optional<Commit> commit = commitRepository.delete(c1.getId());
        assumeTrue(commit.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(c1.getId(), commit.get().getId()),
                () -> Assertions.assertEquals(c1.getTitle(), commit.get().getTitle()),
                () -> Assertions.assertEquals(c1.getDate(), commit.get().getDate()),
                () -> Assertions.assertEquals(repositorio.getId(), commit.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), commit.get().getProyecto().getId()),
                () -> Assertions.assertEquals(programador.getId(), commit.get().getAutorCommit().getId()),
                () -> Assertions.assertEquals(issue.getId(), commit.get().getIssueProcedente().getId())
        );
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(7)
    void getByIdException() {
        Exception exception = assertThrows(SQLException.class,() -> {
           commitRepository.getById(c2.getId());
        });

        String expectedMessage = "Error CommitRepository no se ha encontrado la ID " + c2.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}