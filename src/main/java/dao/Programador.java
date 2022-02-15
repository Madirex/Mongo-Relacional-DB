package dao;

import lombok.AllArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import util.Cifrator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@Table(name = "programador")
public class Programador extends BaseDAO{
    private ObjectId id;
    private String nombre;
    @BsonProperty(value = "fecha_alta")
    private Date fechaAlta;
    private Departamento departamento;
    @BsonProperty(value = "domain_technologies")
    private List<String> dominatedTechnologies;
    private Double salario;
    @BsonProperty(value = "es_jefe_departamento")
    private boolean esJefeDepartamento;
    @BsonProperty(value = "es_jefe_proyecto")
    private boolean esJefeProyecto;
    @BsonProperty(value = "es_jefe_activo")
    private boolean esJefeActivo;
    private String correo;
    private String password;
    private Login login;
    private Set<Ficha> fichas;
    private Set<Tarea> tareas;
    private Set<Commit> commits;

    public Programador(String nombre, Date fechaAlta, Departamento departamento, List<String> dominatedTechnologies,
                       Double salario, boolean esJefeDepartamento, boolean esJefeProyecto, boolean esJefeActivo,
                       String correo, String password, Login login) {
        id = new ObjectId();
        this.nombre = nombre;
        this.fechaAlta = fechaAlta;
        this.departamento = departamento;
        this.dominatedTechnologies = dominatedTechnologies;
        this.salario = salario;
        this.esJefeDepartamento = esJefeDepartamento;
        this.esJefeProyecto = esJefeProyecto;
        this.esJefeActivo = esJefeActivo;
        this.correo = correo;
        this.password = password;
        this.login = login;
        fichas = new HashSet<>();
        tareas = new HashSet<>();
        commits = new HashSet<>();
    }

    public Programador(ObjectId id, String nombre, Date fechaAlta, List<String> dominatedTechnologies, Double salario,
                       boolean esJefeDepartamento, boolean esJefeProyecto, boolean esJefeActivo, String correo, String password) {
        this.id = id;
        this.nombre = nombre;
        this.fechaAlta = fechaAlta;
        this.dominatedTechnologies = dominatedTechnologies;
        this.salario = salario;
        this.esJefeDepartamento = esJefeDepartamento;
        this.esJefeProyecto = esJefeProyecto;
        this.esJefeActivo = esJefeActivo;
        this.correo = correo;
        this.password = password;
    }

    public Programador() {
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
    @Column(name = "fecha_alta", nullable = false)
    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "jefeDepartamento", cascade = CascadeType.REMOVE)
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @ElementCollection
    @Column(name = "domain_technologies", nullable = false)
    public List<String> getDominatedTechnologies() {
        return dominatedTechnologies;
    }

    public void setDominatedTechnologies(List<String> dominatedTechnologies) {
        this.dominatedTechnologies = dominatedTechnologies;
    }

    @Basic
    @Column(name = "salario", nullable = false)
    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    @Basic
    @Column(name = "es_jefe_departamento", nullable = false)
    public boolean isEsJefeDepartamento() {
        return esJefeDepartamento;
    }

    public void setEsJefeDepartamento(boolean esJefeDepartamento) {
        this.esJefeDepartamento = esJefeDepartamento;
    }

    @Basic
    @Column(name = "es_jefe_proyecto", nullable = false)
    public boolean isEsJefeProyecto() {
        return esJefeProyecto;
    }

    public void setEsJefeProyecto(boolean esJefeProyecto) {
        this.esJefeProyecto = esJefeProyecto;
    }

    @Basic
    @Column(name = "es_jefe_activo", nullable = false)
    public boolean isEsJefeActivo() {
        return esJefeActivo;
    }

    public void setEsJefeActivo(boolean esJefeActivo) {
        this.esJefeActivo = esJefeActivo;
    }

    @Basic
    @Column(name = "correo", nullable = false)
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Basic
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Cifrator.SHA256(password);
    }

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "programador", cascade = CascadeType.REMOVE)
    public Login getLogin() {return login;}
    public void setLogin(Login login) {this.login = login;}

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "programador", cascade = CascadeType.REMOVE)
    public Set<Ficha> getFichas() {
        return fichas;
    }

    public void setFichas(Set<Ficha> fichas) {
        this.fichas = fichas;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "programador", cascade = CascadeType.REMOVE)
    public Set<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        this.tareas = tareas;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "autorCommit", cascade = CascadeType.REMOVE)
    public Set<Commit> getCommits() {
        return commits;
    }

    public void setCommits(Set<Commit> commits) {
        this.commits = commits;
    }

    @Override
    public String toString() {
        return "Programador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", dominatedTechnologies='" + dominatedTechnologies + '\'' +
                ", salario=" + salario +
                ", esJefeDepartamento=" + esJefeDepartamento +
                ", esJefeProyecto=" + esJefeProyecto +
                ", esJefeActivo=" + esJefeActivo +
                ", correo='" + correo + '\'' +
                '}';
    }
}
