package controller;

import dao.Departamento;
import dao.AccessHistory;
import dao.Login;
import dao.Programador;
import database.MongoDBController;
import dto.DepartamentoDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import service.DepartamentoService;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;

@DisplayName("JUnit 5 y Mockito Test Controller Departamento")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartamentoControllerTest {

    private DepartamentoController departamentoController;
    private DepartamentoService departamentoService;
    private Departamento d1;
    private Programador programador;
    private Login login;
    private AccessHistory accessHistory;
    private DepartamentoDTO departamentoDTO;

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
        departamentoDTO = DepartamentoDTO.builder()
                .id(new ObjectId())
                .nombre("Departamento tecnológico")
                .jefeDepartamento(programador)
                .presupuesto(200000.00)
                .presupuestoAnual(1000000.00)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        this.departamentoService = mock(DepartamentoService.class);
        this.departamentoController = new DepartamentoController(departamentoService);
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
        List<DepartamentoDTO> lista = new ArrayList<>();
        lista.add(departamentoDTO);
        Mockito.when(this.departamentoService.getAllDTO()).thenReturn(Optional.of(lista));
        Optional<List<DepartamentoDTO>> departamentos = departamentoController.getAll();
        assumeTrue(departamentos.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, departamentos.get().size()),
                () -> Assertions.assertEquals(departamentoDTO.getId(), departamentos.get().get(0).getId()),
                () -> Assertions.assertEquals(departamentoDTO.getNombre(), departamentos.get().get(0).getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamentos.get().get(0).getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(departamentoDTO.getPresupuesto(), departamentos.get().get(0).getPresupuesto()),
                () -> Assertions.assertEquals(departamentoDTO.getPresupuestoAnual(), departamentos.get().get(0).getPresupuestoAnual())
        );
        Mockito.verify(departamentoService, Mockito.atLeastOnce()).getAllDTO();
    }

    @Test
    @DisplayName("Controller getById")
    @Order(3)
    void getById() throws SQLException {
        Mockito.when(this.departamentoService.getByIdDTO(departamentoDTO.getId())).thenReturn(Optional.ofNullable(departamentoDTO));
        Optional<DepartamentoDTO> departamento = this.departamentoController.getById(departamentoDTO.getId());
        assumeTrue(departamento.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(departamentoDTO.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(departamentoDTO.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamento.get().getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(departamentoDTO.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(departamentoDTO.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
        Mockito.verify(departamentoService, Mockito.atLeastOnce()).getByIdDTO(departamentoDTO.getId());
    }

    @Test
    @DisplayName("Controller Insert")
    @Order(4)
    void insert() throws SQLException {
        Mockito.when(departamentoService.insertDTO(departamentoDTO)).thenReturn(Optional.ofNullable(departamentoDTO));
        Optional<DepartamentoDTO> departamento = this.departamentoController.insert(departamentoDTO);
        assumeTrue(departamento.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(departamentoDTO.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(departamentoDTO.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamento.get().getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(departamentoDTO.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(departamentoDTO.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
        Mockito.verify(departamentoService, Mockito.atLeastOnce()).insertDTO(departamentoDTO);
    }

    @Test
    @DisplayName("Controller Update")
    @Order(5)
    void update() throws SQLException {
        Mockito.when(departamentoService.updateDTO(departamentoDTO)).thenReturn(Optional.ofNullable(departamentoDTO));
        Optional<DepartamentoDTO> departamento = this.departamentoController.update(departamentoDTO);
        assumeTrue(departamento.isPresent());
        Assertions.assertAll("Comprobaciones test update",
                () -> Assertions.assertEquals(departamentoDTO.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(departamentoDTO.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamento.get().getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(departamentoDTO.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(departamentoDTO.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
        Mockito.verify(departamentoService, Mockito.atLeastOnce()).updateDTO(departamentoDTO);
    }

    @Test
    @DisplayName("Controller delete")
    @Order(6)
    void delete() throws SQLException {
        Mockito.when(departamentoService.deleteDTO(departamentoDTO.getId())).thenReturn(Optional.ofNullable(departamentoDTO));
        Optional<DepartamentoDTO> departamento = this.departamentoController.delete(departamentoDTO.getId());
        assumeTrue(departamento.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(departamentoDTO.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(departamentoDTO.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamento.get().getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(departamentoDTO.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(departamentoDTO.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
        Mockito.verify(departamentoService, Mockito.atLeastOnce()).deleteDTO(departamentoDTO.getId());
    }

}