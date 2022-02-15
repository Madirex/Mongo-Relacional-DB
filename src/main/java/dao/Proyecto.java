package dao;

import lombok.AllArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@Table(name = "proyecto")
public class Proyecto extends BaseDAO{
    private ObjectId id;
    @BsonProperty(value = "jefe_proyecto")
    private Programador jefeProyecto;
    private String nombre;
    private Double presupuesto;
    @BsonProperty(value = "fecha_inicio")
    private Date fechaInicio;
    @BsonProperty(value = "fecha_fin")
    private Date fechaFin;
    @BsonProperty(value = "used_technologies")
    private List<String> usedTechnologies;
    private Repositorio repositorio;
    @BsonProperty(value = "es_acabado")
    private boolean esAcabado;
    private Departamento departamento;
    private Set<Ficha> fichas;
    private Set<Commit> commits;
    private Set<Issue> issues;

    public  Proyecto(Programador jefeProyecto, String nombre, Double presupuesto, Date fechaInicio, Date fechaFin,
                     List<String> usedTechnologies, Repositorio repositorio, boolean esAcabado, Departamento departamento) {
        id = new ObjectId();
        this.jefeProyecto = jefeProyecto;
        this.nombre = nombre;
        this.presupuesto = presupuesto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.usedTechnologies = usedTechnologies;
        this.repositorio = repositorio;
        this.esAcabado = esAcabado;
        this.departamento = departamento;
        fichas = new HashSet<>();
        commits = new HashSet<>();
        issues = new HashSet<>();
    }

    public Proyecto(ObjectId id, String nombre, Double presupuesto, Date fechaInicio, Date fechaFin,
                    List<String> usedTechnologies, boolean esAcabado) {
        this.id = id;
        this.nombre = nombre;
        this.presupuesto = presupuesto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.usedTechnologies = usedTechnologies;
        this.esAcabado = esAcabado;
    }

    public Proyecto() {
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

    @OneToOne
    //@JoinColumn(name = "jefe_proyecto", referencedColumnName = "id", nullable = false)
    public Programador getJefeProyecto() {
        return jefeProyecto;
    }

    public void setJefeProyecto(Programador jefeProyecto) {
        this.jefeProyecto = jefeProyecto;
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
    @Column(name = "presupuesto", nullable = false)
    public Double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Double presupuesto) {
        this.presupuesto = presupuesto;
    }

    @Basic
    @Column(name = "fecha_inicio", nullable = false)
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    @Basic
    @Column(name = "fecha_fin", nullable = false)
    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @ElementCollection
    @Column(name = "used_technologies", nullable = false)
    public List<String> getUsedTechnologies() {
        return usedTechnologies;
    }

    public void setUsedTechnologies(List<String> usedTechnologies) {
        this.usedTechnologies = usedTechnologies;
    }

    @OneToOne
    //@JoinColumn(name = "repositorio", referencedColumnName = "id", nullable = false)
    public Repositorio getRepositorio() {
        return repositorio;
    }

    public void setRepositorio(Repositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Basic
    @Column(name = "es_acabado", nullable = false)
    public boolean isEsAcabado() {
        return esAcabado;
    }

    public void setEsAcabado(boolean esAcabado) {
        this.esAcabado = esAcabado;
    }

    @ManyToOne
    //@JoinColumn(name = "departamento", referencedColumnName = "id", nullable = false)
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "proyecto", cascade = CascadeType.REMOVE)
    public Set<Ficha> getFichas() {
        return fichas;
    }

    public void setFichas(Set<Ficha> fichas) {
        this.fichas = fichas;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "proyecto", cascade = CascadeType.REMOVE)
    public Set<Commit> getCommits() {
        return commits;
    }

    public void setCommits(Set<Commit> commits) {
        this.commits = commits;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "proyecto", cascade = CascadeType.REMOVE)
    public Set<Issue> getIssues() {
        return issues;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", presupuesto=" + presupuesto +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", usedTechnologies='" + usedTechnologies + '\'' +
                ", esAcabado=" + esAcabado +
                '}';
    }
}
