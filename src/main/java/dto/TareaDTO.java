package dto;

import dao.Issue;
import dao.Programador;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Builder
@Data
public class TareaDTO extends BaseDTO{

    private ObjectId id;
    private Programador programador;
    private Issue issue;

    @Override
    public String toString() {
        return "TareaDTO{" +
                "uuid_tarea=" + id +
                //", programador=" + programador +
                //", issue=" + issue +
                '}';
    }
}
