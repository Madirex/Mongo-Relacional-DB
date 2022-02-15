package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import dao.Issue;
import database.MongoDBController;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class IssueRepository implements CrudRepository<Issue, ObjectId> {
    private ApplicationProperties properties = ApplicationProperties.getInstance();

    @Override
    public Optional<List<Issue>> findAll() throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Issue> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "issue", Issue.class);
        List<Issue> list = collection.find().into(new ArrayList<>());
        mongoController.close();
        return Optional.of(list);
    }

    @Override
    public Optional<Issue> getById(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Issue> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "issue", Issue.class);
        Issue issue = collection.find(eq("_id", id)).first();
        mongoController.close();
        if (issue != null)
            return Optional.of(issue);
        throw new SQLException("Error IssueRepository no se ha encontrado la ID " + id);
    }

    @Override
    public Optional<Issue> insert(Issue issue) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Issue> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "issue", Issue.class);
        try {
            collection.insertOne(issue);
            return Optional.of(issue);
        } catch (Exception e) {
            throw new SQLException("Error IssueRepository al insertar:" + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Issue> update(Issue issue) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Issue> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "issue", Issue.class);
        try {
            Document filtered = new Document("_id", issue.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);

            return Optional.ofNullable(collection.findOneAndReplace(filtered, issue, returnDoc));
        } catch (Exception e) {
            throw new SQLException("Error IssueRepository al actualizar con id: " +
                    issue.getId() + ": " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Issue> delete(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Issue> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "issue", Issue.class);
        Document filtered = new Document("_id", id);
        Optional<Issue> issue = Optional.ofNullable(collection.findOneAndDelete(filtered));
        mongoController.close();
        return issue;
    }
}
