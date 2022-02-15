package service;

import dao.Login;
import dto.LoginDTO;
import mapper.LoginMapper;
import org.bson.types.ObjectId;
import repository.LoginRepository;

/**
 * Login - Servicio
 */
public class LoginService extends BaseService<Login, ObjectId, LoginRepository, LoginDTO, LoginMapper>{

    /**
     * Inyección de dependencias en constructor
     * @param repository repositorio
     */
    public LoginService(LoginRepository repository) {
        super(repository, new LoginMapper());
    }
}
