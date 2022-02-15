package util;

import controller.*;
import dao.*;
import database.MongoDBController;
import dto.*;
import org.bson.types.ObjectId;
import repository.*;
import service.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Datos
 */
public class Data {

    private static Data instance;
    private Data() {
    }
    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    /**
     * Inicializar datos e insertar en la base de datos
     */
    public void initData() {
        MongoDBController mongo = MongoDBController.getInstance();
        mongo.open();

        //Fechas
        Date date = Date.valueOf("2020-10-06");
        Date date2 = Date.valueOf("2021-11-07");

        //IDs
        ObjectId commitid1 = new ObjectId();
        ObjectId commitid2 = new ObjectId();
        ObjectId departamentoid1 = new ObjectId();
        ObjectId departamentoid2 = new ObjectId();
        ObjectId fichaid1 = new ObjectId();
        ObjectId fichaid2 = new ObjectId();
        ObjectId fichaid3 = new ObjectId();
        ObjectId issueid1 = new ObjectId();
        ObjectId issueid2 = new ObjectId();
        ObjectId programadorid1 = new ObjectId();
        ObjectId programadorid2 = new ObjectId();
        ObjectId programadorid3 = new ObjectId();
        ObjectId proyectoid1 = new ObjectId();
        ObjectId proyectoid2 = new ObjectId();
        ObjectId repositorioid1 = new ObjectId();
        ObjectId repositorioid2 = new ObjectId();
        ObjectId tareaid1 = new ObjectId();
        ObjectId tareaid2 = new ObjectId();
        ObjectId loginid1 = new ObjectId();
        ObjectId accessHistoryid1 = new ObjectId();

        Set<Ficha> fichas = new HashSet<>();
        Set<Tarea> tareas = new HashSet<>();
        //COMMIT
        Commit commit1 = new Commit(commitid1,"primer commit", "commit inicial", date);
        Commit commit2 = new Commit(commitid2,"segundo commit", "commit segundo", date);
        Set<Commit> commits = new HashSet<>();
        commits.add(commit1);
        commits.add(commit2);
        //ISSUE
        Issue issue1 = new Issue(issueid1, "primer issue", "issue inicial", date,false);
        Issue issue2 = new Issue(issueid2, "segunda issue", "issue final", date,true);
        Set<Issue> issues = new HashSet<>();
        issues.add(issue1);
        issues.add(issue2);
        //TECNOLOGIAS
        List<String> technologies = new ArrayList<>();
        technologies.add("java");
        technologies.add("python");
        technologies.add("kotlin");
        //PROGRAMADORES
        Programador programador1 = new Programador(programadorid1, "paco", date, technologies, 10.8,
                true,false,true,"paco@o.com", "paco123");
        Programador programador2 = new Programador(programadorid2,"andres", date, technologies, 10.8,
                false,true,true,"andres@o.com", "andres123");
        Programador programador3 = new Programador( programadorid3, "javi", date, technologies, 10.8,
                true,false,false,"javi@o.com", "javi123");
        //PROYECTOS
        Proyecto proyecto1 = new Proyecto(proyectoid1, "proyecto 1", 10.0, date, date2, technologies, true);
        Proyecto proyecto2 = new Proyecto(proyectoid2,"proyecto 2", 70.0, date, date2, technologies, false);
        Set<Proyecto> proyectos = new HashSet<>();
        proyectos.add(proyecto1);
        proyectos.add(proyecto2);
        //DEPARTAMENTO
        Departamento departamento1 = new Departamento(departamentoid1,"primer departamento",10.0, 80.0);
        Departamento departamento2 = new Departamento(departamentoid2,"segundo departamento", 100.0, 800.0);
        //REPOSITORIO
        Repositorio repositorio1 = new Repositorio(repositorioid1,"primer Repositorio", date);
        Repositorio repositorio2 = new Repositorio(repositorioid2,"segundo Repositorio",date2);
        //FICHA
        Ficha ficha1 = new Ficha(fichaid1,programador1, proyecto1);
        Ficha ficha2 = new Ficha(fichaid2,programador2, proyecto2);
        Ficha ficha3 = new Ficha(fichaid3,programador3, proyecto2);
        fichas.add(ficha1);
        fichas.add(ficha2);
        fichas.add(ficha3);
        //TAREA
        Tarea tarea1 = new Tarea(tareaid1,programador1, issue1);
        Tarea tarea2 = new Tarea(tareaid2,programador2, issue2);
        tareas.add(tarea1);
        tareas.add(tarea2);
        String token = "98iuh43gi5yy6556tu4i5h4iu";
        LocalDateTime ahora = LocalDateTime.now();
        Login login = new Login(loginid1,programador1, ahora,token, null,true);
        Set<Login> logins = new HashSet<>();
        logins.add(login);

        //Clases DTO
        CommitDTO commit1DTO = CommitDTO.builder()
                .id(commitid1)
                .title("primer commit")
                .text("commit inicial")
                .date(date)
                .repositorio(repositorio1)
                .proyecto(proyecto1)
                .autorCommit(programador1)
                .issueProcedente(issue1)
                .build();
        CommitDTO commit2DTO = CommitDTO.builder()
                .id(commitid2)
                .title("segundo commit")
                .text("commit secundario")
                .date(date2)
                .repositorio(repositorio2)
                .proyecto(proyecto2)
                .autorCommit(programador2)
                .issueProcedente(issue2)
                .build();
        DepartamentoDTO departamento1DTO = DepartamentoDTO.builder()
                .id(departamentoid1)
                .nombre("primer departamento")
                .jefeDepartamento(programador1)
                .presupuesto(10.0)
                .presupuestoAnual(80.0)
                .proyectos(proyectos)
                .build();
        DepartamentoDTO departamento2DTO = DepartamentoDTO.builder()
                .id(departamentoid2)
                .nombre("segundo departamento")
                .jefeDepartamento(programador1)
                .presupuesto(100.0)
                .presupuestoAnual(800.0)
                .proyectos(proyectos)
                .build();
        FichaDTO ficha1DTO = FichaDTO.builder()
                .id(fichaid1)
                .programador(programador1)
                .proyecto(proyecto1)
                .build();
        FichaDTO ficha2DTO = FichaDTO.builder()
                .id(fichaid2)
                .programador(programador2)
                .proyecto(proyecto2)
                .build();
        IssueDTO issue1DTO = IssueDTO.builder()
                .id(issueid1)
                .title("primer issue")
                .text("issue inicial")
                .fecha(date)
                .proyecto(proyecto1)
                .repositorioAsignado(repositorio1)
                .commit(commit1)
                .esAcabado(false)
                .tareas(tareas)
                .build();
        IssueDTO issue2DTO = IssueDTO.builder()
                .id(issueid2)
                .title("segunda issue")
                .text("issue final")
                .fecha(date)
                .proyecto(proyecto1)
                .repositorioAsignado(repositorio1)
                .commit(commit1)
                .esAcabado(true)
                .tareas(tareas)
                .build();
        ProgramadorDTO programador1DTO = ProgramadorDTO.builder()
                .id(programadorid1)
                .nombre("paco")
                .fechaAlta(date)
                .departamento(departamento1)
                .domainTechnologies(technologies)
                .salario(10.8)
                .esJefeDepartamento(true)
                .esJefeProyecto(false)
                .esJefeActivo(true)
                .correo("paco@o.com")
                .password("paco123")
                .login(login)
                .fichas(fichas)
                .tareas(tareas)
                .commits(commits)
                .build();
        ProgramadorDTO programador2DTO = ProgramadorDTO.builder()
                .id(programadorid2)
                .nombre("andres")
                .fechaAlta(date)
                .departamento(departamento1)
                .domainTechnologies(technologies)
                .salario(10.8)
                .esJefeDepartamento(false)
                .esJefeProyecto(true)
                .esJefeActivo(true)
                .correo("andres@o.com")
                .password("andres123")
                .login(login)
                .fichas(fichas)
                .tareas(tareas)
                .commits(commits)
                .build();
        ProgramadorDTO programador3DTO = ProgramadorDTO.builder()
                .id(programadorid3)
                .nombre("javi")
                .fechaAlta(date)
                .departamento(departamento2)
                .domainTechnologies(technologies)
                .salario(10.8)
                .esJefeDepartamento(true)
                .esJefeProyecto(false)
                .esJefeActivo(true)
                .correo("javi@o.com")
                .password("javi123")
                .login(login)
                .fichas(fichas)
                .tareas(tareas)
                .commits(commits)
                .build();
        ProyectoDTO proyecto1DTO = ProyectoDTO.builder()
                .id(proyectoid1)
                .jefeProyecto(programador2)
                .nombre("proyecto 1")
                .presupuesto(10.0)
                .fechaInicio(date)
                .fechaFin(date2)
                .usedTechnologies(technologies)
                .repositorio(repositorio1)
                .esAcabado(true)
                .departamento(departamento1)
                .fichas(fichas)
                .commits(commits)
                .issues(issues)
                .build();
        ProyectoDTO proyecto2DTO = ProyectoDTO.builder()
                .id(proyectoid2)
                .jefeProyecto(programador2)
                .nombre("proyecto 2")
                .presupuesto(80.0)
                .fechaInicio(date)
                .fechaFin(date2)
                .usedTechnologies(technologies)
                .repositorio(repositorio1)
                .esAcabado(false)
                .departamento(departamento1)
                .fichas(fichas)
                .commits(commits)
                .issues(issues)
                .build();
        RepositorioDTO repositorio1DTO  = RepositorioDTO.builder()
                .id(repositorioid1)
                .nombre("primer repositorio")
                .creationDate(date)
                .proyecto(proyecto1)
                .commits(commits)
                .issues(issues)
                .build();
        RepositorioDTO repositorio2DTO  = RepositorioDTO.builder()
                .id(repositorioid2)
                .nombre("segundo repositorio")
                .creationDate(date2)
                .proyecto(proyecto2)
                .commits(commits)
                .issues(issues)
                .build();
        TareaDTO tarea1DTO  = TareaDTO.builder()
                .id(tareaid1)
                .programador(programador1)
                .issue(issue1)
                .build();
        AccessHistoryDTO accessHistoryDTO = AccessHistoryDTO.builder()
                .id(accessHistoryid1)
                .programador(new ObjectId(String.valueOf(programador1.getId())))
                .instante(LocalDateTime.now())
                .logins(logins)
                .build();
        LoginDTO login1DTO = LoginDTO.builder()
                .id(loginid1)
                .programador(programador1)
                .instante(LocalDateTime.now())
                .token(token)
                .accessHistory(new AccessHistory())
                .conectado(false)
                .build();

        //Contoladores
        CommitController commitController = new CommitController(new CommitService(new CommitRepository()));
        DepartamentoController departamentoController = new DepartamentoController(new DepartamentoService(new DepartamentoRepository()));
        FichaController fichaController = new FichaController(new FichaService(new FichaRepository()));
        AccessHistoryController accessHistoryController = new AccessHistoryController(new AccessHistoryService(new AccessHistoryRepository()));
        IssueController issueController = new IssueController(new IssueService(new IssueRepository()));
        LoginController loginController = new LoginController(new LoginService(new LoginRepository()));
        ProgramadorController programadorController = new ProgramadorController(new ProgramadorService(new ProgramadorRepository()));
        ProyectoController proyectoController = new ProyectoController(new ProyectoService(new ProyectoRepository()));
        RepositorioController repositorioController = new RepositorioController(new RepositorioService(new RepositorioRepository()));
        TareaController tareaController = new TareaController(new TareaService(new TareaRepository()));

        departamentoController.insert(departamento1DTO);
        departamentoController.insert(departamento2DTO);
        fichaController.insert(ficha1DTO);
        fichaController.insert(ficha2DTO);
        commitController.insert(commit1DTO);
        commitController.insert(commit2DTO);
        accessHistoryController.insert(accessHistoryDTO);
        issueController.insert(issue1DTO);
        issueController.insert(issue2DTO);
        loginController.insert(login1DTO);
        programadorController.insert(programador1DTO);
        programadorController.insert(programador2DTO);
        programadorController.insert(programador3DTO);
        proyectoController.insert(proyecto1DTO);
        proyectoController.insert(proyecto2DTO);
        repositorioController.insert(repositorio1DTO);
        repositorioController.insert(repositorio2DTO);
        tareaController.insert(tarea1DTO);

    }

}