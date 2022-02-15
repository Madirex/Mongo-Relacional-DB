package controller;

import dto.LoginDTO;
import org.bson.types.ObjectId;
import service.LoginService;

public class LoginController extends BaseController<LoginDTO, ObjectId, LoginService> {

    /**
     * Constructor con inyección de dependencias
     * @param service LoginService
     */
    public LoginController(LoginService service) {
        super(service);
    }
}