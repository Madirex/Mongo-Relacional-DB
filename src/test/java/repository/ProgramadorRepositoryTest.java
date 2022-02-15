package repository;

import dao.*;
import database.MongoDBController;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import util.ApplicationProperties;
import util.Cifrator;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("JUnit 5 Test CRUD Programador")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProgramadorRepositoryTest {

    private ProgramadorRepository programadorRepository = new ProgramadorRepository();

    private Programador p1;
    private Programador p2;
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
        this.p2 = new Programador(null, null, null, null,
                null, false, false, false, null, null, null);
        this.accessHistory = new AccessHistory(new ObjectId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
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
    @DisplayName("Respository Insert Programador")
    @Order(2)
    void insertTest() throws SQLException {
        Optional<Programador> programador = programadorRepository.insert(p1);
        assumeTrue(programador.isPresent());
        Assertions.assertAll( "Comprobaciones test insert",
                () -> Assertions.assertEquals(p1.getId(), programador.get().getId()),
                () -> Assertions.assertEquals(p1.getNombre(), programador.get().getNombre()),
                () -> Assertions.assertEquals(p1.getFechaAlta(), programador.get().getFechaAlta()),
                () -> Assertions.assertEquals(p1.getDepartamento(), programador.get().getDepartamento()),
                () -> Assertions.assertEquals(p1.getDominatedTechnologies(), programador.get().getDominatedTechnologies()),
                () -> Assertions.assertEquals(p1.getSalario(), programador.get().getSalario()),
                () -> Assertions.assertEquals(p1.isEsJefeDepartamento(), programador.get().isEsJefeDepartamento()),
                () -> Assertions.assertEquals(p1.isEsJefeProyecto(), programador.get().isEsJefeProyecto()),
                () -> Assertions.assertEquals(p1.isEsJefeActivo(), programador.get().isEsJefeActivo()),
                () -> Assertions.assertEquals(p1.getCorreo(), programador.get().getCorreo()),
                () -> Assertions.assertEquals(p1.getPassword(), programador.get().getPassword()),
                () -> Assertions.assertEquals(p1.getLogin(), programador.get().getLogin())
        );
    }

    @Test
    @DisplayName("Get all Programador")
    @Order(3)
    void getAllTest() throws SQLException {
        programadorRepository.insert(p1);
        Optional<List<Programador>> programadores = programadorRepository.findAll();
        assumeTrue(programadores.isPresent());
        Assertions.assertAll( "Comprobaciones test getAll",
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
                () -> Assertions.assertEquals(Cifrator.SHA256(p1.getPassword()), programadores.get().get(0).getPassword()),
                () -> Assertions.assertEquals(login.getId(), programadores.get().get(0).getLogin().getId())
        );
    }

    @Test
    @DisplayName("Find Programador By ID")
    @Order(4)
    void getById() throws SQLException {
        programadorRepository.insert(p1);
        Optional<Programador> programador = programadorRepository.getById(p1.getId());
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
                () -> Assertions.assertEquals(Cifrator.SHA256(p1.getPassword()), programador.get().getPassword()),
                () -> Assertions.assertEquals(login.getId(), programador.get().getLogin().getId())
        );
    }

    @Test
    @DisplayName("Update Programador")
    @Order(5)
    void update() throws SQLException {
        programadorRepository.insert(p1);
        p1.setNombre("Alfonso");
        Optional<Programador> programador = programadorRepository.update(p1);
        assumeTrue(programador.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(p1.getNombre(), programador.get().getNombre()),
                () -> Assertions.assertEquals(p1.getFechaAlta(), programador.get().getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programador.get().getDepartamento().getId()),
                () -> Assertions.assertEquals(p1.getDominatedTechnologies(), programador.get().getDominatedTechnologies()),
                () -> Assertions.assertEquals(p1.getSalario(), programador.get().getSalario()),
                () -> Assertions.assertEquals(p1.isEsJefeDepartamento(), programador.get().isEsJefeDepartamento()),
                () -> Assertions.assertEquals(p1.isEsJefeProyecto(), programador.get().isEsJefeProyecto()),
                () -> Assertions.assertEquals(p1.isEsJefeActivo(), programador.get().isEsJefeActivo()),
                () -> Assertions.assertEquals(p1.getCorreo(), programador.get().getCorreo()),
                () -> Assertions.assertEquals(Cifrator.SHA256(p1.getPassword()), programador.get().getPassword()),
                () -> Assertions.assertEquals(login.getId(), programador.get().getLogin().getId())
        );
    }

    @Test
    @DisplayName("Delete Programador")
    @Order(6)
    void delete() throws SQLException {
        programadorRepository.insert(p1);
        Optional<Programador> programador = programadorRepository.delete(p1.getId());
        assumeTrue(programador.isPresent());
        Assertions.assertAll("Comprobaciones Test Update",
                () -> Assertions.assertEquals(p1.getNombre(), programador.get().getNombre()),
                () -> Assertions.assertEquals(p1.getFechaAlta(), programador.get().getFechaAlta()),
                () -> Assertions.assertEquals(departamento.getId(), programador.get().getDepartamento().getId()),
                () -> Assertions.assertEquals(p1.getDominatedTechnologies(), programador.get().getDominatedTechnologies()),
                () -> Assertions.assertEquals(p1.getSalario(), programador.get().getSalario()),
                () -> Assertions.assertEquals(p1.isEsJefeDepartamento(), programador.get().isEsJefeDepartamento()),
                () -> Assertions.assertEquals(p1.isEsJefeProyecto(), programador.get().isEsJefeProyecto()),
                () -> Assertions.assertEquals(p1.isEsJefeActivo(), programador.get().isEsJefeActivo()),
                () -> Assertions.assertEquals(p1.getCorreo(), programador.get().getCorreo()),
                () -> Assertions.assertEquals(Cifrator.SHA256(p1.getPassword()), programador.get().getPassword()),
                () -> Assertions.assertEquals(login.getId(), programador.get().getLogin().getId())
        );
    }

    @Test
    @DisplayName("Repository getById Exception Test")
    @Order(7)
    void getByIdException() {
        Exception exception = assertThrows(SQLException.class,() -> {
            programadorRepository.getById(p2.getId());
        });

        String expectedMessage = "Error ProgramadorRepository no se ha encontrado la ID " + p2.getId();
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}