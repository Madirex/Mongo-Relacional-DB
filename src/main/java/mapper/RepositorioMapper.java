package mapper;

import dao.Repositorio;
import dto.RepositorioDTO;

public class RepositorioMapper extends BaseMapper<Repositorio, RepositorioDTO> {
    @Override
    public Repositorio fromDTO(RepositorioDTO item) {
        Repositorio repositorio = new Repositorio();
        if (item.getId() != null) {
            repositorio.setId(item.getId());
        }
        repositorio.setNombre(item.getNombre());
        repositorio.setCreationDate(item.getCreationDate());
        repositorio.setProyecto(item.getProyecto());
        repositorio.setCommits(item.getCommits());
        repositorio.setIssues(item.getIssues());
        return repositorio;
    }

    @Override
    public RepositorioDTO toDTO(Repositorio item) {
        return RepositorioDTO.builder()
                .id(item.getId())
                .nombre(item.getNombre())
                .creationDate(item.getCreationDate())
                .proyecto(item.getProyecto())
                .commits(item.getCommits())
                .issues(item.getIssues())
                .build();
    }
}
