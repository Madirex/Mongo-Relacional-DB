package controller;

import dao.*;
import database.MongoDBController;
import dto.IssueDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import service.IssueService;
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
import static org.mockito.Mockito.mock;

@DisplayName("JUnit 5 y Mockito Test Controller Issue")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IssueControllerTest {

    private IssueController issueController;
    private IssueService issueService;

    private Issue i1;
    private IssueDTO issueDTO;
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
        issueDTO = IssueDTO.builder()
                .id(new ObjectId())
                .title("Primera issue")
                .text("Esta es la primera issue")
                .fecha(date)
                .proyecto(proyecto)
                .repositorioAsignado(repositorio)
                .commit(commit)
                .esAcabado(false)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        this.issueService = mock(IssueService.class);
        this.issueController = new IssueController(issueService);
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
    @DisplayName("Controller getAll")
    @Order(2)
    void getAll() throws SQLException {
        List<IssueDTO> lista = new ArrayList<>();
        lista.add(issueDTO);
        Mockito.when(this.issueService.getAllDTO()).thenReturn(Optional.of(lista));
        Optional<List<IssueDTO>> issues = issueController.getAll();
        assumeTrue(issues.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, issues.get().size()),
                () -> Assertions.assertEquals(issueDTO.getId(), issues.get().get(0).getId()),
                () -> Assertions.assertEquals(issueDTO.getTitle(), issues.get().get(0).getTitle()),
                () -> Assertions.assertEquals(issueDTO.getText(), issues.get().get(0).getText()),
                () -> Assertions.assertEquals(issueDTO.getFecha(), issues.get().get(0).getFecha()),
                () -> Assertions.assertEquals(proyecto.getId(), issues.get().get(0).getProyecto().getId()),
                () -> Assertions.assertEquals(repositorio.getId(), issues.get().get(0).getRepositorioAsignado().getId()),
                () -> Assertions.assertEquals(commit.getId(), issues.get().get(0).getCommit().getId()),
                () -> Assertions.assertEquals(issueDTO.isEsAcabado(), issues.get().get(0).isEsAcabado())
        );
        Mockito.verify(issueService, Mockito.atLeastOnce()).getAllDTO();
    }

    @Test
    @DisplayName("Controller getById")
    @Order(3)
    void getById() throws SQLException {
        Mockito.when(this.issueService.getByIdDTO(issueDTO.getId())).thenReturn(Optional.ofNullable(issueDTO));
        Optional<IssueDTO> issue = this.issueController.getById(issueDTO.getId());
        assumeTrue(issue.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(issueDTO.getId(), issue.get().getId()),
                () -> Assertions.assertEquals(issueDTO.getTitle(), issue.get().getTitle()),
                () -> Assertions.assertEquals(issueDTO.getText(), issue.get().getText()),
                () -> Assertions.assertEquals(issueDTO.getFecha(), issue.get().getFecha()),
                () -> Assertions.assertEquals(issueDTO.getProyecto(), issue.get().getProyecto()),
                () -> Assertions.assertEquals(issueDTO.getRepositorioAsignado(), issue.get().getRepositorioAsignado()),
                () -> Assertions.assertEquals(issueDTO.getCommit(), issue.get().getCommit()),
                () -> Assertions.assertEquals(issueDTO.isEsAcabado(), issue.get().isEsAcabado())
        );
        Mockito.verify(issueService, Mockito.atLeastOnce()).getByIdDTO(issueDTO.getId());
    }

    @Test
    @DisplayName("Controller Insert")
    @Order(4)
    void insert() throws SQLException {
        Mockito.when(issueService.insertDTO(issueDTO)).thenReturn(Optional.ofNullable(issueDTO));
        Optional<IssueDTO> issue = this.issueController.insert(issueDTO);
        assumeTrue(issue.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(issueDTO.getId(), issue.get().getId()),
                () -> Assertions.assertEquals(issueDTO.getTitle(), issue.get().getTitle()),
                () -> Assertions.assertEquals(issueDTO.getText(), issue.get().getText()),
                () -> Assertions.assertEquals(issueDTO.getFecha(), issue.get().getFecha()),
                () -> Assertions.assertEquals(issueDTO.getProyecto(), issue.get().getProyecto()),
                () -> Assertions.assertEquals(issueDTO.getRepositorioAsignado(), issue.get().getRepositorioAsignado()),
                () -> Assertions.assertEquals(issueDTO.getCommit(), issue.get().getCommit()),
                () -> Assertions.assertEquals(issueDTO.isEsAcabado(), issue.get().isEsAcabado())
        );
        Mockito.verify(issueService, Mockito.atLeastOnce()).insertDTO(issueDTO);
    }

    @Test
    @DisplayName("Controller Update")
    @Order(5)
    void update() throws SQLException {
        Mockito.when(issueService.updateDTO(issueDTO)).thenReturn(Optional.ofNullable(issueDTO));
        Optional<IssueDTO> issue = this.issueController.update(issueDTO);
        assumeTrue(issue.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(issueDTO.getId(), issue.get().getId()),
                () -> Assertions.assertEquals(issueDTO.getTitle(), issue.get().getTitle()),
                () -> Assertions.assertEquals(issueDTO.getText(), issue.get().getText()),
                () -> Assertions.assertEquals(issueDTO.getFecha(), issue.get().getFecha()),
                () -> Assertions.assertEquals(issueDTO.getProyecto(), issue.get().getProyecto()),
                () -> Assertions.assertEquals(issueDTO.getRepositorioAsignado(), issue.get().getRepositorioAsignado()),
                () -> Assertions.assertEquals(issueDTO.getCommit(), issue.get().getCommit()),
                () -> Assertions.assertEquals(issueDTO.isEsAcabado(), issue.get().isEsAcabado())
        );
        Mockito.verify(issueService, Mockito.atLeastOnce()).updateDTO(issueDTO);
    }

    @Test
    @DisplayName("Controller delete")
    @Order(6)
    void delete() throws SQLException {
        Mockito.when(issueService.deleteDTO(issueDTO.getId())).thenReturn(Optional.ofNullable(issueDTO));
        Optional<IssueDTO> issue = this.issueController.delete(issueDTO.getId());
        assumeTrue(issue.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(issueDTO.getId(), issue.get().getId()),
                () -> Assertions.assertEquals(issueDTO.getTitle(), issue.get().getTitle()),
                () -> Assertions.assertEquals(issueDTO.getText(), issue.get().getText()),
                () -> Assertions.assertEquals(issueDTO.getFecha(), issue.get().getFecha()),
                () -> Assertions.assertEquals(issueDTO.getProyecto(), issue.get().getProyecto()),
                () -> Assertions.assertEquals(issueDTO.getRepositorioAsignado(), issue.get().getRepositorioAsignado()),
                () -> Assertions.assertEquals(issueDTO.getCommit(), issue.get().getCommit()),
                () -> Assertions.assertEquals(issueDTO.isEsAcabado(), issue.get().isEsAcabado())
        );
        Mockito.verify(issueService, Mockito.atLeastOnce()).deleteDTO(issueDTO.getId());
    }


}