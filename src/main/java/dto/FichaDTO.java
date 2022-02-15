package dto;

import dao.Programador;
import dao.Proyecto;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Builder
@Data
public class FichaDTO extends BaseDTO{

    private ObjectId id;
    private Programador programador;
    private Proyecto proyecto;

    @Override
    public String toString() {
        return "FichaDTO{" +
                "uuid_ficha=" + id +
                //", programador=" + programador +
                //", proyecto=" + proyecto +
                '}';
    }
}
