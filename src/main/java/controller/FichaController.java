package controller;

import dto.FichaDTO;
import org.bson.types.ObjectId;
import service.FichaService;

public class FichaController extends BaseController<FichaDTO, ObjectId, FichaService> {
    /**
     * Constructor con inyección de dependencias
     * @param service FichaService
     */
    public FichaController(FichaService service) {
        super(service);
    }
}