package controller;

import dao.Departamento;
import dao.AccessHistory;
import dao.Login;
import dao.Programador;
import database.MongoDBController;
import dto.ProgramadorDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import service.ProgramadorService;
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

@DisplayName("JUnit 5 y Mockito Test Controller Programador")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProgramadorControllerTest {

    private ProgramadorService programadorService;
    private ProgramadorController programadorController;

    private Programador programador;
    private ProgramadorDTO programadorDTO;
    private Departamento departamento;
    private Login login;
    private AccessHistory accessHistory;

    private final void initData() {
        Date date = new Date();
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");


        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.login = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);
        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.accessHistory = new AccessHistory(new ObjectId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        programadorDTO = ProgramadorDTO.builder()
                .id(new ObjectId())
                .nombre("Jose")
                .fechaAlta(date)
                .departamento(departamento)
                .domainTechnologies(technologies)
                .salario(1000.00)
                .esJefeDepartamento(false)
                .esJefeProyecto(false)
                .esJefeActivo(false)
                .correo("jose@jose.com")
                .password("12345")
                .login(login)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        programadorService = mock(ProgramadorService.class);
        programadorController = new ProgramadorController(programadorService);
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
        List<ProgramadorDTO> lista = new ArrayList<>();
        lista.add(programadorDTO);
        Mockito.when(this.programadorService.getAllDTO()).thenReturn(Optional.of(lista));
        Optional<List<ProgramadorDTO>> programadores = programadorController.getAll();
        assumeTrue(programadores.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, programadores.get().size()),
                () -> Assertions.assertEquals(programadorDTO.getNombre(), programadores.get().get(0).getNombre()),
                () -> Assertions.assertEquals(programadorDTO.getFechaAlta(), programadores.get().get(0).getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programadores.get().get(0).getDepartamento().getId()),
                () -> Assertions.assertEquals(programadorDTO.getDomainTechnologies(), programadores.get().get(0).getDomainTechnologies()),
                () -> Assertions.assertEquals(programadorDTO.getSalario(), programadores.get().get(0).getSalario()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeDepartamento(), programadores.get().get(0).isEsJefeDepartamento()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeProyecto(), programadores.get().get(0).isEsJefeProyecto()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeActivo(), programadores.get().get(0).isEsJefeActivo()),
                () -> Assertions.assertEquals(programadorDTO.getCorreo(), programadores.get().get(0).getCorreo()),
                () -> Assertions.assertEquals(programadorDTO.getPassword(), programadores.get().get(0).getPassword()),
                () -> Assertions.assertEquals(login.getId(), programadores.get().get(0).getLogin().getId())
        );
        Mockito.verify(programadorService, Mockito.atLeastOnce()).getAllDTO();
    }

    @Test
    @DisplayName("Controller getById")
    @Order(3)
    void getById() throws SQLException {
        Mockito.when(this.programadorService.getByIdDTO(programadorDTO.getId())).thenReturn(Optional.ofNullable(programadorDTO));
        Optional<ProgramadorDTO> programador = this.programadorController.getById(programadorDTO.getId());
        assumeTrue(programador.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(programadorDTO.getNombre(), programador.get().getNombre()),
                () -> Assertions.assertEquals(programadorDTO.getFechaAlta(), programador.get().getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programador.get().getDepartamento().getId()),
                () -> Assertions.assertEquals(programadorDTO.getDomainTechnologies(), programador.get().getDomainTechnologies()),
                () -> Assertions.assertEquals(programadorDTO.getSalario(), programador.get().getSalario()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeDepartamento(), programador.get().isEsJefeDepartamento()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeProyecto(), programador.get().isEsJefeProyecto()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeActivo(), programador.get().isEsJefeActivo()),
                () -> Assertions.assertEquals(programadorDTO.getCorreo(), programador.get().getCorreo()),
                () -> Assertions.assertEquals(programadorDTO.getPassword(), programador.get().getPassword()),
                () -> Assertions.assertEquals(login.getId(), programador.get().getLogin().getId())
        );
        Mockito.verify(programadorService, Mockito.atLeastOnce()).getByIdDTO(programadorDTO.getId());
    }

    @Test
    @DisplayName("Controller Insert")
    @Order(4)
    void insert() throws SQLException {
        Mockito.when(programadorService.insertDTO(programadorDTO)).thenReturn(Optional.ofNullable(programadorDTO));
        Optional<ProgramadorDTO> programador = this.programadorController.insert(programadorDTO);
        assumeTrue(programador.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(programadorDTO.getNombre(), programador.get().getNombre()),
                () -> Assertions.assertEquals(programadorDTO.getFechaAlta(), programador.get().getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programador.get().getDepartamento().getId()),
                () -> Assertions.assertEquals(programadorDTO.getDomainTechnologies(), programador.get().getDomainTechnologies()),
                () -> Assertions.assertEquals(programadorDTO.getSalario(), programador.get().getSalario()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeDepartamento(), programador.get().isEsJefeDepartamento()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeProyecto(), programador.get().isEsJefeProyecto()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeActivo(), programador.get().isEsJefeActivo()),
                () -> Assertions.assertEquals(programadorDTO.getCorreo(), programador.get().getCorreo()),
                () -> Assertions.assertEquals(programadorDTO.getPassword(), programador.get().getPassword()),
                () -> Assertions.assertEquals(login.getId(), programador.get().getLogin().getId())
        );
        Mockito.verify(programadorService, Mockito.atLeastOnce()).insertDTO(programadorDTO);
    }

    @Test
    @DisplayName("Controller Update")
    @Order(5)
    void update() throws SQLException {
        Mockito.when(programadorService.updateDTO(programadorDTO)).thenReturn(Optional.ofNullable(programadorDTO));
        Optional<ProgramadorDTO> programador = this.programadorController.update(programadorDTO);
        assumeTrue(programador.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(programadorDTO.getNombre(), programador.get().getNombre()),
                () -> Assertions.assertEquals(programadorDTO.getFechaAlta(), programador.get().getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programador.get().getDepartamento().getId()),
                () -> Assertions.assertEquals(programadorDTO.getDomainTechnologies(), programador.get().getDomainTechnologies()),
                () -> Assertions.assertEquals(programadorDTO.getSalario(), programador.get().getSalario()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeDepartamento(), programador.get().isEsJefeDepartamento()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeProyecto(), programador.get().isEsJefeProyecto()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeActivo(), programador.get().isEsJefeActivo()),
                () -> Assertions.assertEquals(programadorDTO.getCorreo(), programador.get().getCorreo()),
                () -> Assertions.assertEquals(programadorDTO.getPassword(), programador.get().getPassword()),
                () -> Assertions.assertEquals(login.getId(), programador.get().getLogin().getId())
        );
        Mockito.verify(programadorService, Mockito.atLeastOnce()).updateDTO(programadorDTO);
    }

    @Test
    @DisplayName("Controller delete")
    @Order(6)
    void delete() throws SQLException {
        Mockito.when(programadorService.deleteDTO(programadorDTO.getId())).thenReturn(Optional.ofNullable(programadorDTO));
        Optional<ProgramadorDTO> programador = this.programadorController.delete(programadorDTO.getId());
        assumeTrue(programador.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(programadorDTO.getNombre(), programador.get().getNombre()),
                () -> Assertions.assertEquals(programadorDTO.getFechaAlta(), programador.get().getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programador.get().getDepartamento().getId()),
                () -> Assertions.assertEquals(programadorDTO.getDomainTechnologies(), programador.get().getDomainTechnologies()),
                () -> Assertions.assertEquals(programadorDTO.getSalario(), programador.get().getSalario()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeDepartamento(), programador.get().isEsJefeDepartamento()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeProyecto(), programador.get().isEsJefeProyecto()),
                () -> Assertions.assertEquals(programadorDTO.isEsJefeActivo(), programador.get().isEsJefeActivo()),
                () -> Assertions.assertEquals(programadorDTO.getCorreo(), programador.get().getCorreo()),
                () -> Assertions.assertEquals(programadorDTO.getPassword(), programador.get().getPassword()),
                () -> Assertions.assertEquals(login.getId(), programador.get().getLogin().getId())
        );
        Mockito.verify(programadorService, Mockito.atLeastOnce()).deleteDTO(programadorDTO.getId());
    }
}