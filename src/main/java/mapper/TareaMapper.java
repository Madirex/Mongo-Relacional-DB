package mapper;

import dao.Tarea;
import dto.TareaDTO;

public class TareaMapper extends BaseMapper<Tarea, TareaDTO> {
    @Override
    public Tarea fromDTO(TareaDTO item) {
        Tarea tarea = new Tarea();
        if (item.getId() != null) {
            tarea.setId(item.getId());
        }
        tarea.setProgramador(item.getProgramador());
        tarea.setIssue(item.getIssue());
        return tarea;
    }

    @Override
    public TareaDTO toDTO(Tarea item) {
        return TareaDTO.builder()
                .id(item.getId())
                .programador(item.getProgramador())
                .issue(item.getIssue())
                .build();
    }
}
