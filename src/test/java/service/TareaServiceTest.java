package service;

import dao.*;
import database.MongoDBController;
import dto.TareaDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import repository.TareaRepository;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;

@DisplayName("JUnit 5 Test Service Tarea")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TareaServiceTest {

    private TareaRepository tareaRepository;
    private TareaService tareaService;

    private Tarea t1;
    private TareaDTO tareaDTO;
    private TareaDTO dto1;
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
        tareaDTO = TareaDTO.builder()
                .id(new ObjectId())
                .programador(programador)
                .issue(issue)
                .build();
        dto1 = TareaDTO.builder()
                .id(new ObjectId())
                .programador(programador)
                .issue(issue)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        this.tareaRepository = mock(TareaRepository.class);
        this.tareaService = new TareaService(tareaRepository);
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
        List<Tarea> lista = new ArrayList<>();
        lista.add(t1);
        Mockito.when(this.tareaRepository.findAll()).thenReturn(Optional.of(lista));
        Optional<List<Tarea>> tareas = tareaService.findAll();
        assumeTrue(tareas.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, tareas.get().size()),
                () -> Assertions.assertEquals(t1.getId(), tareas.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), tareas.get().get(0).getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tareas.get().get(0).getIssue().getId())
        );
        Mockito.verify(tareaRepository, Mockito.atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Service FindById")
    @Order(3)
    void findById() throws SQLException {
        Mockito.when(this.tareaRepository.getById(t1.getId())).thenReturn(Optional.ofNullable(t1));
        Optional<Tarea> tarea = this.tareaService.getById(t1.getId());
        assumeTrue(tarea.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(t1.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), tarea.get().getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tarea.get().getIssue().getId())
        );
        Mockito.verify(tareaRepository, Mockito.atLeastOnce()).getById(t1.getId());
    }

    @Test
    @DisplayName("Service Insert")
    @Order(4)
    void insertTest() throws SQLException {
        Mockito.when(tareaRepository.insert(t1)).thenReturn(Optional.ofNullable(t1));
        Optional<Tarea> tarea = this.tareaService.insert(t1);
        assumeTrue(tarea.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(t1.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), tarea.get().getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tarea.get().getIssue().getId())
        );
        Mockito.verify(tareaRepository, Mockito.atLeastOnce()).insert(t1);
    }

    @Test
    @DisplayName("Service Update")
    @Order(5)
    void updateTest() throws SQLException {
        Mockito.when(tareaRepository.update(t1)).thenReturn(Optional.ofNullable(t1));
        Optional<Tarea> tarea = this.tareaService.update(t1);
        assumeTrue(tarea.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(t1.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), tarea.get().getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tarea.get().getIssue().getId())
        );
        Mockito.verify(tareaRepository, Mockito.atLeastOnce()).update(t1);
    }

    @Test
    @DisplayName("Service delete")
    @Order(6)
    void deleteTest() throws SQLException {
        Mockito.when(tareaRepository.delete(t1.getId())).thenReturn(Optional.ofNullable(t1));
        Optional<Tarea> tarea = this.tareaService.delete(t1.getId());
        assumeTrue(tarea.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(t1.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), tarea.get().getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tarea.get().getIssue().getId())
        );
        Mockito.verify(tareaRepository, Mockito.atLeastOnce()).delete(t1.getId());
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(8)
    void getByIdException() {
        TareaRepository tareaRepository = new TareaRepository();
        TareaService tareaService = new TareaService(tareaRepository);
        Exception exception = assertThrows(SQLException.class,() -> {
            tareaService.getByIdDTO(dto1.getId());
        });

        String expectedMessage = "Error TareaRepository no se ha encontrado la ID " + dto1.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}