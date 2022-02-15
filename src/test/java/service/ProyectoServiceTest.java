package service;

import dao.*;
import database.MongoDBController;
import dto.ProyectoDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import repository.ProyectoRepository;
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

@DisplayName("JUnit 5 Test Service Proyecto")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProyectoServiceTest {

    private ProyectoRepository proyectoRepository;
    private ProyectoService proyectoService;

    private Proyecto p1;
    private ProyectoDTO proyectoDTO;
    private ProyectoDTO dto1;
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
        this.repositorio = new Repositorio("Ciencia", date, p1);
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.login = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);
        this.accessHistory = new AccessHistory(new ObjectId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        this.p1 = new Proyecto(programador, "Ciencia", 20000.00, date,
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

        dto1 = ProyectoDTO.builder()
                .jefeProyecto(null)
                .nombre(null)
                .presupuesto(null)
                .fechaInicio(null)
                .fechaFin(null)
                .usedTechnologies(null)
                .repositorio(null)
                .esAcabado(false)
                .departamento(null)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        this.proyectoRepository = mock(ProyectoRepository.class);
        this.proyectoService = new ProyectoService(proyectoRepository);
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
        List<Proyecto> lista = new ArrayList<>();
        lista.add(p1);
        Mockito.when(this.proyectoRepository.findAll()).thenReturn(Optional.of(lista));
        Optional<List<Proyecto>> proyectos = proyectoService.findAll();
        assumeTrue(proyectos.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, proyectos.get().size()),
                () -> Assertions.assertEquals(p1.getId(), proyectos.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), proyectos.get().get(0).getJefeProyecto().getId()),
                () -> Assertions.assertEquals(p1.getNombre(), proyectos.get().get(0).getNombre()),
                () -> Assertions.assertEquals(p1.getPresupuesto(), proyectos.get().get(0).getPresupuesto()),
                () -> Assertions.assertEquals(p1.getFechaInicio(), proyectos.get().get(0).getFechaInicio()),
                () -> Assertions.assertEquals(p1.getFechaFin(), proyectos.get().get(0).getFechaFin()),
                () -> Assertions.assertEquals(p1.getUsedTechnologies(), proyectos.get().get(0).getUsedTechnologies()),
                () -> Assertions.assertEquals(repositorio.getId(), proyectos.get().get(0).getRepositorio().getId()),
                () -> Assertions.assertEquals(p1.isEsAcabado(), proyectos.get().get(0).isEsAcabado()),
                () -> Assertions.assertEquals(departamento.getId(), proyectos.get().get(0).getDepartamento().getId())
        );
        Mockito.verify(proyectoRepository, Mockito.atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Service FindById")
    @Order(3)
    void findById() throws SQLException {
        Mockito.when(this.proyectoRepository.getById(p1.getId())).thenReturn(Optional.ofNullable(p1));
        Optional<Proyecto> proyecto = this.proyectoService.getById(p1.getId());
        assumeTrue(proyecto.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(p1.getId(), proyecto.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), proyecto.get().getJefeProyecto().getId()),
                () -> Assertions.assertEquals(p1.getNombre(), proyecto.get().getNombre()),
                () -> Assertions.assertEquals(p1.getPresupuesto(), proyecto.get().getPresupuesto()),
                () -> Assertions.assertEquals(p1.getFechaInicio(), proyecto.get().getFechaInicio()),
                () -> Assertions.assertEquals(p1.getFechaFin(), proyecto.get().getFechaFin()),
                () -> Assertions.assertEquals(p1.getUsedTechnologies(), proyecto.get().getUsedTechnologies()),
                () -> Assertions.assertEquals(repositorio.getId(), proyecto.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(p1.isEsAcabado(), proyecto.get().isEsAcabado()),
                () -> Assertions.assertEquals(departamento.getId(), proyecto.get().getDepartamento().getId())
        );
        Mockito.verify(proyectoRepository, Mockito.atLeastOnce()).getById(p1.getId());
    }

    @Test
    @DisplayName("Service Insert")
    @Order(4)
    void insertTest() throws SQLException {
        Mockito.when(proyectoRepository.insert(p1)).thenReturn(Optional.ofNullable(p1));
        Optional<Proyecto> proyecto = this.proyectoService.insert(p1);
        assumeTrue(proyecto.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(p1.getId(), proyecto.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), proyecto.get().getJefeProyecto().getId()),
                () -> Assertions.assertEquals(p1.getNombre(), proyecto.get().getNombre()),
                () -> Assertions.assertEquals(p1.getPresupuesto(), proyecto.get().getPresupuesto()),
                () -> Assertions.assertEquals(p1.getFechaInicio(), proyecto.get().getFechaInicio()),
                () -> Assertions.assertEquals(p1.getFechaFin(), proyecto.get().getFechaFin()),
                () -> Assertions.assertEquals(p1.getUsedTechnologies(), proyecto.get().getUsedTechnologies()),
                () -> Assertions.assertEquals(repositorio.getId(), proyecto.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(p1.isEsAcabado(), proyecto.get().isEsAcabado()),
                () -> Assertions.assertEquals(departamento.getId(), proyecto.get().getDepartamento().getId())
        );
        Mockito.verify(proyectoRepository, Mockito.atLeastOnce()).insert(p1);
    }

    @Test
    @DisplayName("Service Update")
    @Order(5)
    void updateTest() throws SQLException {
        Mockito.when(proyectoRepository.update(p1)).thenReturn(Optional.ofNullable(p1));
        Optional<Proyecto> proyecto = this.proyectoService.update(p1);
        assumeTrue(proyecto.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(p1.getId(), proyecto.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), proyecto.get().getJefeProyecto().getId()),
                () -> Assertions.assertEquals(p1.getNombre(), proyecto.get().getNombre()),
                () -> Assertions.assertEquals(p1.getPresupuesto(), proyecto.get().getPresupuesto()),
                () -> Assertions.assertEquals(p1.getFechaInicio(), proyecto.get().getFechaInicio()),
                () -> Assertions.assertEquals(p1.getFechaFin(), proyecto.get().getFechaFin()),
                () -> Assertions.assertEquals(p1.getUsedTechnologies(), proyecto.get().getUsedTechnologies()),
                () -> Assertions.assertEquals(repositorio.getId(), proyecto.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(p1.isEsAcabado(), proyecto.get().isEsAcabado()),
                () -> Assertions.assertEquals(departamento.getId(), proyecto.get().getDepartamento().getId())
        );
        Mockito.verify(proyectoRepository, Mockito.atLeastOnce()).update(p1);
    }

    @Test
    @DisplayName("Service delete")
    @Order(6)
    void deleteTest() throws SQLException {
        Mockito.when(proyectoRepository.delete(p1.getId())).thenReturn(Optional.ofNullable(p1));
        Optional<Proyecto> proyecto = this.proyectoService.delete(p1.getId());
        assumeTrue(proyecto.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(p1.getId(), proyecto.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), proyecto.get().getJefeProyecto().getId()),
                () -> Assertions.assertEquals(p1.getNombre(), proyecto.get().getNombre()),
                () -> Assertions.assertEquals(p1.getPresupuesto(), proyecto.get().getPresupuesto()),
                () -> Assertions.assertEquals(p1.getFechaInicio(), proyecto.get().getFechaInicio()),
                () -> Assertions.assertEquals(p1.getFechaFin(), proyecto.get().getFechaFin()),
                () -> Assertions.assertEquals(p1.getUsedTechnologies(), proyecto.get().getUsedTechnologies()),
                () -> Assertions.assertEquals(repositorio.getId(), proyecto.get().getRepositorio().getId()),
                () -> Assertions.assertEquals(p1.isEsAcabado(), proyecto.get().isEsAcabado()),
                () -> Assertions.assertEquals(departamento.getId(), proyecto.get().getDepartamento().getId())
        );
        Mockito.verify(proyectoRepository, Mockito.atLeastOnce()).delete(p1.getId());
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(8)
    void getByIdException() {
        ProyectoRepository proyectoRepository = new ProyectoRepository();
        ProyectoService proyectoService = new ProyectoService(proyectoRepository);
        Exception exception = assertThrows(SQLException.class,() -> {
            proyectoService.getByIdDTO(dto1.getId());
        });

        String expectedMessage = "Error ProyectoRepository no se ha encontrado la ID " + dto1.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}