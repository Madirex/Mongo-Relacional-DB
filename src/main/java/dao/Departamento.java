package dao;

import lombok.AllArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@Table(name = "departamento")
public class Departamento  extends BaseDAO{
    private ObjectId id;
    private String nombre;
    @BsonProperty(value = "jefe_departamento")
    private Programador jefeDepartamento;
    private Double presupuesto;
    @BsonProperty(value = "presupuesto_anual")
    private Double presupuestoAnual;
    private Set<Proyecto> proyectos;

    public Departamento(String nombre, Programador jefeDepartamento, Double presupuesto, Double presupuestoAnual) {
        id = new ObjectId();
        this.nombre = nombre;
        this.jefeDepartamento = jefeDepartamento;
        this.presupuesto = presupuesto;
        this.presupuestoAnual = presupuestoAnual;
        proyectos = new HashSet<>();
    }

    public Departamento(ObjectId id, String nombre, Double presupuesto, Double presupuestoAnual) {
        this.id = id;
        this.nombre = nombre;
        this.presupuesto = presupuesto;
        this.presupuestoAnual = presupuestoAnual;
    }

    public Departamento() {
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

    @OneToOne
    //@JoinColumn(name = "jefeDepartamento", referencedColumnName = "id", nullable = false)
    public Programador getJefeDepartamento() {
        return jefeDepartamento;
    }

    public void setJefeDepartamento(Programador jefeDepartamento) {
        this.jefeDepartamento = jefeDepartamento;
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
    @Column(name = "presupuesto_anual", nullable = false)
    public Double getPresupuestoAnual() {
        return presupuestoAnual;
    }

    public void setPresupuestoAnual(Double presupuestoAnual) {
        this.presupuestoAnual = presupuestoAnual;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "departamento", cascade = CascadeType.REMOVE)
    public Set<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(Set<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    @Override
    public String toString() {
        return "Departamento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", presupuesto=" + presupuesto +
                ", presupuestoAnual=" + presupuestoAnual;
    }
}
