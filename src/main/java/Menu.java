import controller.*;
import dao.*;
import database.MongoDBController;
import dto.*;
import org.bson.types.ObjectId;
import repository.*;
import service.*;
import util.ApplicationProperties;
import util.Token;
import util.UserScanner;
import util.Util;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class Menu {

    private static Menu menuInstance;
    private static UserScanner scanner;
    private static Util util;

    private Menu() {
        scanner = UserScanner.getInstance();
        util = Util.getInstance();
    }

    public static Menu getInstance() {
        if (menuInstance == null) {
            menuInstance = new Menu();
        }
        return menuInstance;
    }

    /**
     * Inicializar los datos
     */
    public void initData() {
        System.out.println("Iniciamos los datos");
        MongoDBController controller = MongoDBController.getInstance();

        controller.open();

        if (ApplicationProperties.getInstance().readProperty("database.init").equals("true")){
            controller.initData();
        }

        controller.close();
    }

    /**
     * Busca los datos y los retorna
     * @return Datos
     * @throws SQLException SqlException
     */
    public String searchData() throws SQLException {
        Optional<String> result = Optional.empty();

        do {
            //Selección de exporte
            int nSelected = -1;
            Scanner sc;

            do {
                sc = new Scanner(System.in);
                System.out.println("\n▶ ¿Qué datos quieres exportar?\n" +
                        "1 -> Operaciones CRUD\n2 -> Otras operaciones\n");

                try {
                    nSelected = sc.nextInt();

                    //Evitar que se introduzca un número menor que 1 o mayor que 2
                    if (nSelected < 1 || nSelected > 2) {
                        nSelected = -1;
                    }
                } catch (InputMismatchException e) {
                    System.err.println("Error: no has introducido un número válido.");
                    nSelected = -1;
                }
            } while (nSelected == -1);

            //Operaciones CRUD
            if (nSelected == 1) {
                result = searchCRUDClass();
            }
            //Otras operaciones
            else if (nSelected == 2) {
                result = searchOtherOpt();
            }
        }while(result.isEmpty());

        return result.get();
    }

    /**
     * Buscar por operaciones CRUD (entidades)
     * @return Entidad a consultar
     */
    private Optional<String> searchCRUDClass() {
        Optional<String> result;

        do {
            //Selección de exporte
            result = Optional.empty();
            int nSelected = -1;
            Scanner sc;

            do {
                sc = new Scanner(System.in);
                System.out.println("\n▶ ¿Sobre qué deseas realizar la acción?\n" +
                        "0 -> Volver atrás\n1 -> Commit\n2 -> Departamento\n3 -> Ficha\n4 -> Issue\n5 -> Programador\n" +
                        "6 -> Proyecto\n7 -> Repositorio\n8 -> Tarea\n9 -> Login\n10 -> Histórico de acceso");

                try {
                    nSelected = sc.nextInt();

                    //Evitar que se introduzca un número menor que 0 o mayor que 10
                    if (nSelected < 0 || nSelected > 10) {
                        nSelected = -1;
                    }
                } catch (InputMismatchException e) {
                    System.err.println("Error: no has introducido un número válido.");
                    nSelected = -1;
                }
            } while (nSelected == -1);

            BaseController controller;
            switch(nSelected){
                case 1:
                    //Commit
                    controller = new CommitController(new CommitService(new CommitRepository()));
                    break;
                case 2:
                    //Departamento
                    controller = new DepartamentoController(new DepartamentoService(new DepartamentoRepository()));
                    break;
                case 3:
                    //Ficha
                    controller = new FichaController(new FichaService(new FichaRepository()));
                    break;
                case 4:
                    //Issue
                    controller = new IssueController(new IssueService(new IssueRepository()));
                    break;
                case 5:
                    //Programador
                    controller = new ProgramadorController(new ProgramadorService(new ProgramadorRepository()));
                    break;
                case 6:
                    //Proyecto
                    controller = new ProyectoController(new ProyectoService(new ProyectoRepository()));
                    break;
                case 7:
                    //Repositorio
                    controller = new RepositorioController(new RepositorioService(new RepositorioRepository()));
                    break;
                case 8:
                    //Tarea
                    controller = new TareaController(new TareaService(new TareaRepository()));
                    break;
                case 9:
                    //Login
                    controller = new LoginController(new LoginService(new LoginRepository()));
                    break;
                case 10:
                    //Histórico de Acceso
                    controller = new AccessHistoryController(new AccessHistoryService(new AccessHistoryRepository()));
                    break;
                default:
                    //Volver atrás
                    return Optional.empty();
            }

            //Seleccionar ahora el tipo de CRUD
            result = selectCRUDType(controller);

        }while(result.isEmpty());

        return result;
    }

    /**
     * Realizar consultas avanzadas
     * @return Consulta devuelta en un String dentro de un Optional
     */
    private Optional<String> searchOtherOpt() {
        Optional<String> result;

        do {
            //Selección de exporte
            result = Optional.empty();
            int nSelected = -1;
            Scanner sc;

            do {
                sc = new Scanner(System.in);
                System.out.println("\n▶ ¿Sobre qué deseas obtener información?\n" +
                        "0 -> Volver atrás\n" +
                        "1 -> Obtener de un departamento, los proyectos y trabajadores asociados.\n" +
                        "2 -> Listado de issues abiertas por proyecto que no se han consolidado en commits.\n" +
                        "3 -> Programadores de un proyecto ordenados por número de commits.\n" +
                        "4 -> Programadores con su productividad completa: Proyectos en los que participa, " +
                        "commits e issues asignadas.\n" +
                        "5 -> Tres proyectos más caros en base a su presupuesto asignado y " +
                        "el salario de cada trabajador que participa.\n" +
                        "6 -> Listado de proyectos con su información completa. Programadores que trabajan en él. " +
                        "Repositorio asociado con su listado de issues y de commits.\n" +
                        "7 -> Número de accesos de un programador e información de los mismos, es decir, " +
                        "fecha en la que se produjo cada uno de ellos.\n");

                try {
                    nSelected = sc.nextInt();

                    //Evitar que se introduzca un número menor que 1 o mayor que 7
                    if (nSelected < 0 || nSelected > 7) {
                        nSelected = -1;
                    }
                } catch (InputMismatchException e) {
                    System.err.println("Error: no has introducido un número válido.");
                    nSelected = -1;
                }
            } while (nSelected == -1);

            switch(nSelected){
                case 1:
                    //Obtener de un departamento, los proyectos y trabajadores asociados.
                    ObjectId id = new ObjectId(scanner.scannerString("Introduce la ID del departamento"));
                    Optional<List<Proyecto>> proyectoListOpt = MoreOptionController.getInstance()
                            .getDepartamentProjects(id);
                    Optional<List<ProgramadorDTO>> trabajadorListOpt = MoreOptionController.getInstance()
                            .getAllTrabajadoresAsociados(id);

                    if (proyectoListOpt.isPresent()){
                        Optional<String> proyectos = util.convertToJSON(proyectoListOpt);
                        StringBuilder sb = new StringBuilder();

                        if (proyectos.isPresent()) {
                            sb.append("{\"proyecto\":");
                            String str = proyectos.get();
                            sb.append(str);
                            sb.append(",");
                        }

                        Optional<String> trabajadores = util.convertToJSON(trabajadorListOpt);
                        if (trabajadores.isPresent()) {
                            sb.append("\"trabajador\":");
                            String str = trabajadores.get();
                            sb.append(str);
                            sb.append("}");
                        }
                        result = Optional.of(sb.toString());
                    }else{
                        System.err.println("No se han encontrado datos.");
                        result = Optional.empty();
                    }
                    break;
                case 2:
                    //Listado de issues abiertas por proyecto que no se hayan consolidado en commits.
                    Optional<List<IssueDTO>> issueOpt = MoreOptionController.getInstance().getIssuesOpenAndNotInCommit();
                    result = util.convertToJSON(issueOpt);
                    break;
                case 3:
                    //Programadores de un proyecto ordenados por número de commits.
                    Optional<List<ProgramadorDTO>> programadorOpt = MoreOptionController.getInstance()
                            .getAllProgramadorCommitOrder(new ObjectId(scanner.scannerString("Introduce la ID del proyecto")));
                    result = util.convertToJSON(programadorOpt);
                    break;
                case 4:
                    //Programadores con su productividad completa: Proyectos en los que participa,
                    // commits e issues asignadas.
                    Optional<List<ProductividadProgramadorDTO>> productividadProgramadorOpt =
                            MoreOptionController.getInstance().getAllProgramadorProductividad();
                    result = util.convertToJSON(productividadProgramadorOpt);
                    break;
                case 5:
                    //Tres proyectos más caros en base a su presupuesto asignado y el salario
                    // de cada trabajador que participa.
                    Optional<List<ProyectoDTO>> proyectoOpt =
                            MoreOptionController.getInstance().getThreeExpensiveProjects();
                    result = util.convertToJSON(proyectoOpt);
                    break;
                case 6:
                    //Listado de proyectos con su información completa. Programadores que trabajan en él.
                    //Repositorio asociado con su listado de issues y de commits.
                    Optional<List<ProductividadProyectoDTO>> opt =
                            MoreOptionController.getInstance().getAllProyectoProductividad();
                    result = util.convertToJSON(opt);
                    break;
                case 7:
                    //Número de accesos de un programador e información de los mismos, es decir,
                    //"fecha en la que se produjo cada uno de ellos.
                    ObjectId idObj = new ObjectId(scanner.scannerString("Introduce la ID del programador a consultar"));
                    Optional<AccesosProgramadorDTO> optAccesos =
                            MoreOptionController.getInstance().getHistoricoCount(idObj);
                    result = util.convertToJSON(optAccesos);
                    break;
                default:
                    //Volver atrás
                    return Optional.empty();
            }

        }while(result.isEmpty());

        return result;
    }

    /**
     * Selecciona el tipo de operación CRUD a realizar
     * @param controller controlador que se va a utilizar en la operación CRUD
     * @return Optional que devuelve el resultado de la consulta
     */
    private Optional<String> selectCRUDType(BaseController controller) {
        //Selección de exporte
        Optional<String> result = Optional.empty();
        Scanner sc;
        int nSelected;

        do {
            sc = new Scanner(System.in);
            System.out.println("\n▶ ¿Qué tipo de acción deseas realizar?\n" +
                    "0 -> Volver atrás\n1 -> Obtener todos\n2 -> Obtener por ID\n3 -> Insertar" +
                    "\n4 -> Actualizar\n5 -> Eliminar\n");

            try {
                nSelected = sc.nextInt();

                //Evitar que se introduzca un número menor que 1 o mayor que 5
                if (nSelected < 0 || nSelected > 5) {
                    nSelected = -1;
                }

                Optional optional;
                switch(nSelected){
                    case 0:
                        //Volver atrás
                        result = Optional.empty();
                        break;
                    case 1:
                        //Obtener todos
                        optional = controller.getAll();
                        if (optional.isPresent()) {
                            result = util.convertToJSON(optional);
                        }
                        break;
                    case 2:
                        try{
                            String searchID = scanner.scannerString("Introduce la ID para obtener la entidad:");
                            optional = controller.getById(new ObjectId(searchID));
                            if (optional.isPresent()) {
                                result = util.convertToJSON(optional);
                            }

                            if (result.isEmpty()){
                                System.err.println("No se ha encontrado la entidad.");
                            }
                        } catch (InputMismatchException e) {
                            System.err.println("Error: no has introducido una ID válida.");
                            nSelected = -1;
                        }

                        break;
                    case 3:
                        //Insertar
                        Optional<BaseDTO> obj = createAndGetEntity(controller, false);
                        if (obj.isPresent()){
                            optional = controller.insert(obj.get());
                            if (optional.isPresent()) {
                                result = util.convertToJSON(optional);
                            }
                        }else{
                            System.err.println("Error: Datos introducidos no válidos.");
                            nSelected = -1;
                        }
                        break;
                    case 4:
                        //Actualizar
                        Optional<BaseDTO> obj2 = createAndGetEntity(controller, true);
                        if (obj2.isPresent()){
                            optional = controller.update(obj2.get());
                            if (optional.isPresent()) {
                                result = util.convertToJSON(optional);
                            }
                        }else{
                            System.err.println("Error: Datos introducidos no válidos.");
                            nSelected = -1;
                        }
                        break;
                    case 5:
                        //Eliminar
                        System.out.println("Introduce la ID de la entidad a eliminar:");
                        sc = new Scanner(System.in);

                        try{
                            String searchID = sc.next();
                            optional = controller.delete(new ObjectId(searchID));
                            if (optional.isPresent()) {
                                result = util.convertToJSON(optional);
                            }

                            if (result.isEmpty()){
                                System.err.println("No se ha encontrado la entidad.");
                            }
                        } catch (InputMismatchException e) {
                            System.err.println("Error: no has introducido una ID válida.");
                            nSelected = -1;
                        }
                        break;
                    default:
                        System.err.println("Error: Operación no encontrada.");
                        nSelected = -1;
                }

            } catch (InputMismatchException e) {
                System.err.println("Error: no has introducido un número válido.");
                nSelected = -1;
            }
        } while (nSelected == -1);

        return result;
    }

    /**
     * Crea y devuelve una entidad según el controlador pasado
     * @param controller Controlador
     * @param search ¿Buscar?
     * @return Optional del DTO
     */
    public Optional<BaseDTO> createAndGetEntity(BaseController controller, boolean search){
        Optional<BaseDTO> dto = Optional.empty();

        ObjectId id = new ObjectId();

        if (search){
            id = new ObjectId(scanner.scannerString("Introduce la ID sobre la entidad a modificar"));
        }

        if (controller instanceof CommitController){
            String title = scanner.scannerString("Introduce el título");
            String texto = scanner.scannerString("Introduce el texto");
            Date fecha = scanner.scannerDate("Introduce la fecha");

            String repositorioStr = scanner.scannerString("Introduce la ID del repositorio");
            Repositorio repositorio = (Repositorio) getDaoById(repositorioStr, new Repositorio(),
                    new RepositorioService(new RepositorioRepository()));

            String proyectoStr = scanner.scannerString("Introduce la ID del proyecto");
            Proyecto proyecto = (Proyecto) getDaoById(proyectoStr, new Proyecto(), new ProyectoService(new ProyectoRepository()));
            String autorCommitStr = scanner.scannerString("Introduce la ID del autor del commit");
            Programador autorCommit = (Programador) getDaoById(autorCommitStr, new Programador(), new ProgramadorService(new ProgramadorRepository()));
            String issueProcedenteStr = scanner.scannerString("Introduce la ID del issue procedente");
            Issue issueProcedente = (Issue) getDaoById(issueProcedenteStr, new Issue(), new IssueService(new IssueRepository()));

            dto = Optional.ofNullable(CommitDTO.builder()
                    .id(id)
                    .title(title)
                    .text(texto)
                    .date(fecha)
                    .repositorio(repositorio)
                    .proyecto(proyecto)
                    .autorCommit(autorCommit)
                    .issueProcedente(issueProcedente)
                    .build());
        }else if (controller instanceof DepartamentoController){
            String nombre = scanner.scannerString("Introduce el nombre");
            String jefeDepartamentoStr = scanner.scannerString("Introduce la UUID del jefe de departamento");
            Programador jefeDepartamento = (Programador) getDaoById(jefeDepartamentoStr, new Programador(),
                    new ProgramadorService(new ProgramadorRepository()));
            Set<Proyecto> proyectos = new HashSet<>();

            double presupuesto = scanner.scannerDouble("Introduce el presupuesto");
            double presupuestoAnual = scanner.scannerDouble("Introduce el presupuesto anual");

            dto = Optional.ofNullable(DepartamentoDTO.builder()
                    .id(id)
                    .nombre(nombre)
                    .jefeDepartamento(jefeDepartamento)
                    .presupuesto(presupuesto)
                    .presupuestoAnual(presupuestoAnual)
                    .proyectos(proyectos)
                    .build());
        }else if (controller instanceof FichaController){
            String programadorStr = scanner.scannerString("Introduce la UUID del programador");
            Programador programador = (Programador) getDaoById(programadorStr, new Programador(),
                    new ProgramadorService(new ProgramadorRepository()));
            String proyectoStr = scanner.scannerString("Introduce la UUID del proyecto");
            Proyecto proyecto = (Proyecto) getDaoById(proyectoStr, new Proyecto(),
                    new ProyectoService(new ProyectoRepository()));

            dto = Optional.ofNullable(FichaDTO.builder()
                    .id(id)
                    .programador(programador)
                    .proyecto(proyecto)
                    .build());
        } else if (controller instanceof IssueController){
            String title = scanner.scannerString("Introduce el título");
            String texto = scanner.scannerString("Introduce el texto");
            Date fecha = scanner.scannerDate("Introduce la fecha");

            String proyectoStr = scanner.scannerString("Introduce la UUID del proyecto");
            Proyecto proyecto = (Proyecto) getDaoById(proyectoStr, new Proyecto(),
                    new ProyectoService(new ProyectoRepository()));
            String repositorioAsignadoStr = scanner.scannerString("Introduce la UUID del repositorio a asignar");
            Repositorio repositorioAsignado = (Repositorio) getDaoById(repositorioAsignadoStr, new Repositorio(),
                    new RepositorioService(new RepositorioRepository()));

            String commitStr = scanner.scannerString("Introduce la UUID del commit a asignar");
            Commit commit = (Commit) getDaoById(commitStr, new Commit(),
                    new CommitService(new CommitRepository()));

            Set<Tarea> tareas = new HashSet<>();

            boolean acabado = scanner.scannerBoolean("¿Issue cerrada?");

            dto = Optional.ofNullable(IssueDTO.builder()
                    .id(id)
                    .title(title)
                    .text(texto)
                    .fecha(fecha)
                    .proyecto(proyecto)
                    .repositorioAsignado(repositorioAsignado)
                    .commit(commit)
                    .esAcabado(acabado)
                    .tareas(tareas)
                    .build());
        }else if (controller instanceof ProgramadorController){
            String nombre = scanner.scannerString("Introduce el nombre");
            Date fechaAlta = scanner.scannerDate("Introduce la fecha de alta");

            String departamentoStr = scanner.scannerString("Introduce la UUID del departamento");
            Departamento departamento = (Departamento) getDaoById(departamentoStr, new Departamento(),
                    new DepartamentoService(new DepartamentoRepository()));

            String correo = scanner.scannerString("Introduce el correo");
            String password = scanner.scannerString("Introduce la contraseña");

            double salario = scanner.scannerDouble("Introduce el salario");
            boolean jefeDepartamento = scanner.scannerBoolean("¿Es jefe de departamento?");
            boolean jefeProyecto = scanner.scannerBoolean("¿Es jefe de proyecto?");
            boolean jefeActivo = scanner.scannerBoolean("¿Es jefe activo?");

            dto = Optional.ofNullable(ProgramadorDTO.builder()
                    .id(id)
                    .nombre(nombre)
                    .fechaAlta(fechaAlta)
                    .departamento(departamento)
                    .domainTechnologies(new ArrayList<>())
                    .salario(salario)
                    .esJefeDepartamento(jefeDepartamento)
                    .esJefeProyecto(jefeProyecto)
                    .esJefeActivo(jefeActivo)
                    .correo(correo)
                    .password(password)
                    .login(new Login())
                    .fichas(new HashSet<>())
                    .tareas(new HashSet<>())
                    .commits(new HashSet<>())
                    .build());
        }else if (controller instanceof ProyectoController){
            String jefeProyectoStr = scanner.scannerString("Introduce la UUID del jefe de proyecto");
            Programador jefeProyecto = (Programador) getDaoById(jefeProyectoStr, new Programador(),
                    new ProgramadorService(new ProgramadorRepository()));
            String repositorioStr = scanner.scannerString("Introduce la UUID del repositorio");
            Repositorio repositorio = (Repositorio) getDaoById(repositorioStr, new Repositorio(),
                    new RepositorioService(new RepositorioRepository()));
            String departamentoStr = scanner.scannerString("Introduce la UUID del departamento");
            Departamento departamento = (Departamento) getDaoById(departamentoStr, new Departamento(),
                    new DepartamentoService(new DepartamentoRepository()));

            String nombre = scanner.scannerString("Introduce el nombre");
            double presupuesto = scanner.scannerDouble("Introduce el presupuesto");
            Date fechaInicio = scanner.scannerDate("Introduce la fecha de inicio");
            boolean acabado = scanner.scannerBoolean("¿Poner proyecto como acabado?");

            dto = Optional.ofNullable(ProyectoDTO.builder()
                    .id(id)
                    .jefeProyecto(jefeProyecto)
                    .nombre(nombre)
                    .presupuesto(presupuesto)
                    .fechaInicio(fechaInicio)
                    .fechaFin(null)
                    .usedTechnologies(new ArrayList<>())
                    .repositorio(repositorio)
                    .esAcabado(acabado)
                    .departamento(departamento)
                    .fichas(new HashSet<>())
                    .commits(new HashSet<>())
                    .issues(new HashSet<>())
                    .build());
        }else if (controller instanceof RepositorioController){
            String nombre = scanner.scannerString("Introduce el nombre");
            Date fechaCreation = scanner.scannerDate("Introduce la fecha de creación");

            String proyectoStr = scanner.scannerString("Introduce la UUID del proyecto");
            Proyecto proyecto = (Proyecto) getDaoById(proyectoStr, new Proyecto(),
                    new ProyectoService(new ProyectoRepository()));

            dto = Optional.ofNullable(RepositorioDTO.builder()
                    .id(id)
                    .nombre(nombre)
                    .creationDate(fechaCreation)
                    .proyecto(proyecto)
                    .commits(new HashSet<>())
                    .issues(new HashSet<>())
                    .build());
        }else if (controller instanceof TareaController){
            String programadorStr = scanner.scannerString("Introduce la UUID del programador");
            Programador programador = (Programador) getDaoById(programadorStr, new Programador(),
                    new ProgramadorService(new ProgramadorRepository()));
            String issueStr = scanner.scannerString("Introduce la UUID del issue");
            Issue issue = (Issue) getDaoById(issueStr, new Issue(),
                    new IssueService(new IssueRepository()));

            dto = Optional.ofNullable(TareaDTO.builder()
                    .id(id)
                    .programador(programador)
                    .issue(issue)
                    .build());
        }else if (controller instanceof LoginController){

            String programadorStr = scanner.scannerString("Introduce la UUID del programador");
            Programador programador = (Programador) getDaoById(programadorStr, new Programador(),
                    new ProgramadorService(new ProgramadorRepository()));

            String token = Token.getInstance().generateToken("y124efr");
            dto = Optional.ofNullable(LoginDTO.builder()
                    .id(id)
                    .programador(programador)
                    .instante(LocalDateTime.now())
                    .token(token)
                    .accessHistory(new AccessHistory())
                    .conectado(false)
                    .build());
        }else if (controller instanceof AccessHistoryController) {
            String programadorStr = scanner.scannerString("Introduce la UUID del programador");

            dto = Optional.ofNullable(AccessHistoryDTO.builder()
                    .id(id)
                    .programador(new ObjectId(programadorStr))
                    .instante(LocalDateTime.now())
                    .logins(new HashSet<>())
                    .build());
        }

        return dto;
    }

    /**
     * Dada una id, un tipo de dao y un servicio, se devuelve el objeto DAO
     * @param id String ID
     * @param dao DAO
     * @param service Servicio
     * @return DAO
     */
    private BaseDAO getDaoById(String id, BaseDAO dao, BaseService service) {
        try {
            Optional<BaseDAO> opt = service.getById(new ObjectId(id));
            if (opt.isPresent()){
                dao = opt.get();
            }else{
                System.out.println("No se ha encontrado la entidad según la ID proporcionada. Se le asignará una nueva.");
            }
        } catch (Exception e) {
            System.out.println("No se ha encontrado la entidad según la ID proporcionada. Se le asignará una nueva.");
        }

        return dao;
    }
}