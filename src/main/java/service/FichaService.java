package service;

import dao.Ficha;
import dto.FichaDTO;
import mapper.FichaMapper;
import org.bson.types.ObjectId;
import repository.FichaRepository;

/**
 * Ficha - Servicio
 */
public class FichaService extends BaseService<Ficha, ObjectId, FichaRepository, FichaDTO, FichaMapper>{

    /**
     * Inyección de dependencias en constructor
     * @param repository repositorio
     */
    public FichaService(FichaRepository repository) {
        super(repository, new FichaMapper());
    }
}
