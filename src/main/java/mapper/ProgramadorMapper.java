package mapper;

import dao.Programador;
import dto.ProgramadorDTO;

public class ProgramadorMapper extends BaseMapper<Programador, ProgramadorDTO> {
    @Override
    public Programador fromDTO(ProgramadorDTO item) {
        Programador programador = new Programador();
        if (item.getId() != null) {
            programador.setId(item.getId());
        }
        programador.setNombre(item.getNombre());
        programador.setFechaAlta(item.getFechaAlta());
        programador.setDepartamento(item.getDepartamento());
        programador.setDominatedTechnologies(item.getDomainTechnologies());
        programador.setSalario(item.getSalario());
        programador.setEsJefeDepartamento(item.isEsJefeDepartamento());
        programador.setEsJefeProyecto(item.isEsJefeProyecto());
        programador.setEsJefeActivo(item.isEsJefeActivo());
        programador.setCorreo(item.getCorreo());
        programador.setPassword(item.getPassword());
        programador.setLogin(item.getLogin());
        programador.setFichas(item.getFichas());
        programador.setTareas(item.getTareas());
        programador.setCommits(item.getCommits());
        return programador;
    }

    @Override
    public ProgramadorDTO toDTO(Programador item) {
        return ProgramadorDTO.builder()
                .id(item.getId())
                .nombre(item.getNombre())
                .fechaAlta(item.getFechaAlta())
                .departamento(item.getDepartamento())
                .domainTechnologies(item.getDominatedTechnologies())
                .salario(item.getSalario())
                .esJefeDepartamento(item.isEsJefeDepartamento())
                .esJefeProyecto(item.isEsJefeProyecto())
                .esJefeActivo(item.isEsJefeActivo())
                .correo(item.getCorreo())
                .password(item.getPassword())
                .login(item.getLogin())
                .fichas(item.getFichas())
                .tareas(item.getTareas())
                .commits(item.getCommits())
                .build();
    }
}
