package service;

import dao.*;
import database.MongoDBController;
import dto.AccessHistoryDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import repository.AccessHistoryRepository;
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

@DisplayName("JUnit 5 Test Service AccessHistory")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccessHistoryServiceTest {

    private AccessHistoryRepository accessHistoryRepository;
    private AccessHistoryService accessHistoryService;
    private AccessHistory h1;
    private Programador programador;
    private Departamento departamento;
    private Login login;
    private AccessHistoryDTO dto;
    private AccessHistoryDTO dto1;

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
        dto = AccessHistoryDTO.builder()
                .id(new ObjectId())
                .programador(programador.getId())
                .instante(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .build();

        dto1 = AccessHistoryDTO.builder()
                .programador(null)
                .instante(null)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        this.accessHistoryRepository = mock(AccessHistoryRepository.class);
        this.accessHistoryService = new AccessHistoryService(accessHistoryRepository);
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
        List<AccessHistory> lista = new ArrayList<>();
        lista.add(h1);
        Mockito.when(this.accessHistoryRepository.findAll()).thenReturn(Optional.of(lista));
        Optional<List<AccessHistory>> accessHistorys = accessHistoryService.findAll();
        assumeTrue(accessHistorys.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, accessHistorys.get().size()),
                () -> Assertions.assertEquals(h1.getId(), accessHistorys.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistorys.get().get(0).getProgramador()),
                () -> Assertions.assertEquals(h1.getInstante(), accessHistorys.get().get(0).getInstante())
        );
        Mockito.verify(accessHistoryRepository, Mockito.atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Service FindById")
    @Order(3)
    void findById() throws SQLException {
        Mockito.when(this.accessHistoryRepository.getById(h1.getId())).thenReturn(Optional.ofNullable(h1));
        Optional<AccessHistory> accessHistory = this.accessHistoryService.getById(h1.getId());
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(h1.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(h1.getInstante(), accessHistory.get().getInstante())
        );
        Mockito.verify(accessHistoryRepository, Mockito.atLeastOnce()).getById(h1.getId());
    }

    @Test
    @DisplayName("Service Insert")
    @Order(4)
    void insertTest() throws SQLException {
        Mockito.when(accessHistoryRepository.insert(h1)).thenReturn(Optional.ofNullable(h1));
        Optional<AccessHistory> accessHistory = this.accessHistoryService.insert(h1);
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(h1.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(h1.getInstante(), accessHistory.get().getInstante())
        );
        Mockito.verify(accessHistoryRepository, Mockito.atLeastOnce()).insert(h1);
    }

    @Test
    @DisplayName("Service Update")
    @Order(5)
    void updateTest() throws SQLException {
        Mockito.when(accessHistoryRepository.update(h1)).thenReturn(Optional.ofNullable(h1));
        Optional<AccessHistory> accessHistory = this.accessHistoryService.update(h1);
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(h1.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(h1.getInstante(), accessHistory.get().getInstante())
        );
        Mockito.verify(accessHistoryRepository, Mockito.atLeastOnce()).update(h1);
    }

    @Test
    @DisplayName("Service delete")
    @Order(6)
    void deleteTest() throws SQLException {
        Mockito.when(accessHistoryRepository.delete(h1.getId())).thenReturn(Optional.ofNullable(h1));
        Optional<AccessHistory> accessHistory = this.accessHistoryService.delete(h1.getId());
        assumeTrue(accessHistory.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(h1.getId(), accessHistory.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), accessHistory.get().getProgramador()),
                () -> Assertions.assertEquals(h1.getInstante(), accessHistory.get().getInstante())
        );
        Mockito.verify(accessHistoryRepository, Mockito.atLeastOnce()).delete(h1.getId());
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(8)
    void getByIdException() {
        AccessHistoryRepository accessHistoryRepository = new AccessHistoryRepository();
        AccessHistoryService accessHistoryService = new AccessHistoryService(accessHistoryRepository);
        Exception exception = assertThrows(SQLException.class,() -> {
            accessHistoryService.getByIdDTO(dto1.getId());
        });

        String expectedMessage = "Error AccessHistoryRepository no se ha encontrado la ID " + dto1.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}