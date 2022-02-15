package service;

import dao.Tarea;
import dto.TareaDTO;
import mapper.TareaMapper;
import org.bson.types.ObjectId;
import repository.TareaRepository;

/**
 * Tarea - Servicio
 */
public class TareaService extends BaseService<Tarea, ObjectId, TareaRepository, TareaDTO, TareaMapper>{

    /**
     * Inyección de dependencias en constructor
     * @param repository repositorio
     */
    public TareaService(TareaRepository repository) {
        super(repository, new TareaMapper());
    }
}
