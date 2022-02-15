package mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase abstracta para las clases Mapper
 * @param <DAO> DAO
 * @param <DTO> DTO
 */
public abstract class BaseMapper <DAO, DTO>{

    /**
     * Dada una lista de DTO, devuelve una lista de DAO
     * @param items Lista de DTO
     * @return Lista de DAO
     */
    public List<DAO> fromDTO(List<DTO> items) {
        return items.stream().map(this::fromDTO).collect(Collectors.toList());
    }

    /**
     * Dado un DTO, devuelve un DAO
     * @param item DTO
     * @return DAO
     */
    public abstract DAO fromDTO(DTO item);

    /**
     * Dada una lista de DAO, devuelve una lista de DTO
     * @param items Lista de DAO
     * @return Lista de DTO
     */
    public List<DTO> toDTO(List<DAO> items) {
        return items.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Dado un DAO, devuelve un DTO
     * @param item DAO
     * @return DTO
     */
    public abstract DTO toDTO(DAO item);
}
