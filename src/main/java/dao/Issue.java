package dao;

import lombok.AllArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad Issue
 */
@Entity
@AllArgsConstructor
@Table(name = "issue")
public class Issue extends BaseDAO{
    private ObjectId id;
    private String title;
    private String text;
    private Date fecha;
    private Proyecto proyecto;
    @BsonProperty(value = "repositorio_asignado")
    private Repositorio repositorioAsignado;
    private Commit commit;
    @BsonProperty(value = "es_acabado")
    private boolean acabado;
    private Set<Tarea> tareas;

    /**
     * Entidad issue
     * @param title título
     * @param text texto
     * @param fecha fecha
     * @param proyecto proyecto
     * @param repositorioAsignado repositorio asignado
     * @param commit commit
     * @param acabado ¿Acabado?
     */
    public Issue(String title, String text, Date fecha, Proyecto proyecto, Repositorio repositorioAsignado, Commit commit,
                 boolean acabado) {
        id = new ObjectId();
        this.title = title;
        this.text = text;
        this.fecha = fecha;
        this.proyecto = proyecto;
        this.repositorioAsignado = repositorioAsignado;
        this.commit = commit;
        this.acabado = acabado;
        tareas = new HashSet<>();
    }

    /**
     * Constructor de Issue
     * @param id id
     * @param title título
     * @param text texto
     * @param fecha fecha
     * @param acabado ¿Acabado?
     */
    public Issue(ObjectId id, String title, String text, Date fecha, boolean acabado) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.fecha = fecha;
        this.acabado = acabado;
    }

    public Issue() {
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
    @Column(name = "titulo", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Basic
    @Column(name = "texto", nullable = false)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Basic
    @Column(name = "fecha", nullable = false)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
    //@JoinColumn(name = "repositorio_asignado", referencedColumnName = "id", nullable = false)
    public Repositorio getRepositorioAsignado() {
        return repositorioAsignado;
    }

    public void setRepositorioAsignado(Repositorio repositorioAsignado) {
        this.repositorioAsignado = repositorioAsignado;
    }

    @OneToOne
    //@JoinColumn(name = "commit", referencedColumnName = "id", nullable = false)
    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public boolean isAcabado() {
        return acabado;
    }

    public void setAcabado(boolean acabado) {
        this.acabado = acabado;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "issue", cascade = CascadeType.REMOVE)
    public Set<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        this.tareas = tareas;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", titulo='" + title + '\'' +
                ", texto='" + text + '\'' +
                ", fecha=" + fecha +
                ", acabado=" + acabado +
                '}';
    }
}