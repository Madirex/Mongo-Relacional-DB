package service;

import dao.*;
import database.MongoDBController;
import dto.LoginDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import repository.LoginRepository;
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

@DisplayName("JUnit 5 Test Service Login")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginServiceTest {

    private LoginRepository loginRepository;
    private LoginService loginService;

    private Login l1;
    private Programador programador;
    private AccessHistory accessHistory;
    private Departamento departamento;
    private LoginDTO dto;
    private LoginDTO dto1;

    private final void initData() {
        Date date = new Date();
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", l1);
        this.accessHistory = new AccessHistory(programador.getId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);

        this.l1 = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);
        dto = LoginDTO.builder()
                .id(new ObjectId())
                .programador(programador)
                .instante(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .token("Token")
                .accessHistory(accessHistory)
                .conectado(false)
                .build();

        dto1 = LoginDTO.builder()
                .programador(null)
                .instante(null)
                .token(null)
                .accessHistory(null)
                .conectado(false)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        this.loginRepository = mock(LoginRepository.class);
        this.loginService = new LoginService(loginRepository);
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
        List<Login> lista = new ArrayList<>();
        lista.add(l1);
        Mockito.when(this.loginRepository.findAll()).thenReturn(Optional.of(lista));
        Optional<List<Login>> logins = loginService.findAll();
        assumeTrue(logins.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, logins.get().size()),
                () -> Assertions.assertEquals(l1.getId(), logins.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), logins.get().get(0).getProgramador().getId()),
                () -> Assertions.assertEquals(l1.getInstante(), logins.get().get(0).getInstante()),
                () -> Assertions.assertEquals(l1.getToken(), logins.get().get(0).getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), logins.get().get(0).getAccessHistory().getId()),
                () -> Assertions.assertEquals(l1.isConectado(), logins.get().get(0).isConectado())
        );
        Mockito.verify(loginRepository, Mockito.atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Service FindById")
    @Order(3)
    void findById() throws SQLException {
        Mockito.when(this.loginRepository.getById(l1.getId())).thenReturn(Optional.ofNullable(l1));
        Optional<Login> login = this.loginService.getById(l1.getId());
        assumeTrue(login.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(l1.getId(), login.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), login.get().getProgramador().getId()),
                () -> Assertions.assertEquals(l1.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(l1.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), login.get().getAccessHistory().getId()),
                () -> Assertions.assertEquals(l1.isConectado(), login.get().isConectado())
        );
        Mockito.verify(loginRepository, Mockito.atLeastOnce()).getById(l1.getId());
    }

    @Test
    @DisplayName("Service Insert")
    @Order(4)
    void insertTest() throws SQLException {
        Mockito.when(loginRepository.insert(l1)).thenReturn(Optional.ofNullable(l1));
        Optional<Login> login = this.loginService.insert(l1);
        assumeTrue(login.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(l1.getId(), login.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), login.get().getProgramador().getId()),
                () -> Assertions.assertEquals(l1.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(l1.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), login.get().getAccessHistory().getId()),
                () -> Assertions.assertEquals(l1.isConectado(), login.get().isConectado())
        );
        Mockito.verify(loginRepository, Mockito.atLeastOnce()).insert(l1);
    }

    @Test
    @DisplayName("Service Update")
    @Order(5)
    void updateTest() throws SQLException {
        Mockito.when(loginRepository.update(l1)).thenReturn(Optional.ofNullable(l1));
        Optional<Login> login = this.loginService.update(l1);
        assumeTrue(login.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(l1.getId(), login.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), login.get().getProgramador().getId()),
                () -> Assertions.assertEquals(l1.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(l1.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), login.get().getAccessHistory().getId()),
                () -> Assertions.assertEquals(l1.isConectado(), login.get().isConectado())
        );
        Mockito.verify(loginRepository, Mockito.atLeastOnce()).update(l1);
    }

    @Test
    @DisplayName("Service delete")
    @Order(6)
    void deleteTest() throws SQLException {
        Mockito.when(loginRepository.delete(l1.getId())).thenReturn(Optional.ofNullable(l1));
        Optional<Login> login = this.loginService.delete(l1.getId());
        assumeTrue(login.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(l1.getId(), login.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), login.get().getProgramador().getId()),
                () -> Assertions.assertEquals(l1.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(l1.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), login.get().getAccessHistory().getId()),
                () -> Assertions.assertEquals(l1.isConectado(), login.get().isConectado())
        );
        Mockito.verify(loginRepository, Mockito.atLeastOnce()).delete(l1.getId());
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(8)
    void getByIdException() {
        LoginRepository loginRepository = new LoginRepository();
        LoginService loginService = new LoginService(loginRepository);
        Exception exception = assertThrows(SQLException.class,() -> {
            loginService.getByIdDTO(dto1.getId());
        });

        String expectedMessage = "Error LoginRepository no se ha encontrado la ID " + dto1.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}