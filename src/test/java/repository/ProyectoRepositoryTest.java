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

@DisplayName("JUnit 5 Test CRUD Proyecto")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProyectoRepositoryTest {

    private ProyectoRepository proyectoRepository = new ProyectoRepository();

    private Proyecto p1;
    private Proyecto p2;
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
        this.p2 = new Proyecto(null, null, null, null,
                null, null, null, false, null);
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
    @DisplayName("Respository Insert Proyecto")
    @Order(2)
    void insertTest() throws SQLException {
        Optional<Proyecto> proyecto = proyectoRepository.insert(p1);
        assumeTrue(proyecto.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(p1.getId(), proyecto.get().getId()),
                () -> Assertions.assertEquals(p1.getJefeProyecto(), proyecto.get().getJefeProyecto()),
                () -> Assertions.assertEquals(p1.getNombre(), proyecto.get().getNombre()),
                () -> Assertions.assertEquals(p1.getPresupuesto(), proyecto.get().getPresupuesto()),
                () -> Assertions.assertEquals(p1.getFechaInicio(), proyecto.get().getFechaInicio()),
                () -> Assertions.assertEquals(p1.getFechaFin(), proyecto.get().getFechaFin()),
                () -> Assertions.assertEquals(p1.getUsedTechnologies(), proyecto.get().getUsedTechnologies()),
                () -> Assertions.assertEquals(p1.getRepositorio(), proyecto.get().getRepositorio()),
                () -> Assertions.assertEquals(p1.isEsAcabado(), proyecto.get().isEsAcabado()),
                () -> Assertions.assertEquals(p1.getDepartamento(), proyecto.get().getDepartamento())
        );
    }

    @Test
    @DisplayName("Get all Proyecto")
    @Order(3)
    void getAllTest() throws SQLException {
        proyectoRepository.insert(p1);
        Optional<List<Proyecto>> proyectos = proyectoRepository.findAll();
        assumeTrue(proyectos.isPresent());
        Assertions.assertAll( "Comprobaciones test getAll",
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
    }

    @Test
    @DisplayName("Find Proyecto By ID")
    @Order(4)
    void getById() throws SQLException {
        proyectoRepository.insert(p1);
        Optional<Proyecto> proyecto = proyectoRepository.getById(p1.getId());
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
    }

    @Test
    @DisplayName("Update Proyecto")
    @Order(5)
    void update() throws SQLException {
        proyectoRepository.insert(p1);
        p1.setNombre("Nuevo nombre");
        Optional<Proyecto> proyecto = proyectoRepository.update(p1);
        assumeTrue(proyecto.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
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
    }

    @Test
    @DisplayName("Delete Proyecto")
    @Order(6)
    void delete() throws SQLException {
        proyectoRepository.insert(p1);
        Optional<Proyecto> proyecto = proyectoRepository.delete(p1.getId());
        assumeTrue(proyecto.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
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
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(7)
    void getByIdException() {
        Exception exception = assertThrows(SQLException.class,() -> {
            proyectoRepository.getById(p2.getId());
        });

        String expectedMessage = "Error ProyectoRepository no se ha encontrado la ID " + p2.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}