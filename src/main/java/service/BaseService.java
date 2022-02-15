package service;

import lombok.RequiredArgsConstructor;
import mapper.BaseMapper;
import repository.CrudRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Clase abstracta de base para los servicios
 * @param <DAO> DAO
 * @param <ID> ID
 * @param <R> Repository
 * @param <DTO> DTO
 * @param <MAPPER> MAPPER
 */
@RequiredArgsConstructor
public abstract class BaseService<DAO, ID, R extends CrudRepository<DAO, ID>, DTO, MAPPER extends BaseMapper<DAO, DTO>>
        implements IService<DTO, ID>{

    private final MAPPER mapper;
    protected final R repository;

    /**
     * Constructor protegido
     * @param repository repositorio
     * @param mapper mapper
     */
    protected BaseService(R repository, MAPPER mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Busca y devuelve una lista de DAO en Optional
     * @return Optional de lista de DAO
     * @throws SQLException SqlException
     */
    public Optional<List<DAO>> findAll() throws SQLException {
        return repository.findAll();
    }

    /**
     * Busca en la base de datos por la id pasada por parámetro y devuelve el DAO en Optional
     * @param id ID
     * @return Optional de DAO
     * @throws SQLException SqlException
     */
    public Optional<DAO> getById(ID id) throws SQLException {
        return repository.getById(id);
    }

    /**
     * Inserta y devuelve un DAO en Optional
     * @param dao DAO
     * @return Optional de DAO
     * @throws SQLException SqlException
     */
    public Optional<DAO> insert(DAO dao) throws SQLException {
        return repository.insert(dao);
    }

    /**
     * Busca y actualiza un DAO passado por parámetro. Finalmente, se retorna el DAO en Optional
     * @param dao DAO
     * @return Optional de DAO
     * @throws SQLException SqlException
     */
    public Optional<DAO> update(DAO dao) throws SQLException {
        return repository.update(dao);
    }

    /**
     * Dada la ID pasada por parámetro, se busca y se elimina de la base de datos. Se retorna un Optional de DAO
     * @param id ID
     * @return Optional de DAO
     * @throws SQLException SqlException
     */
    public Optional<DAO> delete(ID id) throws SQLException {
        return repository.delete(id);
    }

    /**
     * Retorna todos los DTO buscados en la base de datos
     * @return Optional de lista de DTO
     * @throws SQLException SqlException
     */
    public Optional<List<DTO>> getAllDTO() throws SQLException {
        return Optional.of(mapper.toDTO(this.findAll().orElseThrow()));
    }

    /**
     * Dada una ID pasada por parámetro, se busca en la base de datos y se devuelve un Optional de DTO
     * @param id ID
     * @return Optional de DTO
     * @throws SQLException SqlException
     */
    public Optional<DTO> getByIdDTO(ID id) throws SQLException {
        return Optional.of(mapper.toDTO(this.getById(id).orElseThrow()));
    }

    /**
     * Dado un DTO pasado por parámetro, se inserta y devuelve un Optional de DTO
     * @param dto DTO
     * @return Optional de DTO
     * @throws SQLException SqlException
     */
    public Optional<DTO> insertDTO(DTO dto) throws SQLException {
        return Optional.of(mapper.toDTO(this.insert(mapper.fromDTO(dto)).orElseThrow()));
    }

    /**
     * Dado un DTO pasado por parámetro, se busca y se actualiza. Devuelve un Optional de DTO
     * @param dto DTO
     * @return Optional de DTO
     * @throws SQLException SqlException
     */
    public Optional<DTO> updateDTO(DTO dto) throws SQLException {
        return Optional.of(mapper.toDTO(this.update(mapper.fromDTO(dto)).orElseThrow()));
    }

    /**
     * Dada una ID pasada por parámetro, se busca en la base de datos, se elimina y se devuelve un Optional de DTO
     * @param id ID
     * @return Optional de DTO
     * @throws SQLException SqlException
     */
    public Optional<DTO> deleteDTO(ID id) throws SQLException {
        return Optional.of(mapper.toDTO(this.delete(id).orElseThrow()));
    }
}
