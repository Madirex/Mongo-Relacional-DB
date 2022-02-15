package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import dao.AccessHistory;
import database.MongoDBController;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class AccessHistoryRepository implements CrudRepository<AccessHistory, ObjectId> {
    private ApplicationProperties properties = ApplicationProperties.getInstance();

    @Override
    public Optional<List<AccessHistory>> findAll() throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<AccessHistory> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "accessHistory", AccessHistory.class);
        List<AccessHistory> list = collection.find().into(new ArrayList<>());
        mongoController.close();
        return Optional.of(list);
    }

    @Override
    public Optional<AccessHistory> getById(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<AccessHistory> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "accessHistory", AccessHistory.class);
        AccessHistory accessHistory = collection.find(eq("_id", id)).first();
        mongoController.close();
        if (accessHistory != null)
            return Optional.of(accessHistory);
        throw new SQLException("Error AccessHistoryRepository no se ha encontrado la ID " + id);
    }

    @Override
    public Optional<AccessHistory> insert(AccessHistory accessHistory) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<AccessHistory> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "accessHistory", AccessHistory.class);
        try {
            collection.insertOne(accessHistory);
            return Optional.of(accessHistory);
        } catch (Exception e) {
            throw new SQLException("Error AccessHistoryRepository al insertar:" + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<AccessHistory> update(AccessHistory accessHistory) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<AccessHistory> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "accessHistory", AccessHistory.class);
        try {
            Document filtered = new Document("_id", accessHistory.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);

            return Optional.ofNullable(collection.findOneAndReplace(filtered, accessHistory, returnDoc));
        } catch (Exception e) {
            throw new SQLException("Error AccessHistoryRepository al actualizar con id: " +
                    accessHistory.getId() + ": " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<AccessHistory> delete(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<AccessHistory> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "accessHistory", AccessHistory.class);
        Document filtered = new Document("_id", id);
        Optional<AccessHistory> accessHistory = Optional.ofNullable(collection.findOneAndDelete(filtered));
        mongoController.close();
        return accessHistory;
    }
}
