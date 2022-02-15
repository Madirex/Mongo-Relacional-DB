package controller;

import dto.AccessHistoryDTO;
import org.bson.types.ObjectId;
import service.AccessHistoryService;

public class AccessHistoryController extends BaseController<AccessHistoryDTO, ObjectId, AccessHistoryService> {

    /**
     * Constructor con inyección de dependencias
     * @param service AccessHistoryService
     */
    public AccessHistoryController(AccessHistoryService service) {
        super(service);
    }
}