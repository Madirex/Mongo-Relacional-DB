package service;

import dao.*;
import database.MongoDBController;
import dto.DepartamentoDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import repository.DepartamentoRepository;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JUnit 5 Test Service Departamento")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartamentoServiceTest {
    private DepartamentoRepository departamentoRepository;
    private DepartamentoService departamentoService;

    private DepartamentoDTO dto;
    private DepartamentoDTO dto1;
    private DepartamentoDTO dto2;
    private DepartamentoDTO dto3;
    private Departamento d1;
    private Programador programador;
    private Programador programador2;
    private Login login;
    private AccessHistory accessHistory;

    private final void initData() {
        Date date = new Date();
        //Date date1 = Date.valueOf("2022-01-21");
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.programador = new Programador("Jose", date, d1, technologies,
                1000.00, true, false, false, "jose@jose.com", "12345", login);
        this.programador2 = new Programador("Jose", date, d1, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.login = new Login(programador, LocalDateTime.now(), "Token", accessHistory, false);
        this.accessHistory = new AccessHistory(programador.getId(), LocalDateTime.now());

        this.d1 = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        dto = DepartamentoDTO.builder()
                .id(new ObjectId())
                .nombre("Departamento tecnológico")
                .jefeDepartamento(programador)
                .presupuesto(200000.00)
                .presupuestoAnual(1000000.00)
                .build();
        dto1 = DepartamentoDTO.builder()
                .nombre(null)
                .jefeDepartamento(null)
                .presupuesto(null)
                .presupuestoAnual(null)
                .build();

        dto2 = DepartamentoDTO.builder()
                .nombre("Departamento tecno")
                .jefeDepartamento(null)
                .presupuesto(2000.00)
                .presupuestoAnual(20000.00)
                .build();

        dto3 = DepartamentoDTO.builder()
                .nombre("Departamento tecno")
                .jefeDepartamento(programador2)
                .presupuesto(2000.00)
                .presupuestoAnual(20000.00)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        this.departamentoRepository = mock(DepartamentoRepository.class);
        this.departamentoService = new DepartamentoService(departamentoRepository);
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
        List<Departamento> lista = new ArrayList<>();
        lista.add(d1);
        Mockito.when(this.departamentoRepository.findAll()).thenReturn(Optional.of(lista));
        Optional<List<Departamento>> departamentos = departamentoService.findAll();
        assumeTrue(departamentos.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, departamentos.get().size()),
                () -> Assertions.assertEquals(d1.getId(), departamentos.get().get(0).getId()),
                () -> Assertions.assertEquals(d1.getNombre(), departamentos.get().get(0).getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamentos.get().get(0).getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(d1.getPresupuesto(), departamentos.get().get(0).getPresupuesto()),
                () -> Assertions.assertEquals(d1.getPresupuestoAnual(), departamentos.get().get(0).getPresupuestoAnual())
        );
        Mockito.verify(departamentoRepository, Mockito.atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Service FindById")
    @Order(3)
    void findById() throws SQLException {
        Mockito.when(this.departamentoRepository.getById(d1.getId())).thenReturn(Optional.ofNullable(d1));
        Optional<Departamento> departamento = this.departamentoService.getById(d1.getId());
        assumeTrue(departamento.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(d1.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(d1.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamento.get().getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(d1.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(d1.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
        Mockito.verify(departamentoRepository, Mockito.atLeastOnce()).getById(d1.getId());
    }

    @Test
    @DisplayName("Service Insert")
    @Order(4)
    void insertTest() throws SQLException {
        Mockito.when(departamentoRepository.insert(d1)).thenReturn(Optional.ofNullable(d1));
        Optional<Departamento> departamento = this.departamentoService.insert(d1);
        assumeTrue(departamento.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(d1.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(d1.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamento.get().getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(d1.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(d1.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
        Mockito.verify(departamentoRepository, Mockito.atLeastOnce()).insert(d1);
    }

    @Test
    @DisplayName("Service Update")
    @Order(5)
    void updateTest() throws SQLException {
        Mockito.when(departamentoRepository.update(d1)).thenReturn(Optional.ofNullable(d1));
        Optional<Departamento> departamento = this.departamentoService.update(d1);
        assumeTrue(departamento.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(d1.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(d1.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamento.get().getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(d1.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(d1.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
        Mockito.verify(departamentoRepository, Mockito.atLeastOnce()).update(d1);
    }

    @Test
    @DisplayName("Service delete")
    @Order(6)
    void deleteTest() throws SQLException {
        Mockito.when(departamentoRepository.delete(d1.getId())).thenReturn(Optional.ofNullable(d1));
        Optional<Departamento> departamento = this.departamentoService.delete(d1.getId());
        assumeTrue(departamento.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(d1.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(d1.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(programador.getId(), departamento.get().getJefeDepartamento().getId()),
                () -> Assertions.assertEquals(d1.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(d1.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
        Mockito.verify(departamentoRepository, Mockito.atLeastOnce()).delete(d1.getId());
    }

    @Test
    @DisplayName("Service InsertDTO")
    @Order(7)
    void insertDTOTest() throws SQLException {
        DepartamentoRepository departamentoRepository = new DepartamentoRepository();
        DepartamentoService departamentoService = new DepartamentoService(departamentoRepository);
        Optional<DepartamentoDTO> departamento = departamentoService.insertDTO(dto);
        assumeTrue(departamento.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(dto.getId(), departamento.get().getId()),
                () -> Assertions.assertEquals(dto.getNombre(), departamento.get().getNombre()),
                () -> Assertions.assertEquals(dto.getJefeDepartamento(), departamento.get().getJefeDepartamento()),
                () -> Assertions.assertEquals(dto.getPresupuesto(), departamento.get().getPresupuesto()),
                () -> Assertions.assertEquals(dto.getPresupuestoAnual(), departamento.get().getPresupuestoAnual())
        );
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(8)
    void getByIdException() {
        DepartamentoRepository departamentoRepository = new DepartamentoRepository();
        DepartamentoService departamentoService = new DepartamentoService(departamentoRepository);
        Exception exception = assertThrows(SQLException.class,() -> {
            departamentoService.getByIdDTO(dto1.getId());
        });

        String expectedMessage = "Error DepartamentoRepository no se ha encontrado la ID " + dto1.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}