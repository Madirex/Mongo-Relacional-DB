package mapper;

import dao.Issue;
import dto.IssueDTO;

public class IssueMapper extends BaseMapper<Issue, IssueDTO> {
    @Override
    public Issue fromDTO(IssueDTO item) {
        Issue issue = new Issue();
        if (item.getId() != null) {
            issue.setId(item.getId());
        }
        issue.setTitle(item.getTitle());
        issue.setText(item.getText());
        issue.setFecha(item.getFecha());
        issue.setProyecto(item.getProyecto());
        issue.setRepositorioAsignado(item.getRepositorioAsignado());
        issue.setCommit(item.getCommit());
        issue.setAcabado(item.isEsAcabado());
        issue.setTareas(item.getTareas());
        return issue;
    }

    @Override
    public IssueDTO toDTO(Issue item) {
        return IssueDTO.builder()
                .id(item.getId())
                .title(item.getTitle())
                .text(item.getText())
                .fecha(item.getFecha())
                .proyecto(item.getProyecto())
                .repositorioAsignado(item.getRepositorioAsignado())
                .commit(item.getCommit())
                .esAcabado(item.isAcabado())
                .tareas(item.getTareas())
                .build();
    }
}
