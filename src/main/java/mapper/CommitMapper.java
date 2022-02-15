package mapper;

import dao.Commit;
import dto.CommitDTO;

public class CommitMapper extends BaseMapper<Commit, CommitDTO> {
    @Override
    public Commit fromDTO(CommitDTO item) {
        Commit commit = new Commit();
        if (item.getId() != null) {
            commit.setId(item.getId());
        }
        commit.setTitle(item.getTitle());
        commit.setText(item.getText());
        commit.setDate(item.getDate());
        commit.setRepositorio(item.getRepositorio());
        commit.setProyecto(item.getProyecto());
        commit.setAutorCommit(item.getAutorCommit());
        commit.setIssueProcedente(item.getIssueProcedente());
        return commit;
    }

    @Override
    public CommitDTO toDTO(Commit item) {
        return CommitDTO.builder()
                .id(item.getId())
                .title(item.getTitle())
                .text(item.getText())
                .date(item.getDate())
                .repositorio(item.getRepositorio())
                .proyecto(item.getProyecto())
                .autorCommit(item.getAutorCommit())
                .issueProcedente(item.getIssueProcedente())
                .build();
    }
}
