package controller;

import dao.Proyecto;
import dto.IssueDTO;
import dto.ProgramadorDTO;
import dto.ProyectoDTO;
import org.bson.types.ObjectId;
import service.MoreOptionService;
import dto.AccesosProgramadorDTO;
import dto.ProductividadProgramadorDTO;
import dto.ProductividadProyectoDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MoreOptionController {
    private static MoreOptionController moreOptionControllerInstance;
    private MoreOptionService service = new MoreOptionService();

    /**
     * Constructor privado para Singleton
     */
    private MoreOptionController() {
    }

    /**
     * Singleton del controlador
     */
    public static MoreOptionController getInstance() {
        if (moreOptionControllerInstance == null) {
            moreOptionControllerInstance = new MoreOptionController();
        }
        return moreOptionControllerInstance;
    }

    /**
     * Obtener de un departamento, los proyectos (información completa) asociados con sus datos completos.
     * @param id ObjectId
     * @return Lista de Proyecto en Optional
     */
    public Optional<List<Proyecto>> getDepartamentProjects(ObjectId id) {
        try {
            return service.getDepartamentProjects(id);
        } catch (SQLException e) {
            System.err.println("Error en MoreOptionController: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Obtener de un departamento, los trabajadores (información completa) asociados con sus datos completos.
     * @param id ObjectId
     * @return Lista de ProgramadorDTO en Optional
     */
    public Optional<List<ProgramadorDTO>> getAllTrabajadoresAsociados(ObjectId id) {
        try {
            return service.getAllTrabajadoresAsociados(id);
        } catch (SQLException e) {
            System.err.println("Error en MoreOptionController: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Listado de issues abiertas por proyecto que no se hayan consolidado en commits
     * @return Lista de IssueDTO en Optional
     */
    public Optional<List<IssueDTO>> getIssuesOpenAndNotInCommit() {
        try {
            return service.getIssuesOpenAndNotInCommit();
        } catch (SQLException e) {
            System.err.println("Error en MoreOptionController: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Programadores de un proyecto ordenados por número de commits
     * @param id ObjectId
     * @return Lista de ProgramadorDTO en Optional
     */
    public Optional<List<ProgramadorDTO>> getAllProgramadorCommitOrder(ObjectId id) {
        try {
            return service.getAllProgramadorCommitOrder(id);
        } catch (SQLException e) {
            System.err.println("Error en MoreOptionController: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Programadores con su productividad completa: proyectos en los que participa, commits (información completa)
     * e issues asignadas (información completa)
     * @return Lista de ProductividadProgramadorDTO en Optional
     */
    public Optional<List<ProductividadProgramadorDTO>> getAllProgramadorProductividad() {
        try {
            return service.getAllProgramadorProductividad();
        } catch (SQLException e) {
            System.err.println("Error en MoreOptionController: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Obtener los tres proyectos más caros en base a su presupuesto asignado y el salario
     * de cada trabajador que participa
     * @return Lista de ProyectoDTO en Optional
     */
    public Optional<List<ProyectoDTO>> getThreeExpensiveProjects() {
        try {
            return service.getThreeExpensiveProjects();
        } catch (SQLException e) {
            System.err.println("Error en MoreOptionController: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Listado de Proyectos con su información completa. Programadores que trabajan en él.
     * Repositorio asociado con su listado de issues y de commits.
     * @return Un Optional de una lista de ProductividadProyectoDTO
     */
    public Optional<List<ProductividadProyectoDTO>> getAllProyectoProductividad() {
        try {
            return service.getAllProyectoProductividad();
        } catch (SQLException e) {
            System.err.println("Error en MoreOptionController: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Número de accesos de un Programador e información de los mismos: fecha en la que se produjo cada uno de ellos.
     * @param id ObjectId
     * @return Optional de AccesosProgramadorDTO
     */
    public Optional<AccesosProgramadorDTO> getHistoricoCount(ObjectId id) {
        try {
            return service.getAccessHistoryCount(id);
        } catch (SQLException e) {
            System.err.println("Error en MoreOptionController: " + e.getMessage());
            return Optional.empty();
        }
    }

}
