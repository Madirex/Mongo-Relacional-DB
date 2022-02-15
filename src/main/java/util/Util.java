package util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Optional;

public class Util {

    private static Util utilInstance;

    private Util() {
    }

    public static Util getInstance() {
        if (utilInstance == null) {
            utilInstance = new Util();
        }
        return utilInstance;
    }

    /**
     * Convierte a JSON
     * @param optional Devuelve la conversión en un String dentro de un Optional
     * @return Optional de String convertido
     */
    public Optional<String> convertToJSON(Optional optional){
        ExclusionStrategy strategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }

            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return fieldAttributes.getName().startsWith("password"); //Excluir contraseña
            }
        };

        Gson prettyGson = new GsonBuilder()
                .setPrettyPrinting()
                .addSerializationExclusionStrategy(strategy)
                .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                .create();

        Optional opt = optional;

        if(opt.isPresent()){
            opt = Optional.ofNullable(prettyGson.toJson(opt.get()));
        }else{
            opt = Optional.empty();
        }

        return opt;
    }

}
