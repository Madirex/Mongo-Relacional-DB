package controller;

import dto.ProgramadorDTO;
import org.bson.types.ObjectId;
import service.ProgramadorService;

public class ProgramadorController extends BaseController<ProgramadorDTO, ObjectId, ProgramadorService> {
    /**
     * Constructor con inyección de dependencias
     * @param service ProgramadorService
     */
    public ProgramadorController(ProgramadorService service) {
        super(service);
    }
}