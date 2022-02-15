package dto;

import dao.Commit;
import dao.Issue;
import dao.Proyecto;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ProductividadProgramadorDTO {
    private ProgramadorDTO programador;
    private List<Proyecto> proyectos;
    private Set<Commit> commits;
    private List<Issue> issues;
}