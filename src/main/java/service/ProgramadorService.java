package service;

import dao.Programador;
import dto.ProgramadorDTO;
import mapper.ProgramadorMapper;
import org.bson.types.ObjectId;
import repository.ProgramadorRepository;

/**
 * Programador - Servicio
 */
public class ProgramadorService extends BaseService <Programador, ObjectId, ProgramadorRepository, ProgramadorDTO,
        ProgramadorMapper> {

    /**
     * Inyección de dependencias en constructor
     * @param repository repositorio
     */
    public ProgramadorService(ProgramadorRepository repository) {
        super(repository, new ProgramadorMapper());
    }
}
