package repository;

import dao.*;
import database.MongoDBController;
import org.junit.jupiter.api.*;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("JUnit 5 Test CRUD Ficha")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FichaRepositoryTest {

    private FichaRepository fichaRepository = new FichaRepository();

    private Ficha f1;
    private Ficha f2;
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
        this.f2 = new Ficha(null, null);
    }

    @BeforeEach
    public void setUp() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
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
    @DisplayName("Respository Insert Test")
    @Order(2)
    void insertTest() throws SQLException {
        Optional<Ficha> ficha = fichaRepository.insert(f1);
        assumeTrue(ficha.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(f1.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(f1.getProgramador(), ficha.get().getProgramador()),
                () -> Assertions.assertEquals(f1.getProyecto(), ficha.get().getProyecto())
        );
    }

    @Test
    @DisplayName("Get all Ficha")
    @Order(3)
    void getAllTest() throws SQLException {
        fichaRepository.insert(f1);
        Optional<List<Ficha>> fichas = fichaRepository.findAll();
        assumeTrue(fichas.isPresent());
        Assertions.assertAll("Comprobaciones test getAll",
                () -> Assertions.assertEquals(1, fichas.get().size()),
                () -> Assertions.assertEquals(f1.getId(), fichas.get().get(0).getId()),
                () -> Assertions.assertEquals(programador.getId(), fichas.get().get(0).getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), fichas.get().get(0).getProyecto().getId())
        );
    }

    @Test
    @DisplayName("Find Ficha By ID")
    @Order(4)
    void getById() throws SQLException {
        fichaRepository.insert(f1);
        Optional<Ficha> ficha = fichaRepository.getById(f1.getId());
        assumeTrue(ficha.isPresent());
        Assertions.assertAll("Comprobaciones Test getId",
                () -> Assertions.assertEquals(f1.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), ficha.get().getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), ficha.get().getProyecto().getId())
        );
    }

    @Test
    @DisplayName("Update Ficha")
    @Order(5)
    void update() throws SQLException {
        fichaRepository.insert(f1);
        Proyecto proyecto1 = new Proyecto();
        f1.setProyecto(proyecto1);
        Optional<Ficha> ficha = fichaRepository.update(f1);
        assumeTrue(ficha.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(f1.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), ficha.get().getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto1.getId(), ficha.get().getProyecto().getId())
        );
    }

    @Test
    @DisplayName("Delete Departamento")
    @Order(6)
    void delete() throws SQLException {
        fichaRepository.insert(f1);
        Optional<Ficha> ficha = fichaRepository.delete(f1.getId());
        assumeTrue(ficha.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(f1.getId(), ficha.get().getId()),
                () -> Assertions.assertEquals(programador.getId(), ficha.get().getProgramador().getId()),
                () -> Assertions.assertEquals(proyecto.getId(), ficha.get().getProyecto().getId())
        );
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(7)
    void getByIdException() {
        Exception exception = assertThrows(SQLException.class,() -> {
            fichaRepository.getById(f2.getId());
        });

        String expectedMessage = "Error FichaRepository no se ha encontrado la ID " + f2.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}