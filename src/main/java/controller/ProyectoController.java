package controller;

import dto.ProyectoDTO;
import org.bson.types.ObjectId;
import service.ProyectoService;

public class ProyectoController extends BaseController<ProyectoDTO, ObjectId, ProyectoService> {

    /**
     * Constructor con inyección de dependencias
     * @param service ProyectoService
     */
    public ProyectoController(ProyectoService service) {
        super(service);
    }
}