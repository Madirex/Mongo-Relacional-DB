package controller;

import dao.*;
import database.MongoDBController;
import dto.FichaDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import service.FichaService;
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

@DisplayName("JUnit 5 y Mockito Test Controller Ficha")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FichaControllerTest {

    private FichaController fichaController;
    private FichaService fichaService;
    private Ficha f1;
    private FichaDTO fichaDTO;
    private Programador programador;
    private Proyecto proyecto;
    private Departamento departamento;
    private AccessHistory accessHistory;
    private Repositorio repositorio;
    private Login login;

    private final void initData() {
        Date date = new Date();
        //Date date1 = Date.valueOf("2022-01-21");
        List<String> technologies = new ArrayList<>();
        technologies.add("C#");
        technologies.add("Java");

        this.programador = new Programador("Jose", date, departamento, technologies,
                1000.00, false, false, false, "jose@jose.com", "12345", login);
        this.proyecto = new Proyecto(programador, "Ciencia", 20000.00, date,
                date, technologies, repositorio, false, departamento);
        this.accessHistory = new AccessHistory(programador.getId(), LocalDateTime.now());
        this.departamento = new Departamento("Departamento tecnológico", programador, 2000000.00, 1000000000.00);
        this.login = new Login(programador, LocalDateTime.now(), "Token", accessHistory, false);
        this.repositorio = new Repositorio("Ciencia", date, proyecto);

        this.f1 = new Ficha(programador, proyecto);
        fichaDTO = FichaDTO.builder()
                .id(new ObjectId())
                .programador(programador)
                .proyecto(proyecto)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        this.fichaService = mock(FichaService.class);
        this.fichaController = new FichaController(fichaService);
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
        List<FichaDTO> lista = new ArrayList<>();
        lista.add(fichaDTO);
        Mockito.when(this.fichaService.getAllDTO()).thenReturn(Optional.of(lista));
        Optional<List<FichaDTO>> fichas = fichaController.getAll();
        assumeTrue(fichas.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, fichas.get().size()),
                () -> Assertions.assertEquals(fichaDTO.getId(), fichas.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), fichas.get().get(0).getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), fichas.get().get(0).getProyecto().getId())
        );
        Mockito.verify(fichaService, Mockito.atLeastOnce()).getAllDTO();
    }

    @Test
    @DisplayName("Controller getById")
    @Order(3)
    void getById() throws SQLException {
        Mockito.when(this.fichaService.getByIdDTO(fichaDTO.getId())).thenReturn(Optional.ofNullable(fichaDTO));
        Optional<FichaDTO> ficha = this.fichaController.getById(fichaDTO.getId());
        assumeTrue(ficha.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(fichaDTO.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), ficha.get().getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), ficha.get().getProyecto().getId())
        );
        Mockito.verify(fichaService, Mockito.atLeastOnce()).getByIdDTO(fichaDTO.getId());
    }

    @Test
    @DisplayName("Controller Insert")
    @Order(4)
    void insert() throws SQLException {
        Mockito.when(fichaService.insertDTO(fichaDTO)).thenReturn(Optional.ofNullable(fichaDTO));
        Optional<FichaDTO> ficha = this.fichaController.insert(fichaDTO);
        assumeTrue(ficha.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(fichaDTO.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), ficha.get().getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), ficha.get().getProyecto().getId())
        );
        Mockito.verify(fichaService, Mockito.atLeastOnce()).insertDTO(fichaDTO);
    }

    @Test
    @DisplayName("Controller Update")
    @Order(5)
    void update() throws SQLException {
        Mockito.when(fichaService.updateDTO(fichaDTO)).thenReturn(Optional.ofNullable(fichaDTO));
        Optional<FichaDTO> ficha = this.fichaController.update(fichaDTO);
        assumeTrue(ficha.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(fichaDTO.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), ficha.get().getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), ficha.get().getProyecto().getId())
        );
        Mockito.verify(fichaService, Mockito.atLeastOnce()).updateDTO(fichaDTO);
    }

    @Test
    @DisplayName("Controller delete")
    @Order(6)
    void delete() throws SQLException {
        Mockito.when(fichaService.deleteDTO(fichaDTO.getId())).thenReturn(Optional.ofNullable(fichaDTO));
        Optional<FichaDTO> ficha = this.fichaController.delete(fichaDTO.getId());
        assumeTrue(ficha.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(fichaDTO.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), ficha.get().getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), ficha.get().getProyecto().getId())
        );
        Mockito.verify(fichaService, Mockito.atLeastOnce()).deleteDTO(fichaDTO.getId());
    }
}