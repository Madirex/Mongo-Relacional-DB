package dto;

import dao.Commit;
import dao.Issue;
import dao.Proyecto;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Set;

@Builder
@Data
public class RepositorioDTO extends BaseDTO{

    private ObjectId id;
    private String nombre;
    private Date creationDate;
    private Proyecto proyecto;
    private Set<Commit> commits;
    private Set<Issue> issues;

    @Override
    public String toString() {
        return "RepositorioDTO{" +
                "uuid_repositorio=" + id +
                ", nombre='" + nombre + '\'' +
                ", creation_date=" + creationDate +
                //", proyecto=" + proyecto +
                //", commits=" + commits +
                //", issues=" + issues +
                '}';
    }
}
