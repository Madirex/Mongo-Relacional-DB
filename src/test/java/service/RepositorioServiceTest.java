package service;

import dao.*;
import database.MongoDBController;
import dto.RepositorioDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import repository.RepositorioRepository;
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

@DisplayName("JUnit 5 Test Service Repositorio")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RepositorioServiceTest {

    private RepositorioRepository repositorioRepository;
    private RepositorioService repositorioService;

    private Repositorio r1;
    private RepositorioDTO repositorioDTO;
    private RepositorioDTO dto1;
    private Proyecto proyecto;
    private Programador programador;
    private Departamento departamento;
    private Login login;
    private AccessHistory accessHistory;

    private final void initData() {
        Date date = new Date();
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.proyecto = new Proyecto(programador, "Ciencia", 20000.00, date,
                date, technologies, r1, false, departamento);
        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.login = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);
        this.accessHistory = new AccessHistory(new ObjectId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        this.r1 = new Repositorio("Ciencia", date, proyecto);
        repositorioDTO = RepositorioDTO.builder()
                .id(new ObjectId())
                .nombre("Ciencia")
                .creationDate(date)
                .proyecto(proyecto)
                .build();
        dto1 = RepositorioDTO.builder()
                .nombre(null)
                .creationDate(null)
                .proyecto(null)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        this.repositorioRepository = mock(RepositorioRepository.class);
        this.repositorioService = new RepositorioService(repositorioRepository);
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
        List<Repositorio> lista = new ArrayList<>();
        lista.add(r1);
        Mockito.when(this.repositorioRepository.findAll()).thenReturn(Optional.of(lista));
        Optional<List<Repositorio>> repositorios = repositorioService.findAll();
        assumeTrue(repositorios.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, repositorios.get().size()),
                () -> Assertions.assertEquals(r1.getId(), repositorios.get().get(0).getId()),
                () -> Assertions.assertEquals(r1.getCreationDate(), repositorios.get().get(0).getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorios.get().get(0).getProyecto().getId())
        );
        Mockito.verify(repositorioRepository, Mockito.atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Service FindById")
    @Order(3)
    void findById() throws SQLException {
        Mockito.when(this.repositorioRepository.getById(r1.getId())).thenReturn(Optional.ofNullable(r1));
        Optional<Repositorio> repositorio = this.repositorioService.getById(r1.getId());
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(r1.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(r1.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorio.get().getProyecto().getId())
        );
        Mockito.verify(repositorioRepository, Mockito.atLeastOnce()).getById(r1.getId());
    }

    @Test
    @DisplayName("Service Insert")
    @Order(4)
    void insertTest() throws SQLException {
        Mockito.when(repositorioRepository.insert(r1)).thenReturn(Optional.ofNullable(r1));
        Optional<Repositorio> repositorio = this.repositorioService.insert(r1);
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(r1.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(r1.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorio.get().getProyecto().getId())
        );
        Mockito.verify(repositorioRepository, Mockito.atLeastOnce()).insert(r1);
    }

    @Test
    @DisplayName("Service Update")
    @Order(5)
    void updateTest() throws SQLException {
        Mockito.when(repositorioRepository.update(r1)).thenReturn(Optional.ofNullable(r1));
        Optional<Repositorio> repositorio = this.repositorioService.update(r1);
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(r1.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(r1.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorio.get().getProyecto().getId())
        );
        Mockito.verify(repositorioRepository, Mockito.atLeastOnce()).update(r1);
    }

    @Test
    @DisplayName("Service delete")
    @Order(6)
    void deleteTest() throws SQLException {
        Mockito.when(repositorioRepository.delete(r1.getId())).thenReturn(Optional.ofNullable(r1));
        Optional<Repositorio> repositorio = this.repositorioService.delete(r1.getId());
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(r1.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(r1.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorio.get().getProyecto().getId())
        );
        Mockito.verify(repositorioRepository, Mockito.atLeastOnce()).delete(r1.getId());
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(8)
    void getByIdException() {
        RepositorioRepository repositorioRepository = new RepositorioRepository();
        RepositorioService repositorioService = new RepositorioService(repositorioRepository);
        Exception exception = assertThrows(SQLException.class,() -> {
            repositorioService.getByIdDTO(dto1.getId());
        });

        String expectedMessage = "Error RepositorioRepository no se ha encontrado la ID " + dto1.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}