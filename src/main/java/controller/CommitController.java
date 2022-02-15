package controller;

import dto.CommitDTO;
import org.bson.types.ObjectId;
import service.CommitService;

public class CommitController extends BaseController<CommitDTO, ObjectId, CommitService> {
    /**
     * Constructor con inyección de dependencias
     * @param service CommitService
     */
    public CommitController(CommitService service) {
        super(service);
    }
}