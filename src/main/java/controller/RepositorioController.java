package controller;

import dto.RepositorioDTO;
import org.bson.types.ObjectId;
import service.RepositorioService;

public class RepositorioController extends BaseController<RepositorioDTO, ObjectId, RepositorioService> {

    /**
     * Constructor con inyección de dependencias
     * @param service RepositorioService
     */
    public RepositorioController(RepositorioService service) {
        super(service);
    }
}