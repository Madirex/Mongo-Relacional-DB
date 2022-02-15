package repository;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import dao.Departamento;
import database.MongoDBController;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.ApplicationProperties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class DepartamentoRepository implements CrudRepository<Departamento, ObjectId> {
    private ApplicationProperties properties = ApplicationProperties.getInstance();

    @Override
    public Optional<List<Departamento>> findAll() throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Departamento> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "departamento", Departamento.class);
        List<Departamento> list = collection.find().into(new ArrayList<>());
        mongoController.close();
        return Optional.of(list);
    }

    @Override
    public Optional<Departamento> getById(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Departamento> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "departamento", Departamento.class);
        Departamento departamento = collection.find(eq("_id", id)).first();
        mongoController.close();
        if (departamento != null)
            return Optional.of(departamento);
        throw new SQLException("Error DepartamentoRepository no se ha encontrado la ID " + id);
    }

    @Override
    public Optional<Departamento> insert(Departamento departamento) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Departamento> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "departamento", Departamento.class);
        try {
            collection.insertOne(departamento);
            return Optional.of(departamento);
        } catch (Exception e) {
            throw new SQLException("Error DepartamentoRepository al insertar:" + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Departamento> update(Departamento departamento) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Departamento> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "departamento", Departamento.class);
        try {
            Document filtered = new Document("_id", departamento.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);

            return Optional.ofNullable(collection.findOneAndReplace(filtered, departamento, returnDoc));
        } catch (Exception e) {
            throw new SQLException("Error DepartamentoRepository al actualizar con id: " +
                    departamento.getId() + ": " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Departamento> delete(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Departamento> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "departamento", Departamento.class);
        Document filtered = new Document("_id", id);
        Optional<Departamento> departamento = Optional.ofNullable(collection.findOneAndDelete(filtered));
        mongoController.close();
        return departamento;
    }
}
