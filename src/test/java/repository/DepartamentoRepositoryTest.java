package repository;

import dao.*;
import database.MongoDBController;
import org.junit.jupiter.api.*;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("JUnit 5 Test CRUD Departamento")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartamentoRepositoryTest {

    private DepartamentoRepository departamentoRepository = new DepartamentoRepository();

    private Departamento d1;
    private Departamento d2;
    private Programador programador;
    private Login login;
    private AccessHistory accessHistory;

    private final void initData() {
        Date date = new Date();
        //Date date1 = Date.valueOf("2022-01-21");
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.programador = new Programador("Jose", date, d1, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.login = new Login(programador, LocalDateTime.now(), "Token", accessHistory, false);
        this.accessHistory = new AccessHistory(programador.getId(), LocalDateTime.now());

        this.d1 = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.d2 = new Departamento((String) null, null, null, null);
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
        Optional<Departamento> departamento = departamentoRepository.insert(d1);
        assumeTrue(departamento.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(d1.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(d1.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(d1.getJefeDepartamento(), departamento.get().getJefeDepartamento()),
                () -> Assertions.assertEquals(d1.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(d1.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
    }

    @Test
    @DisplayName("Get all Departamento")
    @Order(3)
    void getAllTest() throws SQLException {
        departamentoRepository.insert(d1);
        Optional<List<Departamento>> departamentos = departamentoRepository.findAll();
        assumeTrue(departamentos.isPresent());
        Assertions.assertAll("Comprobaciones test getAll",
                () -> Assertions.assertEquals(1, departamentos.get().size()),
                () -> Assertions.assertEquals(d1.getId(), departamentos.get().get(0).getId()),
                () -> Assertions.assertEquals(d1.getNombre(), departamentos.get().get(0).getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamentos.get().get(0).getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(d1.getPresupuesto(), departamentos.get().get(0).getPresupuesto()),
                () -> Assertions.assertEquals(d1.getPresupuestoAnual(), departamentos.get().get(0).getPresupuestoAnual())
        );
    }

    @Test
    @DisplayName("Find Departamento By ID")
    @Order(4)
    void getById() throws SQLException {
        departamentoRepository.insert(d1);
        Optional<Departamento> departamento = departamentoRepository.getById(d1.getId());
        assumeTrue(departamento.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(d1.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(d1.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamento.get().getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(d1.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(d1.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
    }

    @Test
    @DisplayName("Update Departamento")
    @Order(5)
    void update() throws SQLException {
        departamentoRepository.insert(d1);
        d1.setNombre("Nombre modificado");
        d1.setPresupuesto(20.00);
        Optional<Departamento> departamento = departamentoRepository.update(d1);
        assumeTrue(departamento.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(d1.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(d1.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamento.get().getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(d1.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(d1.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
    }

    @Test
    @DisplayName("Delete Departamento")
    @Order(6)
    void delete() throws SQLException {
        departamentoRepository.insert(d1);
        Optional<Departamento> departamento = departamentoRepository.delete(d1.getId());
        assumeTrue(departamento.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(d1.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(d1.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamento.get().getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(d1.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(d1.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(7)
    void getByIdException() {
        Exception exception = assertThrows(SQLException.class,() -> {
            departamentoRepository.getById(d2.getId());
        });

        String expectedMessage = "Error DepartamentoRepository no se ha encontrado la ID " + d2.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}