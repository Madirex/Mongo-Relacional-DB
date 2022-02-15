package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import dao.Proyecto;
import database.MongoDBController;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class ProyectoRepository implements CrudRepository<Proyecto, ObjectId> {
    private ApplicationProperties properties = ApplicationProperties.getInstance();

    @Override
    public Optional<List<Proyecto>> findAll() throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Proyecto> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "proyecto", Proyecto.class);
        List<Proyecto> list = collection.find().into(new ArrayList<>());
        mongoController.close();
        return Optional.of(list);
    }

    @Override
    public Optional<Proyecto> getById(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Proyecto> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "proyecto", Proyecto.class);
        Proyecto proyecto = collection.find(eq("_id", id)).first();
        mongoController.close();
        if (proyecto != null)
            return Optional.of(proyecto);
        throw new SQLException("Error ProyectoRepository no se ha encontrado la ID " + id);
    }

    @Override
    public Optional<Proyecto> insert(Proyecto proyecto) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Proyecto> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "proyecto", Proyecto.class);
        try {
            collection.insertOne(proyecto);
            return Optional.of(proyecto);
        } catch (Exception e) {
            throw new SQLException("Error ProyectoRepository al insertar:" + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Proyecto> update(Proyecto proyecto) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Proyecto> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "proyecto", Proyecto.class);
        try {
            Document filtered = new Document("_id", proyecto.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);

            return Optional.ofNullable(collection.findOneAndReplace(filtered, proyecto, returnDoc));
        } catch (Exception e) {
            throw new SQLException("Error ProyectoRepository al actualizar con id: " +
                    proyecto.getId() + ": " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Proyecto> delete(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Proyecto> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "proyecto", Proyecto.class);
        Document filtered = new Document("_id", id);
        Optional<Proyecto> proyecto = Optional.ofNullable(collection.findOneAndDelete(filtered));
        mongoController.close();
        return proyecto;
    }
}
