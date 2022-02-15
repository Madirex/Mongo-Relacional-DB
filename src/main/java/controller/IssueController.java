package controller;

import dto.IssueDTO;
import org.bson.types.ObjectId;
import service.IssueService;

public class IssueController extends BaseController<IssueDTO, ObjectId, IssueService> {

    /**
     * Constructor con inyección de dependencias
     * @param service IssueService
     */
    public IssueController(IssueService service) {
        super(service);
    }
}