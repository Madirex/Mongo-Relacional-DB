package service;

import dao.Issue;
import dao.Programador;
import dao.Proyecto;
import dto.*;
import org.bson.types.ObjectId;
import repository.*;
import dto.AccesosProgramadorDTO;
import dto.ProductividadProgramadorDTO;
import dto.ProductividadProyectoDTO;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * MoreOption - Servicio
 */
public class MoreOptionService {

    /**
     * Obtener de un departamento, los proyectos (información completa) asociados con sus datos completos.
     * @param id ObjectId
     * @return Lista de Proyecto en Optional
     * @throws SQLException SqlException
     */
    public Optional<List<Proyecto>> getDepartamentProjects(ObjectId id) throws SQLException {
        List<Proyecto> projectList = new ArrayList<>();
        DepartamentoService departamentoServ = new DepartamentoService(new DepartamentoRepository());
        Optional<DepartamentoDTO> depOpt = departamentoServ.getByIdDTO(id);
        depOpt.ifPresent(d -> projectList.addAll(d.getProyectos()));
        return Optional.of(projectList);
    }

    /**
     * Obtener de un departamento, los trabajadores (información completa) asociados con sus datos completos.
     * @param id ObjectId
     * @return Lista de ProgramadorDTO en Optional
     * @throws SQLException SqlException
     */
    public Optional<List<ProgramadorDTO>> getAllTrabajadoresAsociados(ObjectId id) throws SQLException {
        List<ProgramadorDTO> programmers = new ArrayList<>();

        ProgramadorService service = new ProgramadorService(new ProgramadorRepository());
        Optional<List<ProgramadorDTO>> programmersOpt = service.getAllDTO();

        programmersOpt.ifPresent(p -> p.forEach(e -> {
            if (e.getDepartamento().getId().equals(id)) {
                programmers.add(e);
            }
        }));

        return Optional.of(programmers);
    }

