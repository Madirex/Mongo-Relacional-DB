package database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.NonNull;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import util.ApplicationProperties;
import util.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBController {
    private static MongoDBController controller;
    @NonNull
    private String serverUrl;
    @NonNull
    private String serverPort;
    @NonNull
    private String dataBaseName;
    @NonNull
    private String user;
    @NonNull
    private String password;
    @NonNull
    private String jdbcDriver;

    ConnectionString connectionString;
    CodecRegistry pojoCodecRegistry;
    CodecRegistry codecRegistry;
    MongoClientSettings clientSettings;

    MongoClient mongoClient;

    private MongoDBController() {
        ApplicationProperties properties = ApplicationProperties.getInstance();
        serverUrl = properties.readProperty("database.server.url");
        serverPort = properties.readProperty("database.server.port");
        dataBaseName = properties.readProperty("database.name");
        jdbcDriver = properties.readProperty("database.jdbc.driver");
        Dotenv dotenv = Dotenv.load();
        user = dotenv.get("DATABASE_USER");
        password = dotenv.get("DATABASE_PASSWORD");

        initConfig();
    }

    /**
     * Devuelve una instancia del controlador MongoDB
     * @return instancia del controlador MongoDB
     */
    public static MongoDBController getInstance() {
        if (controller == null) {
            controller = new MongoDBController();
        }
        return controller;
    }

    /**
     * Carga la configuración de acceso al servidor de Base de Datos
     */
    private void initConfig() {

        connectionString = new ConnectionString("mongodb://" + user + ":" + password + "@" + serverUrl + ":" +
                serverPort + "/" + dataBaseName + "?authSource=admin");

        pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
    }

    /**
     * Abre una conexión con la base de datos
     */
    public void open() {
        this.mongoClient = MongoClients.create(clientSettings);
    }

    /**
     * Cierra la conexión con la base de datos
     */
    public void close() {
        if (mongoClient != null) mongoClient.close();
    }

    /**
     * Devuelve el conjunto de Bases de datos Existentes
     * @return Conjunto de Bases de Datos existentes
     */
    public Optional<List<Document>> getDataBases() {
        return Optional.of(mongoClient.listDatabases().into(new ArrayList<>()));
    }

    /**
     * Elimina la base de datos pasada por parámetro
     * @param dataBaseName Nombre de la base de datos
     */
    public void removeDataBase(String dataBaseName) {
        MongoDatabase dataBase = mongoClient.getDatabase(dataBaseName);
        dataBase.drop();
    }

    /**
     * Elimina una colección de una base de datos
     * @param dataBaseName Nombre de la Base de Datos
     * @param collectionName Nombre de la Colección
     */
    public void removeCollection(@NonNull String dataBaseName, @NonNull String collectionName) {
        MongoDatabase dataBase = mongoClient.getDatabase(dataBaseName);
        dataBase.getCollection(collectionName).drop();
    }

    /**
     * Obtiene la colección consultada en base a los datos pasados por los parámetros
     * @param dataBaseName Nombre de la Base de Datos
     * @param collectionName Nombre de la Colección
     * @param aClass Class TDocument
     * @param <TDocument> TDocument
     * @return TDocument MongoCollection TDocument
     */
    public <TDocument> MongoCollection<TDocument> getCollection(@NonNull String dataBaseName,
                                                                @NonNull String collectionName,
                                                                @NonNull Class<TDocument> aClass) {
        MongoDatabase dataBase = mongoClient.getDatabase(dataBaseName);
        return dataBase.getCollection(collectionName, aClass);
    }

    /**
     * Inicializa los datos
     */
    public void initData() {
        this.removeDataBase(ApplicationProperties.getInstance().readProperty("database.name"));
        Data.getInstance().initData();
    }
}