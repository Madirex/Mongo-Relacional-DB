package service;

import dao.*;
import database.MongoDBController;
import dto.IssueDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import repository.IssueRepository;
import util.ApplicationProperties;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JUnit 5 Test Service Issue")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IssueServiceTest {

    private IssueRepository issueRepository;
    private IssueService issueService;

    private Issue i1;
    private Proyecto proyecto;
    private Repositorio repositorio;
    private Commit commit;
    private Programador programador;
    private AccessHistory accessHistory;
    private Departamento departamento;
    private Login login;
    private IssueDTO dto;
    private IssueDTO dto1;

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
        dto = IssueDTO.builder()
                .id(new ObjectId())
                .title("Primera issue")
                .text("Esta es la primera issue")
                .fecha(date)
                .proyecto(proyecto)
                .repositorioAsignado(repositorio)
                .commit(commit)
                .esAcabado(false)
                .build();

        dto1 = IssueDTO.builder()
                .title(null)
                .text(null)
                .fecha(null)
                .proyecto(null)
                .repositorioAsignado(null)
                .commit(null)
                .esAcabado(false)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        this.issueRepository = mock(IssueRepository.class);
        this.issueService = new IssueService(issueRepository);
        initData();
    }

    @Test
    @DisplayName("True is true")
    @Order(1)
    void trueIsTrue() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Service findAll")
    @Order(2)
    void findAllTest() throws SQLException {
        List<Issue> lista = new ArrayList<>();
        lista.add(i1);
        Mockito.when(this.issueRepository.findAll()).thenReturn(Optional.of(lista));
        Optional<List<Issue>> issues = issueService.findAll();
        assumeTrue(issues.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
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
        Mockito.verify(issueRepository, Mockito.atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Service FindById")
    @Order(3)
    void findById() throws SQLException {
        Mockito.when(this.issueRepository.getById(i1.getId())).thenReturn(Optional.ofNullable(i1));
        Optional<Issue> issue = this.issueService.getById(i1.getId());
        assumeTrue(issue.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(i1.getId(), issue.get().getId()),
                () -> Assertions.assertEquals(i1.getTitle(), issue.get().getTitle()),
                () -> Assertions.assertEquals(i1.getText(), issue.get().getText()),
                () -> Assertions.assertEquals(i1.getFecha(), issue.get().getFecha()),
                () -> Assertions.assertEquals(i1.getProyecto(), issue.get().getProyecto()),
                () -> Assertions.assertEquals(i1.getRepositorioAsignado(), issue.get().getRepositorioAsignado()),
                () -> Assertions.assertEquals(i1.getCommit(), issue.get().getCommit()),
                () -> Assertions.assertEquals(i1.isAcabado(), issue.get().isAcabado())
        );
        Mockito.verify(issueRepository, Mockito.atLeastOnce()).getById(i1.getId());
    }

    @Test
    @DisplayName("Service Insert")
    @Order(4)
    void insertTest() throws SQLException {
        Mockito.when(issueRepository.insert(i1)).thenReturn(Optional.ofNullable(i1));
        Optional<Issue> issue = this.issueService.insert(i1);
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
        Mockito.verify(issueRepository, Mockito.atLeastOnce()).insert(i1);
    }

    @Test
    @DisplayName("Service Update")
    @Order(5)
    void updateTest() throws SQLException {
        Mockito.when(issueService.update(i1)).thenReturn(Optional.ofNullable(i1));
        Optional<Issue> issue = this.issueService.update(i1);
        assumeTrue(issue.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(i1.getId(), issue.get().getId()),
                () -> Assertions.assertEquals(i1.getTitle(), issue.get().getTitle()),
                () -> Assertions.assertEquals(i1.getText(), issue.get().getText()),
                () -> Assertions.assertEquals(i1.getFecha(), issue.get().getFecha()),
                () -> Assertions.assertEquals(i1.getProyecto(), issue.get().getProyecto()),
                () -> Assertions.assertEquals(i1.getRepositorioAsignado(), issue.get().getRepositorioAsignado()),
                () -> Assertions.assertEquals(i1.getCommit(), issue.get().getCommit()),
                () -> Assertions.assertEquals(i1.isAcabado(), issue.get().isAcabado())
        );
        Mockito.verify(issueRepository, Mockito.atLeastOnce()).update(i1);
    }

    @Test
    @DisplayName("Service delete")
    @Order(6)
    void deleteTest() throws SQLException {
        Mockito.when(issueRepository.delete(i1.getId())).thenReturn(Optional.ofNullable(i1));
        Optional<Issue> issue = this.issueService.delete(i1.getId());
        assumeTrue(issue.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(i1.getId(), issue.get().getId()),
                () -> Assertions.assertEquals(i1.getTitle(), issue.get().getTitle()),
                () -> Assertions.assertEquals(i1.getText(), issue.get().getText()),
                () -> Assertions.assertEquals(i1.getFecha(), issue.get().getFecha()),
                () -> Assertions.assertEquals(i1.getProyecto(), issue.get().getProyecto()),
                () -> Assertions.assertEquals(i1.getRepositorioAsignado(), issue.get().getRepositorioAsignado()),
                () -> Assertions.assertEquals(i1.getCommit(), issue.get().getCommit()),
                () -> Assertions.assertEquals(i1.isAcabado(), issue.get().isAcabado())
        );
        Mockito.verify(issueRepository, Mockito.atLeastOnce()).delete(i1.getId());
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(8)
    void getByIdException() {
        IssueRepository issueRepository = new IssueRepository();
        IssueService issueService = new IssueService(issueRepository);
        Exception exception = assertThrows(SQLException.class,() -> {
            issueService.getByIdDTO(dto1.getId());
        });

        String expectedMessage = "Error IssueRepository no se ha encontrado la ID " + dto1.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}