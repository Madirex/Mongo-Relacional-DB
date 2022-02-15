package mapper;

import dao.Departamento;
import dto.DepartamentoDTO;

public class DepartamentoMapper extends BaseMapper<Departamento, DepartamentoDTO> {
    @Override
    public Departamento fromDTO(DepartamentoDTO item) {
        Departamento departamento = new Departamento();
        if (item.getId() != null) {
            departamento.setId(item.getId());
        }
        departamento.setNombre(item.getNombre());
        departamento.setJefeDepartamento(item.getJefeDepartamento());
        departamento.setPresupuesto(item.getPresupuesto());
        departamento.setPresupuestoAnual(item.getPresupuestoAnual());
        departamento.setProyectos(item.getProyectos());
        return departamento;
    }

    @Override
    public DepartamentoDTO toDTO(Departamento item) {
        return DepartamentoDTO.builder()
                .id(item.getId())
                .nombre(item.getNombre())
                .jefeDepartamento(item.getJefeDepartamento())
                .presupuesto(item.getPresupuesto())
                .presupuestoAnual(item.getPresupuestoAnual())
                .proyectos(item.getProyectos())
                .build();
    }
}
