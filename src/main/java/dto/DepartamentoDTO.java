package dto;

import dao.Programador;
import dao.Proyecto;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Set;

@Builder
@Data
public class DepartamentoDTO extends BaseDTO{
    private ObjectId id;
    private String nombre;
    private Programador jefeDepartamento;
    private Double presupuesto;
    private Double presupuestoAnual;
    private Set<Proyecto> proyectos;

    @Override
    public String toString() {
        return "DepartamentoDTO{" +
                "uuid_departamento=" + id +
                ", nombre='" + nombre + '\'' +
                //", jefe_departamento=" + jefeDepartamento +
                ", presupuesto=" + presupuesto +
                ", presupuesto_anual=" + presupuestoAnual +
                //", proyectos=" + proyectos +
                '}';
    }
}
