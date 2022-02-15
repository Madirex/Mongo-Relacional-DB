package service;

import dao.AccessHistory;
import dto.AccessHistoryDTO;
import mapper.AccessHistoryMapper;
import org.bson.types.ObjectId;
import repository.AccessHistoryRepository;

/**
 * Histórico de acceso - Servicio
 */
public class AccessHistoryService extends BaseService<AccessHistory, ObjectId, AccessHistoryRepository, AccessHistoryDTO, AccessHistoryMapper>{

    /**
     * Inyección de dependencias en constructor
     * @param repository repositorio
     */
    public AccessHistoryService(AccessHistoryRepository repository) {
        super(repository, new AccessHistoryMapper());
    }
}
