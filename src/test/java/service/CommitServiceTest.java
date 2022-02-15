package service;

import dao.*;
import database.MongoDBController;
import dto.CommitDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import repository.*;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JUnit 5 Test Service Commit")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommitServiceTest {
    private CommitRepository commitRepository;
    private CommitService commitService;
    private Commit c1;
    private Commit c2;
    private CommitDTO dto;
    private CommitDTO dto1;
    private Repositorio repositorio;
    private Proyecto proyecto;
    private Programador programador;
    private Issue issue;
    private Departamento departamento;
    private Departamento departamento2;
    private Login login;
    private AccessHistory accessHistory;

    private final void initData() throws SQLException {
        Date date = new Date();
        //Date date1 = Date.valueOf("2022-01-21");
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        //Instanciar
        this.repositorio = new Repositorio("Ciencia", date, proyecto);
        this.issue = new Issue("Primera issue", "Issue inicial", date, proyecto, repositorio, c1, false);
        this.departamento = new Departamento("primer departamento",programador,
                10000.0, 10000000.0);
        this.departamento2 = new Departamento("segundo departamento",programador,
                10000.0, 10000000.0);
        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.proyecto = new Proyecto(programador, "Ciencia", 20000.00, date,
                date, technologies, repositorio, false, departamento);
        this.accessHistory = new AccessHistory(programador.getId(), LocalDateTime.now());
        this.login = new Login(programador, LocalDateTime.now(), "Token", accessHistory, false);
        this.c1 = new Commit("Repositorios", "Repositorios realizados", date, repositorio, proyecto, programador, issue);
        this.c2 = new Commit(null,null,null,null,null,null,null);

        //Insertar entidad necesaria para retricciones del servicio
        ProyectoService proyectoService = new ProyectoService(new ProyectoRepository());
        proyectoService.insert(proyecto);

        dto = CommitDTO.builder()
                .id(new ObjectId())
                .title("Repositorios")
                .text("Repositorios realizados")
                .date(date)
                .repositorio(repositorio)
                .proyecto(proyecto)
                .autorCommit(programador)
                .issueProcedente(issue)
                .build();

        dto1 = CommitDTO.builder()
                .title(null)
                .text(null)
                .date(null)
                .repositorio(null)
                .proyecto(null)
                .autorCommit(null)
                .issueProcedente(null)
                .build();
    }

    @BeforeEach
    public void setUp() throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        this.commitRepository = mock(CommitRepository.class);
        this.commitService = new CommitService(this.commitRepository);
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
    @DisplayName("Service findAll")
    @Order(2)
    void findAllTest() throws SQLException {
        List<Commit> lista = new ArrayList<>();
        lista.add(c1);
        Mockito.when(this.commitRepository.findAll()).thenReturn(Optional.of(lista));
        Optional<List<CommitDTO>> commits = commitService.getAllDTO();
        assumeTrue(commits.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, commits.get().size()),
                () -> Assertions.assertEquals(c1.getId(), commits.get().get(0).getId()),
                () -> Assertions.assertEquals(c1.getTitle(), commits.get().get(0).getTitle()),
                () -> Assertions.assertEquals(c1.getDate(), commits.get().get(0).getDate()),
                () -> Assertions.assertEquals(repositorio.getId(), commits.get().get(0).getRepositorio().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), commits.get().get(0).getProyecto().getId()),
                () -> Assertions.assertEquals(programador.getId(), commits.get().get(0).getAutorCommit().getId()),
                () -> Assertions.assertEquals(issue.getId(), commits.get().get(0).getIssueProcedente().getId())
        );
        Mockito.verify(commitRepository, Mockito.atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Service FindById")
    @Order(3)
    void findById() throws SQLException {
        Mockito.when(this.commitRepository.getById(c1.getId())).thenReturn(Optional.ofNullable(c1));
        Optional<Commit> commit = this.commitService.getById(c1.getId());
        assumeTrue(commit.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(c1.getId(), commit.get().getId()),
                () -> Assertions.assertEquals(c1.getTitle(), commit.get().getTitle()),
                () -> Assertions.assertEquals(c1.getDate(), commit.get().getDate()),
                () -> Assertions.assertEquals(repositorio.getId(), commit.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), commit.get().getProyecto().getId()),
                () -> Assertions.assertEquals(programador.getId(), commit.get().getAutorCommit().getId()),
                () -> Assertions.assertEquals(issue.getId(), commit.get().getIssueProcedente().getId())
        );
        Mockito.verify(commitRepository, Mockito.atLeastOnce()).getById(c1.getId());
    }

    @Test
    @DisplayName("Service Insert")
    @Order(4)
    void insertTest() throws SQLException {
        Mockito.when(commitRepository.insert(c1)).thenReturn(Optional.ofNullable(c1));
        Optional<Commit> commit = this.commitService.insert(c1);
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
        Mockito.verify(commitRepository, Mockito.atLeastOnce()).insert(c1);
    }

    @Test
    @DisplayName("Service Update")
    @Order(5)
    void updateTest() throws SQLException {
        Mockito.when(commitRepository.update(c1)).thenReturn(Optional.ofNullable(c1));
        Optional<Commit> commit = this.commitService.update(c1);
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
        Mockito.verify(commitRepository, Mockito.atLeastOnce()).update(c1);
    }

    @Test
    @DisplayName("Service delete")
    @Order(6)
    void deleteTest() throws SQLException {
        Mockito.when(commitRepository.delete(c1.getId())).thenReturn(Optional.ofNullable(c1));
        Optional<Commit> commit = this.commitService.delete(c1.getId());
        assumeTrue(commit.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(c1.getId(), commit.get().getId()),
                () -> Assertions.assertEquals(c1.getTitle(), commit.get().getTitle()),
                () -> Assertions.assertEquals(c1.getDate(), commit.get().getDate()),
                () -> Assertions.assertEquals(repositorio.getId(), commit.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), commit.get().getProyecto().getId()),
                () -> Assertions.assertEquals(programador.getId(), commit.get().getAutorCommit().getId()),
                () -> Assertions.assertEquals(issue.getId(), commit.get().getIssueProcedente().getId())
        );
        Mockito.verify(commitRepository, Mockito.atLeastOnce()).delete(c1.getId());
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(7)
    void getByIdException() {
        CommitRepository commitRepository = new CommitRepository();
        CommitService commitService = new CommitService(commitRepository);
        Exception exception = assertThrows(SQLException.class,() -> {
            commitService.getByIdDTO(dto1.getId());
        });

        String expectedMessage = "Error CommitRepository no se ha encontrado la ID " + dto1.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}