package service;

import dao.Departamento;
import dao.AccessHistory;
import dao.Login;
import dao.Programador;
import database.MongoDBController;
import dto.ProgramadorDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import repository.ProgramadorRepository;
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

@DisplayName("JUnit 5 Test Service Programador")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProgramadorServiceTest {

    private ProgramadorRepository programadorRepository;
    private ProgramadorService programadorService;

    private Programador p1;
    private ProgramadorDTO programadorDTO;
    private ProgramadorDTO dto1;
    private Departamento departamento;
    private Login login;
    private AccessHistory accessHistory;

    private final void initData() {
        Date date = new Date();
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.departamento = new Departamento("Departamento tecnológico", p1, 2000000.00, 1000000000.00);
        this.login = new Login(p1, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);
        this.p1 = new Programador("Jose", date, departamento, technologies,
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

        dto1 = ProgramadorDTO.builder()
                .nombre(null)
                .fechaAlta(null)
                .departamento(null)
                .domainTechnologies(null)
                .salario(null)
                .esJefeDepartamento(false)
                .esJefeProyecto(false)
                .esJefeActivo(false)
                .correo(null)
                .password(null)
                .login(null)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        programadorRepository = mock(ProgramadorRepository.class);
        programadorService = new ProgramadorService(programadorRepository);
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
        List<Programador> lista = new ArrayList<>();
        lista.add(p1);
        Mockito.when(this.programadorRepository.findAll()).thenReturn(Optional.of(lista));
        Optional<List<Programador>> programadores = programadorService.findAll();
        assumeTrue(programadores.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, programadores.get().size()),
                () -> Assertions.assertEquals(p1.getNombre(), programadores.get().get(0).getNombre()),
                () -> Assertions.assertEquals(p1.getFechaAlta(), programadores.get().get(0).getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programadores.get().get(0).getDepartamento().getId()),
                () -> Assertions.assertEquals(p1.getDominatedTechnologies(), programadores.get().get(0).getDominatedTechnologies()),
                () -> Assertions.assertEquals(p1.getSalario(), programadores.get().get(0).getSalario()),
                () -> Assertions.assertEquals(p1.isEsJefeDepartamento(), programadores.get().get(0).isEsJefeDepartamento()),
                () -> Assertions.assertEquals(p1.isEsJefeProyecto(), programadores.get().get(0).isEsJefeProyecto()),
                () -> Assertions.assertEquals(p1.isEsJefeActivo(), programadores.get().get(0).isEsJefeActivo()),
                () -> Assertions.assertEquals(p1.getCorreo(), programadores.get().get(0).getCorreo()),
                () -> Assertions.assertEquals(p1.getPassword(), programadores.get().get(0).getPassword()),
                () -> Assertions.assertEquals(login.getId(), programadores.get().get(0).getLogin().getId())
        );
        Mockito.verify(programadorRepository, Mockito.atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Service FindById")
    @Order(3)
    void findById() throws SQLException {
        Mockito.when(this.programadorRepository.getById(p1.getId())).thenReturn(Optional.ofNullable(p1));
        Optional<Programador> programador = this.programadorService.getById(p1.getId());
        assumeTrue(programador.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(p1.getNombre(), programador.get().getNombre()),
                () -> Assertions.assertEquals(p1.getFechaAlta(), programador.get().getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programador.get().getDepartamento().getId()),
                () -> Assertions.assertEquals(p1.getDominatedTechnologies(), programador.get().getDominatedTechnologies()),
                () -> Assertions.assertEquals(p1.getSalario(), programador.get().getSalario()),
                () -> Assertions.assertEquals(p1.isEsJefeDepartamento(), programador.get().isEsJefeDepartamento()),
                () -> Assertions.assertEquals(p1.isEsJefeProyecto(), programador.get().isEsJefeProyecto()),
                () -> Assertions.assertEquals(p1.isEsJefeActivo(), programador.get().isEsJefeActivo()),
                () -> Assertions.assertEquals(p1.getCorreo(), programador.get().getCorreo()),
                () -> Assertions.assertEquals(p1.getPassword(), programador.get().getPassword()),
                () -> Assertions.assertEquals(login.getId(), programador.get().getLogin().getId())
        );
        Mockito.verify(programadorRepository, Mockito.atLeastOnce()).getById(p1.getId());
    }

    @Test
    @DisplayName("Service Insert")
    @Order(4)
    void insertTest() throws SQLException {
        Mockito.when(programadorRepository.insert(p1)).thenReturn(Optional.ofNullable(p1));
        Optional<Programador> programador = this.programadorService.insert(p1);
        assumeTrue(programador.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(p1.getNombre(), programador.get().getNombre()),
                () -> Assertions.assertEquals(p1.getFechaAlta(), programador.get().getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programador.get().getDepartamento().getId()),
                () -> Assertions.assertEquals(p1.getDominatedTechnologies(), programador.get().getDominatedTechnologies()),
                () -> Assertions.assertEquals(p1.getSalario(), programador.get().getSalario()),
                () -> Assertions.assertEquals(p1.isEsJefeDepartamento(), programador.get().isEsJefeDepartamento()),
                () -> Assertions.assertEquals(p1.isEsJefeProyecto(), programador.get().isEsJefeProyecto()),
                () -> Assertions.assertEquals(p1.isEsJefeActivo(), programador.get().isEsJefeActivo()),
                () -> Assertions.assertEquals(p1.getCorreo(), programador.get().getCorreo()),
                () -> Assertions.assertEquals(p1.getPassword(), programador.get().getPassword()),
                () -> Assertions.assertEquals(login.getId(), programador.get().getLogin().getId())
        );
        Mockito.verify(programadorRepository, Mockito.atLeastOnce()).insert(p1);
    }

    @Test
    @DisplayName("Service Update")
    @Order(5)
    void updateTest() throws SQLException {
        Mockito.when(programadorRepository.update(p1)).thenReturn(Optional.ofNullable(p1));
        Optional<Programador> programador = this.programadorService.update(p1);
        assumeTrue(programador.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(p1.getNombre(), programador.get().getNombre()),
                () -> Assertions.assertEquals(p1.getFechaAlta(), programador.get().getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programador.get().getDepartamento().getId()),
                () -> Assertions.assertEquals(p1.getDominatedTechnologies(), programador.get().getDominatedTechnologies()),
                () -> Assertions.assertEquals(p1.getSalario(), programador.get().getSalario()),
                () -> Assertions.assertEquals(p1.isEsJefeDepartamento(), programador.get().isEsJefeDepartamento()),
                () -> Assertions.assertEquals(p1.isEsJefeProyecto(), programador.get().isEsJefeProyecto()),
                () -> Assertions.assertEquals(p1.isEsJefeActivo(), programador.get().isEsJefeActivo()),
                () -> Assertions.assertEquals(p1.getCorreo(), programador.get().getCorreo()),
                () -> Assertions.assertEquals(p1.getPassword(), programador.get().getPassword()),
                () -> Assertions.assertEquals(login.getId(), programador.get().getLogin().getId())
        );
        Mockito.verify(programadorRepository, Mockito.atLeastOnce()).update(p1);
    }

    @Test
    @DisplayName("Service delete")
    @Order(6)
    void deleteTest() throws SQLException {
        Mockito.when(programadorRepository.delete(p1.getId())).thenReturn(Optional.ofNullable(p1));
        Optional<Programador> programador = this.programadorService.delete(p1.getId());
        assumeTrue(programador.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(p1.getNombre(), programador.get().getNombre()),
                () -> Assertions.assertEquals(p1.getFechaAlta(), programador.get().getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programador.get().getDepartamento().getId()),
                () -> Assertions.assertEquals(p1.getDominatedTechnologies(), programador.get().getDominatedTechnologies()),
                () -> Assertions.assertEquals(p1.getSalario(), programador.get().getSalario()),
                () -> Assertions.assertEquals(p1.isEsJefeDepartamento(), programador.get().isEsJefeDepartamento()),
                () -> Assertions.assertEquals(p1.isEsJefeProyecto(), programador.get().isEsJefeProyecto()),
                () -> Assertions.assertEquals(p1.isEsJefeActivo(), programador.get().isEsJefeActivo()),
                () -> Assertions.assertEquals(p1.getCorreo(), programador.get().getCorreo()),
                () -> Assertions.assertEquals(p1.getPassword(), programador.get().getPassword()),
                () -> Assertions.assertEquals(login.getId(), programador.get().getLogin().getId())
        );
        Mockito.verify(programadorRepository, Mockito.atLeastOnce()).delete(p1.getId());
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(8)
    void getByIdException() {
        ProgramadorRepository programadorRepository = new ProgramadorRepository();
        ProgramadorService programadorService = new ProgramadorService(programadorRepository);
        Exception exception = assertThrows(SQLException.class,() -> {
            programadorService.getByIdDTO(dto1.getId());
        });

        String expectedMessage = "Error ProgramadorRepository no se ha encontrado la ID " + dto1.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}