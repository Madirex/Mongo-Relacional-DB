package dao;

import lombok.AllArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@Table(name = "commit")
@NamedQuery(name = "Commit.findAll", query = "SELECT c FROM Commit c")
public class Commit extends BaseDAO{
    private ObjectId id;
    private String title;
    private String text;
    private Date date;
    private Repositorio repositorio;
    private Proyecto proyecto;
    @BsonProperty(value = "autor_commit")
    private Programador autorCommit;
    @BsonProperty(value = "issue_procediente")
    private Issue issueProcedente;

    public Commit(String title, String text, Date date, Repositorio repositorio, Proyecto proyecto, Programador autorCommit,
                  Issue issueProcedente) {
        this.id = new ObjectId();
        this.title = title;
        this.text = text;
        this.date = date;
        this.repositorio = repositorio;
        this.proyecto = proyecto;
        this.autorCommit = autorCommit;
        this.issueProcedente = issueProcedente;
    }

    public Commit(ObjectId id, String title, String text, Date date) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    /**
     * Constructor vacío
     */
    public Commit() {
        this.id = new ObjectId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Basic
    @Column(name = "titulo")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "texto")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "fecha")
    public Date getDate() {
        return date;
    }

    public void setDate(Date fecha) {
        this.date = fecha;
    }

    @ManyToOne
    //@JoinColumn(name = "repositorio", referencedColumnName = "id", nullable = false)
    public Repositorio getRepositorio() {
        return repositorio;
    }

    public void setRepositorio(Repositorio repositorio) {
        this.repositorio = repositorio;
    }

    @ManyToOne
    //@JoinColumn(name = "proyecto", referencedColumnName = "id", nullable = false)
    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    @ManyToOne
    //@JoinColumn(name = "programador", referencedColumnName = "id", nullable = false)
    public Programador getAutorCommit() {
        return autorCommit;
    }

    public void setAutorCommit(Programador autorCommit) {
        this.autorCommit = autorCommit;
    }

    @OneToOne
    //@JoinColumn(name = "issue", referencedColumnName = "id", nullable = false)
    public Issue getIssueProcedente() {
        return issueProcedente;
    }

    public void setIssueProcedente(Issue issueProcedient) {
        this.issueProcedente = issueProcedient;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
