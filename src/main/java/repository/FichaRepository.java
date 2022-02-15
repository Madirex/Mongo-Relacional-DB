package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import dao.Ficha;
import database.MongoDBController;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class FichaRepository implements CrudRepository<Ficha, ObjectId> {

    private ApplicationProperties properties = ApplicationProperties.getInstance();

    @Override
    public Optional<List<Ficha>> findAll() throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Ficha> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "ficha", Ficha.class);
        List<Ficha> list = collection.find().into(new ArrayList<>());
        mongoController.close();
        return Optional.of(list);
    }

    @Override
    public Optional<Ficha> getById(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Ficha> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "ficha", Ficha.class);
        Ficha ficha = collection.find(eq("_id", id)).first();
        mongoController.close();
        if (ficha != null)
            return Optional.of(ficha);
        throw new SQLException("Error FichaRepository no se ha encontrado la ID " + id);
    }

    @Override
    public Optional<Ficha> insert(Ficha ficha) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Ficha> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "ficha", Ficha.class);
        try {
            collection.insertOne(ficha);
            return Optional.of(ficha);
        } catch (Exception e) {
            throw new SQLException("Error FichaRepository al insertar:" + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Ficha> update(Ficha ficha) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Ficha> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "ficha", Ficha.class);
        try {
            Document filtered = new Document("_id", ficha.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);

            return Optional.ofNullable(collection.findOneAndReplace(filtered, ficha, returnDoc));
        } catch (Exception e) {
            throw new SQLException("Error FichaRepository al actualizar con id: " +
                    ficha.getId() + ": " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Ficha> delete(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Ficha> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "ficha", Ficha.class);
        Document filtered = new Document("_id", id);
        Optional<Ficha> ficha = Optional.ofNullable(collection.findOneAndDelete(filtered));
        mongoController.close();
        return ficha;
    }
}
