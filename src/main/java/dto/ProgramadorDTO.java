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
public class ProgramadorDTO extends BaseDTO{

    private ObjectId id;
    private String nombre;
    private Date fechaAlta;
    private Departamento departamento;
    private List<String> domainTechnologies;
    private Double salario;
    private boolean esJefeDepartamento;
    private boolean esJefeProyecto;
    private boolean esJefeActivo;
    private String correo;
    private String password;
    private Login login;
    private Set<Ficha> fichas;
    private Set<Tarea> tareas;
    private Set<Commit> commits;

    @Override
    public String toString() {
        return "ProgramadorDTO{" +
                "uuid_programador=" + id +
                ", nombre='" + nombre + '\'' +
                ", fecha_alta=" + fechaAlta +
                ", domain_technologies='" + domainTechnologies + '\'' +
                ", salario=" + salario +
                ", es_jefe_departamento=" + esJefeDepartamento +
                ", es_jefe_proyecto=" + esJefeProyecto +
                ", es_jefe_activo=" + esJefeActivo +
                ", password='" + password + '\'' +
                ", correo='" + correo + '\'' +
                //", departamento=" + departamento +
                //", login=" + login +
                //", tareas=" + tareas +
                //", commits=" + commits +
                //", fichas=" + fichas +
                '}';
    }
}
