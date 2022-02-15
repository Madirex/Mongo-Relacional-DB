package controller;

import dao.Departamento;
import dao.AccessHistory;
import dao.Login;
import dao.Programador;
import database.MongoDBController;
import dto.AccessHistoryDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import service.AccessHistoryService;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;

@DisplayName("JUnit 5 y Mockito Test Controller AccessHistory")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccessHistoryControllerTest {

    private AccessHistoryController accessHistoryController;
    private AccessHistoryService accessHistoryService;
    private AccessHistory h1;
    private AccessHistoryDTO accessHistoryDTO;
    private Programador programador;
    private Departamento departamento;
    private Login login;

    private final void initData() {
        Date date = new Date();
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.login = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", h1, false);

        this.h1 = new AccessHistory(programador.getId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        accessHistoryDTO = AccessHistoryDTO.builder()
                .id(new ObjectId())
                .programador(programador.getId())
                .instante(LocalDateTime.now())
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        this.accessHistoryService = mock(AccessHistoryService.class);
        this.accessHistoryController= new AccessHistoryController(accessHistoryService);
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
        List<AccessHistoryDTO> lista = new ArrayList<>();
        lista.add(accessHistoryDTO);
        Mockito.when(this.accessHistoryService.getAllDTO()).thenReturn(Optional.of(lista));
        Optional<List<AccessHistoryDTO>> accessHistorys = accessHistoryController.getAll();
        assumeTrue(accessHistorys.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, accessHistorys.get().size()),
                () -> Assertions.assertEquals(accessHistoryDTO.getId(), accessHistorys.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistorys.get().get(0).getProgramador()),
                () -> Assertions.assertEquals(accessHistoryDTO.getInstante(), accessHistorys.get().get(0).getInstante())
        );
        Mockito.verify(accessHistoryService, Mockito.atLeastOnce()).getAllDTO();
    }

    @Test
    @DisplayName("Controller getById")
    @Order(3)
    void getById() throws SQLException {
        Mockito.when(this.accessHistoryService.getByIdDTO(accessHistoryDTO.getId())).thenReturn(Optional.ofNullable(accessHistoryDTO));
        Optional<AccessHistoryDTO> accessHistory = this.accessHistoryController.getById(accessHistoryDTO.getId());
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(accessHistoryDTO.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(accessHistoryDTO.getInstante(), accessHistory.get().getInstante())
        );
        Mockito.verify(accessHistoryService, Mockito.atLeastOnce()).getByIdDTO(accessHistoryDTO.getId());
    }

    @Test
    @DisplayName("Controller Insert")
    @Order(4)
    void insert() throws SQLException {
        Mockito.when(accessHistoryService.insertDTO(accessHistoryDTO)).thenReturn(Optional.ofNullable(accessHistoryDTO));
        Optional<AccessHistoryDTO> accessHistory = this.accessHistoryController.insert(accessHistoryDTO);
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(accessHistoryDTO.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(accessHistoryDTO.getInstante(), accessHistory.get().getInstante())
        );
        Mockito.verify(accessHistoryService, Mockito.atLeastOnce()).insertDTO(accessHistoryDTO);
    }

    @Test
    @DisplayName("Controller Update")
    @Order(5)
    void update() throws SQLException {
        Mockito.when(accessHistoryService.updateDTO(accessHistoryDTO)).thenReturn(Optional.ofNullable(accessHistoryDTO));
        Optional<AccessHistoryDTO> accessHistory = this.accessHistoryController.update(accessHistoryDTO);
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(accessHistoryDTO.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(accessHistoryDTO.getInstante(), accessHistory.get().getInstante())
        );
        Mockito.verify(accessHistoryService, Mockito.atLeastOnce()).updateDTO(accessHistoryDTO);
    }

    @Test
    @DisplayName("Controller delete")
    @Order(6)
    void delete() throws SQLException {
        Mockito.when(accessHistoryService.deleteDTO(accessHistoryDTO.getId())).thenReturn(Optional.ofNullable(accessHistoryDTO));
        Optional<AccessHistoryDTO> accessHistory = this.accessHistoryController.delete(accessHistoryDTO.getId());
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(accessHistoryDTO.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(accessHistoryDTO.getInstante(), accessHistory.get().getInstante())
        );
        Mockito.verify(accessHistoryService, Mockito.atLeastOnce()).deleteDTO(accessHistoryDTO.getId());
    }
}