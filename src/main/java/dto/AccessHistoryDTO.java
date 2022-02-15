package dto;

import dao.Login;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
public class AccessHistoryDTO extends BaseDTO{

    private ObjectId id;
    private ObjectId programador;
    private LocalDateTime instante;
    private Set<Login> logins;

    @Override
    public String toString() {
        return "AccessHistoryDTO{" +
                "uuid=" + id +
                //"programador=" + programador +
                //", instante=" + instante +
                //", logins=" + logins +
                '}';
    }
}
