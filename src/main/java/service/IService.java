package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para los servicios
 * @param <DTO> DTO
 * @param <ID> ID
 */
public interface IService<DTO, ID> {

    /**
     * Retorna todos los DTO buscados en la base de datos
     * @return Optional de Lista de DTO
     * @throws SQLException SqlException
     */
    Optional<List<DTO>> getAllDTO() throws SQLException;

    /**
     * Dada una ID pasada por parámetro, se busca en la base de datos y se devuelve un Optional de DTO
     * @param id ID
     * @return Optional de DTO
     * @throws SQLException SqlException
     */
    Optional<DTO> getByIdDTO(ID id) throws SQLException;

    /**
     * Dado un DTO pasado por parámetro, se inserta y devuelve un Optional de DTO
     * @param dto DTO
     * @return Optional de DTO
     * @throws SQLException SqlException
     */
    Optional<DTO> insertDTO(DTO dto) throws SQLException;

    /**
     * Dado un DTO pasado por parámetro, se busca y se actualiza. Devuelve un Optional de DTO
     * @param dto DTO
     * @return Optional de DTO
     * @throws SQLException SqlException
     */
    Optional<DTO> updateDTO(DTO dto) throws SQLException;

    /**
     * Dada una ID pasada por parámetro, se busca en la base de datos, se elimina y se devuelve un Optional de DTO
     * @param id ID
     * @return Optional de DTO
     * @throws SQLException SqlException
     */
    Optional<DTO> deleteDTO(ID id) throws SQLException;
}