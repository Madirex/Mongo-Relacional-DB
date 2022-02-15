package controller;

import dao.*;
import database.MongoDBController;
import dto.RepositorioDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import service.RepositorioService;
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

@DisplayName("JUnit 5 y Mockito Test Controller Repositorio")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RepositorioControllerTest {

    private RepositorioService repositorioService;
    private RepositorioController repositorioController;

    private Repositorio repositorio;
    private RepositorioDTO repositorioDTO;
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
                date, technologies, repositorio, false, departamento);
        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.login = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);
        this.accessHistory = new AccessHistory(new ObjectId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        this.repositorio = new Repositorio("Ciencia", date, proyecto);
        repositorioDTO = RepositorioDTO.builder()
                .id(new ObjectId())
                .nombre("Ciencia")
                .creationDate(date)
                .proyecto(proyecto)
                .build();

    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        this.repositorioService = mock(RepositorioService.class);
        this.repositorioController = new RepositorioController(repositorioService);
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
        List<RepositorioDTO> lista = new ArrayList<>();
        lista.add(repositorioDTO);
        Mockito.when(this.repositorioService.getAllDTO()).thenReturn(Optional.of(lista));
        Optional<List<RepositorioDTO>> repositorios = repositorioController.getAll();
        assumeTrue(repositorios.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, repositorios.get().size()),
                () -> Assertions.assertEquals(repositorioDTO.getId(), repositorios.get().get(0).getId()),
                () -> Assertions.assertEquals(repositorioDTO.getCreationDate(), repositorios.get().get(0).getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorios.get().get(0).getProyecto().getId())
        );
        Mockito.verify(repositorioService, Mockito.atLeastOnce()).getAllDTO();
    }

    @Test
    @DisplayName("Controller getById")
    @Order(3)
    void getById() throws SQLException {
        Mockito.when(this.repositorioService.getByIdDTO(repositorioDTO.getId())).thenReturn(Optional.ofNullable(repositorioDTO));
        Optional<RepositorioDTO> repositorio = this.repositorioController.getById(repositorioDTO.getId());
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(repositorioDTO.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(repositorioDTO.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorio.get().getProyecto().getId())
        );
        Mockito.verify(repositorioService, Mockito.atLeastOnce()).getByIdDTO(repositorioDTO.getId());
    }

    @Test
    @DisplayName("Controller Insert")
    @Order(4)
    void insert() throws SQLException {
        Mockito.when(repositorioService.insertDTO(repositorioDTO)).thenReturn(Optional.ofNullable(repositorioDTO));
        Optional<RepositorioDTO> repositorio = this.repositorioController.insert(repositorioDTO);
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(repositorioDTO.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(repositorioDTO.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorio.get().getProyecto().getId())
        );
        Mockito.verify(repositorioService, Mockito.atLeastOnce()).insertDTO(repositorioDTO);
    }

    @Test
    @DisplayName("Controller Update")
    @Order(5)
    void update() throws SQLException {
        Mockito.when(repositorioService.updateDTO(repositorioDTO)).thenReturn(Optional.ofNullable(repositorioDTO));
        Optional<RepositorioDTO> repositorio = this.repositorioController.update(repositorioDTO);
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(repositorioDTO.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(repositorioDTO.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorio.get().getProyecto().getId())
        );
        Mockito.verify(repositorioService, Mockito.atLeastOnce()).updateDTO(repositorioDTO);
    }

    @Test
    @DisplayName("Controller delete")
    @Order(6)
    void delete() throws SQLException {
        Mockito.when(repositorioService.deleteDTO(repositorioDTO.getId())).thenReturn(Optional.ofNullable(repositorioDTO));
        Optional<RepositorioDTO> repositorio = this.repositorioController.delete(repositorioDTO.getId());
        assumeTrue(repositorio.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(repositorioDTO.getId(), repositorio.get().getId()),
                () -> Assertions.assertEquals(repositorioDTO.getCreationDate(), repositorio.get().getCreationDate()),
                () -> Assertions.assertEquals(proyecto.getId(), repositorio.get().getProyecto().getId())
        );
        Mockito.verify(repositorioService, Mockito.atLeastOnce()).deleteDTO(repositorioDTO.getId());
    }
}