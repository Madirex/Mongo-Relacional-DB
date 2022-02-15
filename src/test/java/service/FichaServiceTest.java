package service;

import dao.*;
import database.MongoDBController;
import dto.FichaDTO;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import repository.FichaRepository;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;

@DisplayName("JUnit 5 Test Service Ficha")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FichaServiceTest {

    private FichaRepository fichaRepository;
    private FichaService fichaService;

    private Ficha f1;
    private Programador programador;
    private Proyecto proyecto;
    private Departamento departamento;
    private AccessHistory accessHistory;
    private Repositorio repositorio;
    private Login login;
    private FichaDTO dto;
    private FichaDTO dto1;

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
        dto = FichaDTO.builder()
                .id(new ObjectId())
                .programador(programador)
                .proyecto(proyecto)
                .build();
        dto1 = FichaDTO.builder()
                .programador(null)
                .proyecto(null)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        mongoController.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        this.fichaRepository = mock(FichaRepository.class);
        this.fichaService = new FichaService(fichaRepository);
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
        List<Ficha> lista = new ArrayList<>();
        lista.add(f1);
        Mockito.when(this.fichaRepository.findAll()).thenReturn(Optional.of(lista));
        Optional<List<Ficha>> fichas = fichaService.findAll();
        assumeTrue(fichas.isPresent());
        Assertions.assertAll( "Comprobaciones test findAll",
                () -> Assertions.assertEquals(1, fichas.get().size()),
                () -> Assertions.assertEquals(f1.getId(), fichas.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), fichas.get().get(0).getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), fichas.get().get(0).getProyecto().getId())
        );
        Mockito.verify(fichaRepository, Mockito.atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Service FindById")
    @Order(3)
    void findById() throws SQLException {
        Mockito.when(this.fichaRepository.getById(f1.getId())).thenReturn(Optional.ofNullable(f1));
        Optional<Ficha> ficha = this.fichaService.getById(f1.getId());
        assumeTrue(ficha.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(f1.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), ficha.get().getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), ficha.get().getProyecto().getId())
        );
        Mockito.verify(fichaRepository, Mockito.atLeastOnce()).getById(f1.getId());
    }

    @Test
    @DisplayName("Service Insert")
    @Order(4)
    void insertTest() throws SQLException {
        Mockito.when(fichaRepository.insert(f1)).thenReturn(Optional.ofNullable(f1));
        Optional<Ficha> ficha = this.fichaService.insert(f1);
        assumeTrue(ficha.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(f1.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), ficha.get().getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), ficha.get().getProyecto().getId())
        );
        Mockito.verify(fichaRepository, Mockito.atLeastOnce()).insert(f1);
    }

    @Test
    @DisplayName("Service Update")
    @Order(5)
    void updateTest() throws SQLException {
        Mockito.when(fichaRepository.update(f1)).thenReturn(Optional.ofNullable(f1));
        Optional<Ficha> ficha = this.fichaService.update(f1);
        assumeTrue(ficha.isPresent());
        Assertions.assertAll( "Comprobaciones test update",
                () -> Assertions.assertEquals(f1.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), ficha.get().getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), ficha.get().getProyecto().getId())
        );
        Mockito.verify(fichaRepository, Mockito.atLeastOnce()).update(f1);
    }

    @Test
    @DisplayName("Service delete")
    @Order(6)
    void deleteTest() throws SQLException {
        Mockito.when(fichaRepository.delete(f1.getId())).thenReturn(Optional.ofNullable(f1));
        Optional<Ficha> ficha = this.fichaService.delete(f1.getId());
        assumeTrue(ficha.isPresent());
        Assertions.assertAll("Comprobaciones Test delete",
                () -> Assertions.assertEquals(f1.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), ficha.get().getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), ficha.get().getProyecto().getId())
        );
        Mockito.verify(fichaRepository, Mockito.atLeastOnce()).delete(f1.getId());
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(8)
    void getByIdException() {
        FichaRepository fichaRepository = new FichaRepository();
        FichaService fichaService = new FichaService(fichaRepository);
        Exception exception = assertThrows(SQLException.class,() -> {
            fichaService.getByIdDTO(dto1.getId());
        });

        String expectedMessage = "Error FichaRepository no se ha encontrado la ID " + dto1.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}