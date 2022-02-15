package mapper;

import dao.Login;
import dto.LoginDTO;

public class LoginMapper extends BaseMapper<Login, LoginDTO> {
    @Override
    public Login fromDTO(LoginDTO item) {
        Login login = new Login();
        if (item.getId() != null) {
            login.setId(item.getId());
        }
        login.setProgramador(item.getProgramador());
        login.setInstante(item.getInstante());
        login.setToken(item.getToken());
        login.setAccessHistory(item.getAccessHistory());
        login.setConectado(item.isConectado());
        return login;
    }

    @Override
    public LoginDTO toDTO(Login item) {
        return LoginDTO.builder()
                .id(item.getId())
                .programador(item.getProgramador())
                .instante(item.getInstante())
                .token(item.getToken())
                .accessHistory(item.getAccessHistory())
                .conectado(item.isConectado())
                .build();
    }
}
