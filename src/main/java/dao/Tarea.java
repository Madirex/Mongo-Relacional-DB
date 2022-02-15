package dao;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Table(name = "tarea")
public class Tarea extends BaseDAO{
    private ObjectId id;
    private Programador programador;
    private Issue issue;

    public Tarea(Programador programador, Issue issue) {
        id = new ObjectId();
        this.programador = programador;
        this.issue = issue;
    }

    public Tarea() {
        id = new ObjectId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @ManyToOne
    //@JoinColumn(name = "programador", referencedColumnName = "id", nullable = false)
    public Programador getProgramador() {
        return programador;
    }

    public void setProgramador(Programador programador) {
        this.programador = programador;
    }

    @ManyToOne
    //@JoinColumn(name = "issue", referencedColumnName = "id", nullable = false)
    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", programador=" + programador +
                ", issue=" + issue +
                '}';
    }
}
