package dao;

import lombok.AllArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@Table(name = "repositorio")
public class Repositorio extends BaseDAO{
    private ObjectId id;
    private String nombre;
    @BsonProperty(value = "fecha_creacion")
    private Date creationDate;
    private Proyecto proyecto;
    private Set<Commit> commits;
    private Set<Issue> issues;

    public Repositorio(String nombre, Date creationDate, Proyecto proyecto) {
        id = new ObjectId();
        this.nombre = nombre;
        this.creationDate = creationDate;
        this.proyecto = proyecto;
        commits = new HashSet<>();
        issues = new HashSet<>();
    }

    public Repositorio(ObjectId id, String nombre, Date creationDate) {
        this.id = id;
        this.nombre = nombre;
        this.creationDate = creationDate;
    }

    public Repositorio() {
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
    @Column(name = "nombre", nullable = false)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "fecha_creacion", nullable = false)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @OneToOne
    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "repositorio", cascade = CascadeType.REMOVE)
    public Set<Commit> getCommits() {
        return commits;
    }

    public void setCommits(Set<Commit> commits) {
        this.commits = commits;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "repositorioAsignado", cascade = CascadeType.REMOVE)
    public Set<Issue> getIssues() {
        return issues;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
    }

    @Override
    public String toString() {
        return "Repositorio{" +
                "id=" + id +
                ", nombre='" + nombre +
                ", creationDate=" + creationDate +
                '}';
    }
}