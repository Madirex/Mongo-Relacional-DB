package dto;

import dao.Commit;
import dao.Issue;
import dao.Programador;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ProductividadProyectoDTO {
    private ProyectoDTO proyecto;
    private List<Programador> programadores;
    private Set<Commit> commits;
    private Set<Issue> issues;
}