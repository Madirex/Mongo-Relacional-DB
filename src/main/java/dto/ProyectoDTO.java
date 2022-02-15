package dto;

import dao.*;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Builder
@Data
public class ProyectoDTO extends BaseDTO{

    private ObjectId id;
    private Programador jefeProyecto;
    private String nombre;
    private Double presupuesto;
    private Date fechaInicio;
    private Date fechaFin;
    private List<String> usedTechnologies;
    private Repositorio repositorio;
    private boolean esAcabado;
    private Departamento departamento;
    private Set<Ficha> fichas;
    private Set<Commit> commits;
    private Set<Issue> issues;


    @Override
    public String toString() {
        return "ProyectoDTO{" +
                "uuid_proyecto=" + id +
                //", jefe_proyecto=" + jefeProyecto +
                ", nombre='" + nombre + '\'' +
                ", presupuesto=" + presupuesto +
                ", fecha_inicio=" + fechaInicio +
                ", fecha_fin=" + fechaFin +
                ", used_technologies='" + usedTechnologies + '\'' +
                ", es_acabado=" + esAcabado +
                //", departamento=" + departamento +
                //", repositorio=" + repositorio +
                //", issues=" + issues +
                //", commits=" + commits +
                //", fichas=" + fichas +
                '}';
    }
}
