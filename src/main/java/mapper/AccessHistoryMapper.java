package mapper;

import dao.AccessHistory;
import dto.AccessHistoryDTO;

public class AccessHistoryMapper extends BaseMapper<AccessHistory, AccessHistoryDTO> {
    @Override
    public AccessHistory fromDTO(AccessHistoryDTO item) {
        AccessHistory accessHistory = new AccessHistory();
        if (item.getId() != null) {
            accessHistory.setId(item.getId());
        }
        accessHistory.setProgramador(item.getProgramador());
        accessHistory.setInstante(item.getInstante());
        accessHistory.setLogins(item.getLogins());
        return accessHistory;
    }

    @Override
    public AccessHistoryDTO toDTO(AccessHistory item) {
        return AccessHistoryDTO.builder()
                .id(item.getId())
                .programador(item.getProgramador())
                .instante(item.getInstante())
                .logins(item.getLogins())
                .build();
    }
}
