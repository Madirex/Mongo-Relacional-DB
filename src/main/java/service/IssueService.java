package service;

import dao.Issue;
import dto.IssueDTO;
import mapper.IssueMapper;
import org.bson.types.ObjectId;
import repository.IssueRepository;

/**
 * Issue - Servicio
 */
public class IssueService extends BaseService<Issue, ObjectId, IssueRepository, IssueDTO, IssueMapper>{

    /**
     * Inyección de dependencias en constructor
     * @param repository repositorio
     */
    public IssueService(IssueRepository repository) {
        super(repository, new IssueMapper());
    }
}
