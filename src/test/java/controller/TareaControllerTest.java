package controller;

import dao.*;
import database.MongoDBController;
import dto.TareaDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import service.TareaService;
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

@DisplayName("JUnit 5 y Mockito Test Controller Tarea")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TareaControllerTest {

    private TareaService tareaService;
    private TareaController tareaController;

    private Tarea tarea;
    private TareaDTO tareaDTO;
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

        this.tarea = new Tarea(programador, issue);
        tareaDTO = TareaDTO.builder()
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
        this.tareaService = mock(TareaService.class);
        this.tareaController = new TareaController(tareaService);
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
        List<TareaDTO> lista = new ArrayList<>();
        lista.add(tareaDTO);
        Mockito.when(this.tareaService.getAllDTO()).thenReturn(Optional.of(lista));
        Optional<List<TareaDTO>> tareas = tareaController.getAll();
        assumeTrue(tareas.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, tareas.get().size()),
                () -> Assertions.assertEquals(tareaDTO.getId(), tareas.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), tareas.get().get(0).getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tareas.get().get(0).getIssue().getId())
        );
        Mockito.verify(tareaService, Mockito.atLeastOnce()).getAllDTO();
    }

    @Test
    @DisplayName("Controller getById")
    @Order(3)
    void getById() throws SQLException {
        Mockito.when(this.tareaService.getByIdDTO(tareaDTO.getId())).thenReturn(Optional.ofNullable(tareaDTO));
        Optional<TareaDTO> tarea = this.tareaController.getById(tareaDTO.getId());
        assumeTrue(tarea.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(tareaDTO.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), tarea.get().getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tarea.get().getIssue().getId())
        );
        Mockito.verify(tareaService, Mockito.atLeastOnce()).getByIdDTO(tareaDTO.getId());
    }

    @Test
    @DisplayName("Controller Insert")
    @Order(4)
    void insert() throws SQLException {
        Mockito.when(tareaService.insertDTO(tareaDTO)).thenReturn(Optional.ofNullable(tareaDTO));
        Optional<TareaDTO> tarea = this.tareaController.insert(tareaDTO);
        assumeTrue(tarea.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(tareaDTO.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), tarea.get().getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tarea.get().getIssue().getId())
        );
        Mockito.verify(tareaService, Mockito.atLeastOnce()).insertDTO(tareaDTO);
    }

    @Test
    @DisplayName("Controller Update")
    @Order(5)
    void update() throws SQLException {
        Mockito.when(tareaService.updateDTO(tareaDTO)).thenReturn(Optional.ofNullable(tareaDTO));
        Optional<TareaDTO> tarea = this.tareaController.update(tareaDTO);
        assumeTrue(tarea.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(tareaDTO.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), tarea.get().getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tarea.get().getIssue().getId())
        );
        Mockito.verify(tareaService, Mockito.atLeastOnce()).updateDTO(tareaDTO);
    }

    @Test
    @DisplayName("Controller delete")
    @Order(6)
    void delete() throws SQLException {
        Mockito.when(tareaService.deleteDTO(tareaDTO.getId())).thenReturn(Optional.ofNullable(tareaDTO));
        Optional<TareaDTO> tarea = this.tareaController.delete(tareaDTO.getId());
        assumeTrue(tarea.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(tareaDTO.getId(), tarea.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), tarea.get().getProgramador().getId()),
                () -> Assertions.assertEquals(issue.getId(), tarea.get().getIssue().getId())
        );
        Mockito.verify(tareaService, Mockito.atLeastOnce()).deleteDTO(tareaDTO.getId());
    }
}