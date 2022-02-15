package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import dao.Tarea;
import database.MongoDBController;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class TareaRepository implements CrudRepository<Tarea, ObjectId> {
    private ApplicationProperties properties = ApplicationProperties.getInstance();

    @Override
    public Optional<List<Tarea>> findAll() throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Tarea> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "tarea", Tarea.class);
        List<Tarea> list = collection.find().into(new ArrayList<>());
        mongoController.close();
        return Optional.of(list);
    }

    @Override
    public Optional<Tarea> getById(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Tarea> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "tarea", Tarea.class);
        Tarea tarea = collection.find(eq("_id", id)).first();
        mongoController.close();
        if (tarea != null)
            return Optional.of(tarea);
        throw new SQLException("Error TareaRepository no se ha encontrado la ID " + id);
    }

    @Override
    public Optional<Tarea> insert(Tarea tarea) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Tarea> collection = mongoController.getCollection(properties.readProperty("database.name"), "tarea",
                Tarea.class);
        try {
            collection.insertOne(tarea);
            return Optional.of(tarea);
        } catch (Exception e) {
            throw new SQLException("Error TareaRepository al insertar:" + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Tarea> update(Tarea tarea) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Tarea> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "tarea", Tarea.class);
        try {
            Document filtered = new Document("_id", tarea.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);

            return Optional.ofNullable(collection.findOneAndReplace(filtered, tarea, returnDoc));
        } catch (Exception e) {
            throw new SQLException("Error TareaRepository al actualizar con id: " +
                    tarea.getId() + ": " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Tarea> delete(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Tarea> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "tarea", Tarea.class);
        Document filtered = new Document("_id", id);
        Optional<Tarea> tarea = Optional.ofNullable(collection.findOneAndDelete(filtered));
        mongoController.close();
        return tarea;
    }
}
