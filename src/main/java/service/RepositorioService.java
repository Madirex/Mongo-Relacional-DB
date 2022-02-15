package service;

import dao.Repositorio;
import dto.RepositorioDTO;
import mapper.RepositorioMapper;
import org.bson.types.ObjectId;
import repository.RepositorioRepository;

/**
 * Repositorio - Servicio
 */
public class RepositorioService extends BaseService<Repositorio, ObjectId, RepositorioRepository, RepositorioDTO, RepositorioMapper>{

    /**
     * Inyección de dependencias en constructor
     * @param repository repositorio
     */
    public RepositorioService(RepositorioRepository repository) {
        super(repository, new RepositorioMapper());
    }
}
