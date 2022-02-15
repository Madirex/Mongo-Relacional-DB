import java.util.Arrays;

/**
 * Clase aplicación
 */
public class App {

    /**
     * Main
     * @param args argumento de dirección donde se exportarán los datos
     */
    public static void main(String[] args) {
        if (Arrays.stream(args).findAny().isPresent()){
            new Empresa(args[0]);
        }else{
            System.err.println("Error: Falta agregar el argumento de la dirección del directorio donde se exportarán los datos." +
                    "\nUtiliza el siguiente formato:" +
                    "\njava -jar accessdata.jar <directorio>\n");
            System.exit(1);
        }
    }
}
