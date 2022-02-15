package dto;

import dao.Commit;
import dao.Proyecto;
import dao.Repositorio;
import dao.Tarea;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Set;

@Builder
@Data
public class IssueDTO extends BaseDTO{

    private ObjectId id;
    private String title;
    private String text;
    private Date fecha;
    private Proyecto proyecto;
    private Repositorio repositorioAsignado;
    private Commit commit;
    private boolean esAcabado;
    private Set<Tarea> tareas;

    @Override
    public String toString() {
        return "IssueDTO{" +
                "uuid_issue=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", fecha=" + fecha +
                ", es_acabado=" + esAcabado +
                //", proyecto=" + proyecto +
                //", repositorio=" + repositorioAsignado +
                //", commit=" + commit +
                //", tareas=" + tareas +
                '}';
    }
}
