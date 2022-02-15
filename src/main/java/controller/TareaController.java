package controller;

import dto.TareaDTO;
import org.bson.types.ObjectId;
import service.TareaService;

public class TareaController extends BaseController<TareaDTO, ObjectId, TareaService> {

    /**
     * Constructor con inyección de dependencias
     * @param service TareaService
     */
    public TareaController(TareaService service) {
        super(service);
    }
}