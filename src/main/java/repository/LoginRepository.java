package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import dao.Login;
import database.MongoDBController;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.ApplicationProperties;
import util.Token;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class LoginRepository implements CrudRepository<Login, ObjectId> {
    private ApplicationProperties properties = ApplicationProperties.getInstance();

    @Override
    public Optional<List<Login>> findAll() throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Login> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "login", Login.class);
        List<Login> list = collection.find().into(new ArrayList<>());
        mongoController.close();
        return Optional.of(list);
    }

    @Override
    public Optional<Login> getById(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Login> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "login", Login.class);
        Login login = collection.find(eq("_id", id)).first();
        mongoController.close();
        if (login != null)
            return Optional.of(login);
        throw new SQLException("Error LoginRepository no se ha encontrado la ID " + id);
    }

    @Override
    public Optional<Login> insert(Login login) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Login> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "login", Login.class);
        try {
            login.setToken(Token.getInstance().generateToken(login.getId().toString())); //Asignar token
            collection.insertOne(login);
            return Optional.of(login);
        } catch (Exception e) {
            throw new SQLException("Error LoginRepository al insertar:" + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Login> update(Login login) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Login> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "login", Login.class);
        try {
            Document filtered = new Document("_id", login.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);

            return Optional.ofNullable(collection.findOneAndReplace(filtered, login, returnDoc));
        } catch (Exception e) {
            throw new SQLException("Error LoginRepository al actualizar con id: " +
                    login.getId() + ": " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Optional<Login> delete(ObjectId id) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Login> collection = mongoController.getCollection(properties.readProperty("database.name"),
                "login", Login.class);
        Document filtered = new Document("_id", id);
        Optional<Login> login = Optional.ofNullable(collection.findOneAndDelete(filtered));
        mongoController.close();
        return login;
    }
}
