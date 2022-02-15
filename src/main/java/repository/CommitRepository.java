package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import dao.Commit;
import database.MongoDBController;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class CommitRepository implements CrudRepository<Commit, ObjectId> {
    private ApplicationProperties properties = ApplicationProperties.getInstance();

    @Override
    public Optional<List<Commit>> findAll() throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Commit> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "commit", Commit.class);
        List<Commit> list = collection.find().into(new ArrayList<>());
        mongoController.close();
        return Optional.of(list);
    }

    @Override
    public Optional<Commit> getById(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Commit> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "commit", Commit.class);
        Commit commit = collection.find(eq("_id", id)).first();
        mongoController.close();
        if (commit != null)
            return Optional.of(commit);
        throw new SQLException("Error CommitRepository no se ha encontrado la ID " + id);
    }

    @Override
    public Optional<Commit> insert(Commit commit) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Commit> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "commit", Commit.class);
        try {
            collection.insertOne(commit);
            return Optional.of(commit);
        } catch (Exception e) {
            throw new SQLException("Error CommitRepository al insertar:" + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Commit> update(Commit commit) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Commit> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "commit", Commit.class);
        try {
            Document filtered = new Document("_id", commit.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);

            return Optional.ofNullable(collection.findOneAndReplace(filtered, commit, returnDoc));
        } catch (Exception e) {
            throw new SQLException("Error CommitRepository al actualizar con id: " +
                    commit.getId());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Commit> delete(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Commit> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "commit", Commit.class);
        Document filtered = new Document("_id", id);
        Optional<Commit> commit = Optional.ofNullable(collection.findOneAndDelete(filtered));
        mongoController.close();
        return commit;
    }
}
