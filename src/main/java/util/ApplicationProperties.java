package util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de acceso a ApplicationProperties
 */
public class ApplicationProperties {

    private static ApplicationProperties applicationPropertiesInstance;
    private final Properties properties;

    private ApplicationProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.ALL,ex,
                    () -> "IOException Ocurrido al leer el fichero de propiedades.");
        }
    }

    /**
     * Singleton
     * @return ApplicationProperties
     */
    public static ApplicationProperties getInstance() {
        if (applicationPropertiesInstance == null) {
            applicationPropertiesInstance = new ApplicationProperties();
        }
        return applicationPropertiesInstance;
    }

    /**
     * Lee la propiedad del properties según el keyname pasado por parámetro
     * @param keyName String
     * @return String (propiedad devuelta)
     */
    public String readProperty(String keyName) {
        return properties.getProperty(keyName, "No existe esa clave en el fichero de propiedades");
    }
}
