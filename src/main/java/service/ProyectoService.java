package service;

import dao.Proyecto;
import dto.ProyectoDTO;
import mapper.ProyectoMapper;
import org.bson.types.ObjectId;
import repository.ProyectoRepository;

/**
 * Proyecto - Servicio
 */
public class ProyectoService extends BaseService<Proyecto, ObjectId, ProyectoRepository, ProyectoDTO, ProyectoMapper>{

    /**
     * Inyección de dependencias en constructor
     * @param repository repositorio
     */
    public ProyectoService(ProyectoRepository repository) {
        super(repository, new ProyectoMapper());
    }
}
