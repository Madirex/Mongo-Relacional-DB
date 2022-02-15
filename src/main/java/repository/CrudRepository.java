package repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para operaciones CRUD
 * @param <DAO> DAO
 * @param <ID> ID
 */
public interface CrudRepository<DAO, ID> {

    /**
     * Busca y devuelve una lista de DAO en Optional
     * @return Optional Lista de DAO
     * @throws SQLException SqlException
     */
    Optional<List<DAO>> findAll() throws SQLException;

    /**
     * Dada una ID pasada por parámetro, busca y devuelve el objeto DAO en un Optional
     * @param id ID
     * @return Optional DAO
     * @throws SQLException SqlException
     */
    Optional<DAO> getById(ID id) throws SQLException;

    /**
     * Dado un objeto DAO pasado por parámetro, se inserta y retorna el DAO en optional
     * @param dao DAO
     * @return Optional DAO
     * @throws SQLException SqlException
     */
    Optional<DAO> insert(DAO dao) throws SQLException;

    /**
     * Dado un objeto DAO pasado por parámetro, se actualiza y retorna el DAO en optional
     * @param dao DAO
     * @return Optional DAO
     * @throws SQLException SqlException
     */
    Optional<DAO> update(DAO dao) throws SQLException;

    /**
     * Dada una ID pasada por parámetro, busca, elimina y devuelve el objeto DAO en un Optional
     * @param id ID
     * @return Optional DAO
     * @throws SQLException SqlException
     */
    Optional<DAO> delete(ID id) throws SQLException;
}