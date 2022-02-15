import util.ApplicationProperties;
import util.ExportData;
import util.UserScanner;

public class Empresa {
    public Empresa(String pathExport){
        ApplicationProperties properties = ApplicationProperties.getInstance();
        System.out.println("Bienvenid@s a " + properties.readProperty("app.company"));

        Menu menu = Menu.getInstance();
        menu.initData();

        boolean exitMenu = false;
        do {
            String result;
            try {
                result = menu.searchData();
                if (UserScanner.getInstance().scannerBoolean("\n¿Quieres exportar la consulta?")){
                    ExportData.getInstance().exportUserSelect(pathExport, result);
                }
                //exitMenu = UserScanner.getInstance().scannerBoolean("\n❌ ¿Cerrar programa?");
                exitMenu = true; //Por defecto, salir siempre del programa
            } catch (Exception e) {
                System.err.println("Error al tratar los datos: " + e.getMessage());
                e.printStackTrace();
            }
        }while(!exitMenu);

    }
}