    /**
     * Listado de issues abiertas por proyecto que no se hayan consolidado en commits
     * @return Lista de IssueDTO en Optional
     * @throws SQLException SqlException
     */
    public Optional<List<IssueDTO>> getIssuesOpenAndNotInCommit() throws SQLException {
        IssueService issueService = new IssueService(new IssueRepository());
        Optional<List<IssueDTO>> opt = issueService.getAllDTO();
        List<IssueDTO> listIssues = new ArrayList<>();
        CommitService CommitServ = new CommitService(new CommitRepository());

        //Obtener Issues asociados
        opt.ifPresent(issueDTOS -> issueDTOS.forEach(pr -> {
            //Issues abiertas
            if (!pr.isEsAcabado()) {
                //Issues en commit
                try {
                    Optional<List<CommitDTO>> commits = CommitServ.getAllDTO();
                    commits.ifPresent(commitDTOS -> commitDTOS.forEach(c -> {
                        if (!c.getIssueProcedente().getId().equals(pr.getId())){
                            listIssues.add(pr);
                        }
                    }));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }));

        return Optional.of(listIssues);
    }

    /**
     * Programadores de un proyecto ordenados por número de commits
     * @param id ObjectId
     * @return Lista de ProgramadorDTO en Optional
     * @throws SQLException SqlException
     */
    public Optional<List<ProgramadorDTO>> getAllProgramadorCommitOrder(ObjectId id) throws SQLException {

        ProgramadorService programServ = new ProgramadorService(new ProgramadorRepository());
        ProyectoService proyectoService = new ProyectoService(new ProyectoRepository());
        Optional<ProyectoDTO> proyecto = proyectoService.getByIdDTO(id); //Proyecto
        Optional<List<ProgramadorDTO>> programList = programServ.getAllDTO();
        List<ProgramadorDTO> programadoresAsociados = new ArrayList<>();

        programList.ifPresent(programadorDTOS -> programadorDTOS.forEach(p -> {
            if (proyecto.isPresent()){
                if (p.getDepartamento() != null && proyecto.get().getDepartamento().getId().equals(p.getDepartamento().getId())){
                    programadoresAsociados.add(p);
                }
            }else{
                System.err.println("Error al obtener todos los programadores ordenador por número de commits.");
                System.exit(1);
            }
        }));

        return Optional.of(programadoresAsociados.stream()
                .sorted(Comparator.comparing(t -> Collections.frequency(programadoresAsociados, t)))
                .collect(Collectors.toList()));
    }

    /**
     * Programadores con su productividad completa: proyectos en los que participa, commits (información completa)
     * e issues asignadas (información completa)
     * @return Lista de ProductividadProgramadorDTO en Optional
     * @throws SQLException SqlException
     */
    public Optional<List<ProductividadProgramadorDTO>> getAllProgramadorProductividad() throws SQLException {
        ProgramadorService programServ = new ProgramadorService(new ProgramadorRepository());
        Optional<List<ProgramadorDTO>> programmers = programServ.getAllDTO();
        List<ProductividadProgramadorDTO> productividadProgramadorDTO = new ArrayList<>();

        programmers.ifPresent(programadorDTOS -> programadorDTOS.forEach(p -> {
            ProductividadProgramadorDTO productividad = new ProductividadProgramadorDTO();
            productividad.setProgramador(p);

            //Proyectos en los que participa
            List<Proyecto> projectList = new ArrayList<>();
            if (p.getFichas() != null) {
                p.getFichas().forEach(f -> projectList.add(f.getProyecto()));
            }
            productividad.setProyectos(projectList);

            //Commits
            productividad.setCommits(p.getCommits());

            //Issues
            List<Issue> issueList = new ArrayList<>();
            if (p.getTareas() != null) {
                p.getTareas().forEach(e -> issueList.add(e.getIssue()));
            }
            productividad.setIssues(issueList);

            productividadProgramadorDTO.add(productividad);
        }));

        return Optional.of(productividadProgramadorDTO);
    }

    /**
     * Obtener los tres proyectos más caros en base a su presupuesto asignado y el salario
     * de cada trabajador que participa
     * @return Lista de ProyectoDTO en Optional
     * @throws SQLException SqlException
     */
    public Optional<List<ProyectoDTO>> getThreeExpensiveProjects() throws SQLException {
        //Todos los proyectos
        ProyectoService proyectoService = new ProyectoService(new ProyectoRepository());
        Optional<List<ProyectoDTO>> proyectos = proyectoService.getAllDTO();
        Map<ProyectoDTO, Double> proyectosPresupuesto = new LinkedHashMap<>();
        proyectos.ifPresent(p -> p.forEach(pr -> {
            AtomicReference<Double> amount = new AtomicReference<>(pr.getPresupuesto());
            pr.getFichas().forEach(f -> {
                if (f.getProgramador() != null){
                    amount.updateAndGet(v -> f.getProgramador().getSalario());
                }
            });
            proyectosPresupuesto.put(pr,amount.get());
        }));

        //Ahora ordenarlos
        Map<ProyectoDTO, Double> sorted = proyectosPresupuesto.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        //Ahora buscar los 3 proyectos más caros y devolverlos
        List<ProyectoDTO> threeExpensiveProjects = new ArrayList<>();
        int limitSearch = 3;

        if (sorted.size() < limitSearch){
            limitSearch = sorted.size();
        }

        for (int n = 0; n < limitSearch; n++){
            threeExpensiveProjects.add((ProyectoDTO) sorted.keySet().toArray()[n]);
        }

        return Optional.of(threeExpensiveProjects);
    }

    /**
     * Listado de Proyectos con su información completa. Programadores que trabajan en él.
     * Repositorio asociado con su listado de issues y de commits.
     * @return Un Optional de una lista de ProductividadProyectoDTO
     * @throws SQLException SqlException
     */
    public Optional<List<ProductividadProyectoDTO>> getAllProyectoProductividad() throws SQLException {

        //Agregar proyectos
        ProyectoService proyectoService = new ProyectoService(new ProyectoRepository());
        Optional<List<ProyectoDTO>> proyectos = proyectoService.getAllDTO();
        List<ProductividadProyectoDTO> productividadProyectoDTO = new ArrayList<>();

        proyectos.ifPresent(ps -> ps.forEach(p -> {
            ProductividadProyectoDTO productividad = new ProductividadProyectoDTO();
            productividad.setProyecto(p);
            productividad.setCommits(p.getCommits());
            productividad.setIssues(p.getIssues());
            productividadProyectoDTO.add(productividad);
        }));

        //Agregar proyectos relacionados
        FichaService fichaService = new FichaService(new FichaRepository());
        Optional<List<FichaDTO>> fichas = fichaService.getAllDTO();

        fichas.ifPresent(fi -> fi.forEach(f -> productividadProyectoDTO.forEach(p -> {
            if (f.getProyecto() != null && p.getProyecto().getId().equals(f.getProyecto().getId())){
                    List<Programador> programadorList = p.getProgramadores();

                    if (programadorList == null) programadorList = new ArrayList<>();

                    if (f.getProyecto() != null){
                        programadorList.add(f.getProgramador());
                    }
                    p.setProgramadores(programadorList);
            }
        })));

        return Optional.of(productividadProyectoDTO);
    }

    /**
     * Número de accesos de un Programador e información de los mismos: fecha en la que se produjo cada uno de ellos.
     * @param id ObjectId
     * @return Optional de AccesosProgramadorDTO
     * @throws SQLException SqlException
     */
    public Optional<AccesosProgramadorDTO> getAccessHistoryCount(ObjectId id) throws SQLException {
            AccesosProgramadorDTO ap = new AccesosProgramadorDTO();
            AccessHistoryService serv = new AccessHistoryService(new AccessHistoryRepository());
            serv.getAllDTO().ifPresent(e -> e.forEach(
                    p -> {
                        if (p.getProgramador().equals(id)){
                            ap.setNum(p.getLogins().size());
                            ap.setLogins(p.getLogins());
                        }
                    }
            ));

            return Optional.of(ap);
    }
}
