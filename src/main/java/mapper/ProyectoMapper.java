package mapper;

import dao.Proyecto;
import dto.ProyectoDTO;

public class ProyectoMapper extends BaseMapper<Proyecto, ProyectoDTO> {
    @Override
    public Proyecto fromDTO(ProyectoDTO item) {
        Proyecto proyecto = new Proyecto();
        if (item.getId() != null) {
            proyecto.setId(item.getId());
        }
        proyecto.setJefeProyecto(item.getJefeProyecto());
        proyecto.setNombre(item.getNombre());
        proyecto.setPresupuesto(item.getPresupuesto());
        proyecto.setFechaInicio(item.getFechaInicio());
        proyecto.setFechaFin(item.getFechaFin());
        proyecto.setUsedTechnologies(item.getUsedTechnologies());
        proyecto.setRepositorio(item.getRepositorio());
        proyecto.setEsAcabado(item.isEsAcabado());
        proyecto.setDepartamento(item.getDepartamento());
        proyecto.setFichas(item.getFichas());
        proyecto.setCommits(item.getCommits());
        proyecto.setIssues(item.getIssues());
        return proyecto;
    }

    @Override
    public ProyectoDTO toDTO(Proyecto item) {
        return ProyectoDTO.builder()
                .id(item.getId())
                .jefeProyecto(item.getJefeProyecto())
                .nombre(item.getNombre())
                .presupuesto(item.getPresupuesto())
                .fechaInicio(item.getFechaInicio())
                .fechaFin(item.getFechaFin())
                .usedTechnologies(item.getUsedTechnologies())
                .repositorio(item.getRepositorio())
                .esAcabado(item.isEsAcabado())
                .departamento(item.getDepartamento())
                .fichas(item.getFichas())
                .commits(item.getCommits())
                .issues(item.getIssues())
                .build();
    }
}
