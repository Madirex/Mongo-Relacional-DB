package controller;

import dao.*;
import database.MongoDBController;
import dto.ProyectoDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import service.ProyectoService;
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

@DisplayName("JUnit 5 y Mockito Test Controller Proyecto")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProyectoControllerTest {

    private ProyectoService proyectoService;
    private ProyectoController proyectoController;

    private Proyecto proyecto;
    private ProyectoDTO proyectoDTO;
    private Repositorio repositorio;
    private Programador programador;
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
        this.repositorio = new Repositorio("Ciencia", date, proyecto);
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.login = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);
        this.accessHistory = new AccessHistory(new ObjectId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        this.proyecto = new Proyecto(programador, "Ciencia", 20000.00, date,
                date, technologies, repositorio, false, departamento);
        proyectoDTO = ProyectoDTO.builder()
                .id(new ObjectId())
                .jefeProyecto(programador)
                .nombre("Ciencia")
                .presupuesto(20000.00)
                .fechaInicio(date)
                .fechaFin(date)
                .usedTechnologies(technologies)
                .repositorio(repositorio)
                .esAcabado(false)
                .departamento(departamento)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        this.proyectoService = mock(ProyectoService.class);
        this.proyectoController = new ProyectoController(proyectoService);
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
        List<ProyectoDTO> lista = new ArrayList<>();
        lista.add(proyectoDTO);
        Mockito.when(this.proyectoService.getAllDTO()).thenReturn(Optional.of(lista));
        Optional<List<ProyectoDTO>> proyectos = proyectoController.getAll();
        assumeTrue(proyectos.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, proyectos.get().size()),
                () -> Assertions.assertEquals(proyectoDTO.getId(), proyectos.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), proyectos.get().get(0).getJefeProyecto().getId()),
                () -> Assertions.assertEquals(proyectoDTO.getNombre(), proyectos.get().get(0).getNombre()),
                () -> Assertions.assertEquals(proyectoDTO.getPresupuesto(), proyectos.get().get(0).getPresupuesto()),
                () -> Assertions.assertEquals(proyectoDTO.getFechaInicio(), proyectos.get().get(0).getFechaInicio()),
                () -> Assertions.assertEquals(proyectoDTO.getFechaFin(), proyectos.get().get(0).getFechaFin()),
                () -> Assertions.assertEquals(proyectoDTO.getUsedTechnologies(), proyectos.get().get(0).getUsedTechnologies()),
                () -> Assertions.assertEquals(repositorio.getId(), proyectos.get().get(0).getRepositorio().getId()),
                () -> Assertions.assertEquals(proyectoDTO.isEsAcabado(), proyectos.get().get(0).isEsAcabado()),
                () -> Assertions.assertEquals(departamento.getId(), proyectos.get().get(0).getDepartamento().getId())
        );
        Mockito.verify(proyectoService, Mockito.atLeastOnce()).getAllDTO();
    }

    @Test
    @DisplayName("Controller FindById")
    @Order(3)
    void getById() throws SQLException {
        Mockito.when(this.proyectoService.getByIdDTO(proyectoDTO.getId())).thenReturn(Optional.ofNullable(proyectoDTO));
        Optional<ProyectoDTO> proyecto = this.proyectoController.getById(proyectoDTO.getId());
        assumeTrue(proyecto.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(proyectoDTO.getId(), proyecto.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), proyecto.get().getJefeProyecto().getId()),
                () -> Assertions.assertEquals(proyectoDTO.getNombre(), proyecto.get().getNombre()),
                () -> Assertions.assertEquals(proyectoDTO.getPresupuesto(), proyecto.get().getPresupuesto()),
                () -> Assertions.assertEquals(proyectoDTO.getFechaInicio(), proyecto.get().getFechaInicio()),
                () -> Assertions.assertEquals(proyectoDTO.getFechaFin(), proyecto.get().getFechaFin()),
                () -> Assertions.assertEquals(proyectoDTO.getUsedTechnologies(), proyecto.get().getUsedTechnologies()),
                () -> Assertions.assertEquals(repositorio.getId(), proyecto.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(proyectoDTO.isEsAcabado(), proyecto.get().isEsAcabado()),
                () -> Assertions.assertEquals(departamento.getId(), proyecto.get().getDepartamento().getId())
        );
        Mockito.verify(proyectoService, Mockito.atLeastOnce()).getByIdDTO(proyectoDTO.getId());
    }

    @Test
    @DisplayName("Controller Insert")
    @Order(4)
    void insert() throws SQLException {
        Mockito.when(proyectoService.insertDTO(proyectoDTO)).thenReturn(Optional.ofNullable(proyectoDTO));
        Optional<ProyectoDTO> proyecto = this.proyectoController.insert(proyectoDTO);
        assumeTrue(proyecto.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(proyectoDTO.getId(), proyecto.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), proyecto.get().getJefeProyecto().getId()),
                () -> Assertions.assertEquals(proyectoDTO.getNombre(), proyecto.get().getNombre()),
                () -> Assertions.assertEquals(proyectoDTO.getPresupuesto(), proyecto.get().getPresupuesto()),
                () -> Assertions.assertEquals(proyectoDTO.getFechaInicio(), proyecto.get().getFechaInicio()),
                () -> Assertions.assertEquals(proyectoDTO.getFechaFin(), proyecto.get().getFechaFin()),
                () -> Assertions.assertEquals(proyectoDTO.getUsedTechnologies(), proyecto.get().getUsedTechnologies()),
                () -> Assertions.assertEquals(repositorio.getId(), proyecto.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(proyectoDTO.isEsAcabado(), proyecto.get().isEsAcabado()),
                () -> Assertions.assertEquals(departamento.getId(), proyecto.get().getDepartamento().getId())
        );
        Mockito.verify(proyectoService, Mockito.atLeastOnce()).insertDTO(proyectoDTO);
    }

    @Test
    @DisplayName("Controller Update")
    @Order(5)
    void update() throws SQLException {
        Mockito.when(proyectoService.updateDTO(proyectoDTO)).thenReturn(Optional.ofNullable(proyectoDTO));
        Optional<ProyectoDTO> proyecto = this.proyectoController.update(proyectoDTO);
        assumeTrue(proyecto.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(proyectoDTO.getId(), proyecto.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), proyecto.get().getJefeProyecto().getId()),
                () -> Assertions.assertEquals(proyectoDTO.getNombre(), proyecto.get().getNombre()),
                () -> Assertions.assertEquals(proyectoDTO.getPresupuesto(), proyecto.get().getPresupuesto()),
                () -> Assertions.assertEquals(proyectoDTO.getFechaInicio(), proyecto.get().getFechaInicio()),
                () -> Assertions.assertEquals(proyectoDTO.getFechaFin(), proyecto.get().getFechaFin()),
                () -> Assertions.assertEquals(proyectoDTO.getUsedTechnologies(), proyecto.get().getUsedTechnologies()),
                () -> Assertions.assertEquals(repositorio.getId(), proyecto.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(proyectoDTO.isEsAcabado(), proyecto.get().isEsAcabado()),
                () -> Assertions.assertEquals(departamento.getId(), proyecto.get().getDepartamento().getId())
        );
        Mockito.verify(proyectoService, Mockito.atLeastOnce()).updateDTO(proyectoDTO);
    }

    @Test
    @DisplayName("Controller delete")
    @Order(6)
    void delete() throws SQLException {
        Mockito.when(proyectoService.deleteDTO(proyectoDTO.getId())).thenReturn(Optional.ofNullable(proyectoDTO));
        Optional<ProyectoDTO> proyecto = this.proyectoController.delete(proyectoDTO.getId());
        assumeTrue(proyecto.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(proyectoDTO.getId(), proyecto.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), proyecto.get().getJefeProyecto().getId()),
                () -> Assertions.assertEquals(proyectoDTO.getNombre(), proyecto.get().getNombre()),
                () -> Assertions.assertEquals(proyectoDTO.getPresupuesto(), proyecto.get().getPresupuesto()),
                () -> Assertions.assertEquals(proyectoDTO.getFechaInicio(), proyecto.get().getFechaInicio()),
                () -> Assertions.assertEquals(proyectoDTO.getFechaFin(), proyecto.get().getFechaFin()),
                () -> Assertions.assertEquals(proyectoDTO.getUsedTechnologies(), proyecto.get().getUsedTechnologies()),
                () -> Assertions.assertEquals(repositorio.getId(), proyecto.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(proyectoDTO.isEsAcabado(), proyecto.get().isEsAcabado()),
                () -> Assertions.assertEquals(departamento.getId(), proyecto.get().getDepartamento().getId())
        );
        Mockito.verify(proyectoService, Mockito.atLeastOnce()).deleteDTO(proyectoDTO.getId());
    }
}