package controller;

import service.BaseService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Clase abstracta de Base para los controladores
 * @param <DTO> DTO
 * @param <ID> ID
 * @param <SERVICE> Servicio
 */
public abstract class BaseController<DTO, ID, SERVICE extends BaseService> {
    private final SERVICE service;

    /**
     * Constructor con inyección de dependencias
     * @param service servicio
     */
    protected BaseController(SERVICE service) {
        this.service = service;
    }

    /**
     * Devuelve una lista de todos los DTO
     * @return Lista de DTO dentro de Optional
     */
    public Optional<List<DTO>> getAll() {
        try {
            return service.getAllDTO();
        } catch (SQLException e) {
            System.err.println("Error Controller en getAllDTO: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Dada una id pasada por parámetro, se obtiene el objeto DTO
     * @param id ID
     * @return Optional de DTO
     */
    public Optional<DTO> getById(ID id) {
        try {
            return service.getByIdDTO(id);
        } catch (SQLException e) {
            System.err.println("Error Controller en getDTOById: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Inserta un DTO pasado por parámetro y lo devuelve
     * @param dto DTO
     * @return Optional de DTO
     */
    public Optional<DTO> insert(DTO dto) {
        try {

            return service.insertDTO(dto);
        }catch (SQLException e) {
            System.err.println("Error Controller en insertDTO: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Actualiza un DTO pasado por parámetro y lo devuelve
     * @param dto DTO
     * @return Optional de DTO
     */
    public Optional<DTO> update(DTO dto) {
        try {
            return service.updateDTO(dto);
        } catch (SQLException e) {
            System.err.println("Error Controller en updateDTO: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Dada una id pasada por parámetro, se elimina de la base de datos y se devuelve el DTO
     * @param id ID
     * @return Optional de DTO
     */
    public Optional<DTO> delete(ID id) {
        try {
            return service.deleteDTO(id);
        } catch (SQLException e) {
            System.err.println("Error Controller en deleteDTO: " + e.getMessage());
            return Optional.empty();
        }
    }

}