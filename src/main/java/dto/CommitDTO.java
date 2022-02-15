package dto;

import dao.Issue;
import dao.Programador;
import dao.Proyecto;
import dao.Repositorio;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

@Builder
@Data
public class CommitDTO extends BaseDTO{
    private ObjectId id;
    private String title;
    private String text;
    private Date date;
    private Repositorio repositorio;
    private Proyecto proyecto;
    private Programador autorCommit;
    private Issue issueProcedente;

    @Override
    public String toString() {
        return "CommitDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                //", repositorio=" + repositorio +
                //", proyecto=" + proyecto +
                //", autorCommit=" + autorCommit +
                //", issueProcedente=" + issueProcedente +
                '}';
    }
}
