package service;

import dao.Commit;
import dto.CommitDTO;
import mapper.CommitMapper;
import org.bson.types.ObjectId;
import repository.CommitRepository;
import repository.ProyectoRepository;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Commit - Servicio
 */
public class CommitService extends BaseService<Commit, ObjectId, CommitRepository, CommitDTO, CommitMapper>{

    CommitMapper mapper = new CommitMapper();

    /**
     * Inyección de dependencias en constructor
     * @param repository repositorio
     */
    public CommitService(CommitRepository repository) {
        super(repository, new CommitMapper());
    }

    /**
     * Inserta un Commit dado el CommitDTO pasado por parámetro
     * Este método tiene restricciones: No podrá realizar ningún commit ningún programador que no esté en este proyecto
     * ni que no nazca de un issue.
     * @param dto DTO
     * @return Optional de CommitDTO
     * @throws SQLException SqlException
     */
    @Override
    public Optional<CommitDTO> insertDTO(CommitDTO dto) throws SQLException {
        AtomicBoolean continueInsert = new AtomicBoolean(false);
        ProyectoService proyectoService = new ProyectoService(new ProyectoRepository());

        try {
            if (dto.getIssueProcedente() != null && dto.getAutorCommit() != null
                    && dto.getProyecto() != null && dto.getProyecto().getId() != null) {
                proyectoService.getById(dto.getProyecto().getId()).ifPresent(p -> {
                    if (p.getDepartamento().getId().equals(dto.getAutorCommit().getDepartamento().getId())) {
                        continueInsert.set(true);
                    }
                });
            }
        }catch (NullPointerException e) {
            System.out.println("\nError: " + e.getMessage());
            continueInsert.set(false);
        }

        if (continueInsert.get()){
            return Optional.of(mapper.toDTO(this.insert(mapper.fromDTO(dto)).orElseThrow()));
        }else{
            System.err.println("No podrá realizar ningún commit ningún programador que no esté en este proyecto " +
                    "ni que no nazca de un issue.");
            return Optional.empty();
        }

    }
}
