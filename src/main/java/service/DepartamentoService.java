package service;

import dao.Departamento;
import dto.DepartamentoDTO;
import mapper.DepartamentoMapper;
import org.bson.types.ObjectId;
import repository.DepartamentoRepository;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Departamento - Servicio
 */
public class DepartamentoService extends BaseService<Departamento, ObjectId, DepartamentoRepository,
        DepartamentoDTO, DepartamentoMapper>{

    DepartamentoMapper mapper = new DepartamentoMapper();

    /**
     * Inyección de dependencias en constructor
     * @param repository repositorio
     */
    public DepartamentoService(DepartamentoRepository repository) {
        super(repository, new DepartamentoMapper());
    }

    /** Inserta un departamento dado un DepartamentoDTO pasado por parámetro
     * No se podrá insertar si el programador no es jefe de departamento o no se ha asignado correctamente.
     * @param departamentoDTO DepartamentoDTO
     * @return Devuelve un Optional de DepartamentoDTO
     * @throws SQLException SqlException
     */
    @Override
    public Optional<DepartamentoDTO> insertDTO(DepartamentoDTO departamentoDTO) throws SQLException {

        //Las issues las crea solamente el jefe del proyecto.
        boolean doInsert = false;

        if(departamentoDTO.getJefeDepartamento() != null){
            if (departamentoDTO.getJefeDepartamento().isEsJefeDepartamento()) {
                doInsert = true;
            }else{
                System.err.println("No se ha insertado el departamento porque el programador " +
                        "asignado no es un jefe de departamento");
            }
        }else{
            System.err.println("No se ha insertado el departamento porque no hay ningún jefe de departamento" +
                    " que haya sido asignado.");
        }

        if (doInsert){
            //addNewJefe(departamentoDTO); //Agregar a histórico de jefes
            return Optional.of(mapper.toDTO(this.insert(mapper.fromDTO(departamentoDTO)).orElseThrow()));
        }else{
            return Optional.empty();
        }
    }
}
