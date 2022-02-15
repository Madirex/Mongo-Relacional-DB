package controller;

import dao.Departamento;
import dao.AccessHistory;
import dao.Login;
import dao.Programador;
import database.MongoDBController;
import dto.LoginDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import service.LoginService;
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

@DisplayName("JUnit 5 y Mockito Test Controller Login")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginControllerTest {

    private LoginController loginController;
    private LoginService loginService;

    private Login login;
    private LoginDTO loginDTO;
    private Programador programador;
    private AccessHistory accessHistory;
    private Departamento departamento;

    private final void initData() {
        Date date = new Date();
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.accessHistory = new AccessHistory(programador.getId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);

        this.login = new Login(programador, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Token", accessHistory, false);
        loginDTO = LoginDTO.builder()
                .id(new ObjectId())
                .programador(programador)
                .instante(LocalDateTime.now())
                .token("Token")
                .accessHistory(accessHistory)
                .conectado(false)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        this.loginService = mock(LoginService.class);
        this.loginController = new LoginController(loginService);
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
        List<LoginDTO> lista = new ArrayList<>();
        lista.add(loginDTO);
        Mockito.when(this.loginService.getAllDTO()).thenReturn(Optional.of(lista));
        Optional<List<LoginDTO>> logins = loginController.getAll();
        assumeTrue(logins.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, logins.get().size()),
                () -> Assertions.assertEquals(loginDTO.getId(), logins.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), logins.get().get(0).getProgramador().getId()),
                () -> Assertions.assertEquals(loginDTO.getInstante(), logins.get().get(0).getInstante()),
                () -> Assertions.assertEquals(loginDTO.getToken(), logins.get().get(0).getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), logins.get().get(0).getAccessHistory().getId()),
                () -> Assertions.assertEquals(loginDTO.isConectado(), logins.get().get(0).isConectado())
        );
        Mockito.verify(loginService, Mockito.atLeastOnce()).getAllDTO();
    }

    @Test
    @DisplayName("Controller getById")
    @Order(3)
    void getById() throws SQLException {
        Mockito.when(this.loginService.getByIdDTO(loginDTO.getId())).thenReturn(Optional.ofNullable(loginDTO));
        Optional<LoginDTO> login = this.loginController.getById(loginDTO.getId());
        assumeTrue(login.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(loginDTO.getId(), login.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), login.get().getProgramador().getId()),
                () -> Assertions.assertEquals(loginDTO.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(loginDTO.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), login.get().getAccessHistory().getId()),
                () -> Assertions.assertEquals(loginDTO.isConectado(), login.get().isConectado())
        );
        Mockito.verify(loginService, Mockito.atLeastOnce()).getByIdDTO(loginDTO.getId());
    }

    @Test
    @DisplayName("Controller Insert")
    @Order(4)
    void insert() throws SQLException {
        Mockito.when(loginService.insertDTO(loginDTO)).thenReturn(Optional.ofNullable(loginDTO));
        Optional<LoginDTO> login = this.loginController.insert(loginDTO);
        assumeTrue(login.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(loginDTO.getId(), login.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), login.get().getProgramador().getId()),
                () -> Assertions.assertEquals(loginDTO.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(loginDTO.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), login.get().getAccessHistory().getId()),
                () -> Assertions.assertEquals(loginDTO.isConectado(), login.get().isConectado())
        );
        Mockito.verify(loginService, Mockito.atLeastOnce()).insertDTO(loginDTO);
    }

    @Test
    @DisplayName("Controller Update")
    @Order(5)
    void update() throws SQLException {
        Mockito.when(loginService.updateDTO(loginDTO)).thenReturn(Optional.ofNullable(loginDTO));
        Optional<LoginDTO> login = this.loginController.update(loginDTO);
        assumeTrue(login.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(loginDTO.getId(), login.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), login.get().getProgramador().getId()),
                () -> Assertions.assertEquals(loginDTO.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(loginDTO.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), login.get().getAccessHistory().getId()),
                () -> Assertions.assertEquals(loginDTO.isConectado(), login.get().isConectado())
        );
        Mockito.verify(loginService, Mockito.atLeastOnce()).updateDTO(loginDTO);
    }

    @Test
    @DisplayName("Controller delete")
    @Order(6)
    void delete() throws SQLException {
        Mockito.when(loginService.deleteDTO(loginDTO.getId())).thenReturn(Optional.ofNullable(loginDTO));
        Optional<LoginDTO> login = this.loginController.delete(loginDTO.getId());
        assumeTrue(login.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(loginDTO.getId(), login.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), login.get().getProgramador().getId()),
                () -> Assertions.assertEquals(loginDTO.getInstante(), login.get().getInstante()),
                () -> Assertions.assertEquals(loginDTO.getToken(), login.get().getToken()),
                () -> Assertions.assertEquals(accessHistory.getId(), login.get().getAccessHistory().getId()),
                () -> Assertions.assertEquals(loginDTO.isConectado(), login.get().isConectado())
        );
        Mockito.verify(loginService, Mockito.atLeastOnce()).deleteDTO(loginDTO.getId());
    }

}