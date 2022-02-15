package dto;

import dao.AccessHistory;
import dao.Programador;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Builder
@Data
public class LoginDTO extends BaseDTO{

    private ObjectId id;
    private Programador programador;
    private LocalDateTime instante;
    private String token;
    private AccessHistory accessHistory;
    private boolean conectado;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "uuid_login=" + id +
                ", instante=" + instante +
                ", conectado=" + conectado +
                ", uuid_usuario=" + programador +
                ", token='" + token + '\'' +
                //", accessHistory=" + accessHistory +
                '}';
    }
}
