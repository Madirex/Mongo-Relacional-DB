package dao;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Table(name = "ficha")
public class Ficha  extends BaseDAO{
    private ObjectId id;
    private Programador programador;
    private Proyecto proyecto;

    public Ficha(Programador programador, Proyecto proyecto) {
        id = new ObjectId();
        this.programador = programador;
        this.proyecto = proyecto;
    }

    public Ficha() {
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

    @ManyToOne
    //@JoinColumn(name = "programador", referencedColumnName = "id", nullable = false)
    public Programador getProgramador() {
        return programador;
    }

    public void setProgramador(Programador programador) {
        this.programador = programador;
    }

    @ManyToOne
    //@JoinColumn(name = "proyecto", referencedColumnName = "id", nullable = false)
    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    @Override
    public String toString() {
        return "Ficha{" +
                "id=" + id +
                ", programador=" + programador +
                ", proyecto=" + proyecto +
                '}';
    }
}
