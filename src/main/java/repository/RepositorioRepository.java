package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import dao.Repositorio;
import database.MongoDBController;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class RepositorioRepository implements CrudRepository<Repositorio, ObjectId> {
    private ApplicationProperties properties = ApplicationProperties.getInstance();

    @Override
    public Optional<List<Repositorio>> findAll() throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Repositorio> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "repositorio", Repositorio.class);
        List<Repositorio> list = collection.find().into(new ArrayList<>());
        mongoController.close();
        return Optional.of(list);
    }

    @Override
    public Optional<Repositorio> getById(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Repositorio> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "repositorio", Repositorio.class);
        Repositorio repositorio = collection.find(eq("_id", id)).first();
        mongoController.close();
        if (repositorio != null)
            return Optional.of(repositorio);
        throw new SQLException("Error RepositorioRepository no se ha encontrado la ID " + id);
    }

    @Override
    public Optional<Repositorio> insert(Repositorio repositorio) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Repositorio> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "repositorio", Repositorio.class);
        try {
            collection.insertOne(repositorio);
            return Optional.of(repositorio);
        } catch (Exception e) {
            throw new SQLException("Error RepositorioRepository al insertar:" + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Repositorio> update(Repositorio repositorio) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Repositorio> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "repositorio", Repositorio.class);
        try {
            Document filtered = new Document("_id", repositorio.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);

            return Optional.ofNullable(collection.findOneAndReplace(filtered, repositorio, returnDoc));
        } catch (Exception e) {
            throw new SQLException("Error RepositorioRepository al actualizar con id: " +
                    repositorio.getId() + ": " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Repositorio> delete(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Repositorio> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "repositorio", Repositorio.class);
        Document filtered = new Document("_id", id);
        Optional<Repositorio> repositorio = Optional.ofNullable(collection.findOneAndDelete(filtered));
        mongoController.close();
        return repositorio;
    }
}
