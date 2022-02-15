package controller;

import dto.DepartamentoDTO;
import org.bson.types.ObjectId;
import service.DepartamentoService;

public class DepartamentoController extends BaseController<DepartamentoDTO, ObjectId, DepartamentoService> {
    /**
     * Constructor con inyección de dependencias
     * @param service DepartamentoService
     */
    public DepartamentoController(DepartamentoService service) {
        super(service);
    }
}