package mapper;

import dao.Ficha;
import dto.FichaDTO;

public class FichaMapper extends BaseMapper<Ficha, FichaDTO> {
    @Override
    public Ficha fromDTO(FichaDTO item) {
        Ficha ficha = new Ficha();
        if (item.getId() != null) {
            ficha.setId(item.getId());
        }
        ficha.setProgramador(item.getProgramador());
        ficha.setProyecto(item.getProyecto());
        return ficha;
    }

    @Override
    public FichaDTO toDTO(Ficha item) {
        return FichaDTO.builder()
                .id(item.getId())
                .programador(item.getProgramador())
                .proyecto(item.getProyecto())
                .build();
    }
}
