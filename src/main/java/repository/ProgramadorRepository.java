package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import dao.Programador;
import database.MongoDBController;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.ApplicationProperties;
import util.Cifrator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class ProgramadorRepository implements CrudRepository<Programador, ObjectId> {
    private ApplicationProperties properties = ApplicationProperties.getInstance();

    @Override
    public Optional<List<Programador>> findAll() throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Programador> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "programador", Programador.class);
        List<Programador> list = collection.find().into(new ArrayList<>());
        mongoController.close();
        return Optional.of(list);
    }

    @Override
    public Optional<Programador> getById(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Programador> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "programador", Programador.class);
        Programador programador = collection.find(eq("_id", id)).first();
        mongoController.close();
        if (programador != null)
            return Optional.of(programador);
        throw new SQLException("Error ProgramadorRepository no se ha encontrado la ID " + id);
    }

    /**
     * Dado un programador pasado por parámetro:
     * 1. Se cifra la contraseña del programador
     * 2. Se inserta en la base de datos
     * @param programador {@link Programador}
     * @return Optional de Programador
     * @throws SQLException excepción SQL
     */
    @Override
    public Optional<Programador> insert(Programador programador) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Programador> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "programador", Programador.class);
        try {
            programador.setPassword(Cifrator.SHA256(programador.getPassword()));
            collection.insertOne(programador);
            return Optional.of(programador);
        } catch (Exception e) {
            throw new SQLException("Error ProgramadorRepository al insertar:" + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    /**
     * Dado un programador pasado por parámetro:
     * 1. Se cifra la contraseña del programador
     * 2. Se actualiza en la base de datos
     * @param programador {@link Programador}
     * @return Optional de Programador
     * @throws SQLException excepción SQL
     */
    @Override
    public Optional<Programador> update(Programador programador) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Programador> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "programador", Programador.class);
        try {
            programador.setPassword(Cifrator.SHA256(programador.getPassword()));
            Document filtered = new Document("_id", programador.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);

            return Optional.ofNullable(collection.findOneAndReplace(filtered, programador, returnDoc));
        } catch (Exception e) {
            throw new SQLException("Error ProgramadorRepository al actualizar con id: " +
                    programador.getId() + ": " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Programador> delete(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Programador> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "programador", Programador.class);
        Document filtered = new Document("_id", id);
        Optional<Programador> programador = Optional.ofNullable(collection.findOneAndDelete(filtered));
        mongoController.close();
        return programador;
    }
}
