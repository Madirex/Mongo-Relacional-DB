package util;

import com.github.underscore.lodash.U;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class ExportData {

    private static ExportData exportDataInstance;

    private ExportData() {
    }

    public static ExportData getInstance() {
        if (exportDataInstance == null) {
            exportDataInstance = new ExportData();
        }
        return exportDataInstance;
    }

    /**
     * Exporta los datos según los booleans
     * @param toJSON ¿Exportar a JSON?
     * @param toXML ¿Exportar a XML?
     * @param path Dirección de exportación
     * @param export Contenido a exportar
     */
    private void exportData(boolean toJSON, boolean toXML, String path, String export) {
        if (toJSON) {
            saveFile(path, export, "json");
        }

        if (toXML){
            saveFile(path, export, "xml");
        }
    }

    /**
     * Guarda un archivo
     * @param path Dirección
     * @param export Contenido a exportar
     * @param fileType Tipo de archivo
     */
    private void saveFile(String path, String export, String fileType) {
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }

        Scanner sc;
        boolean savedToFile = false;
        boolean archivoExistente = false;

        do {
            Optional<String> nombreArchivo;
            do {
                System.out.println("Introduce el nombre del archivo a exportar como " + fileType + ":");
                sc = new Scanner(System.in);

                try{
                    nombreArchivo = Optional.ofNullable(sc.next());
                }catch (InputMismatchException e) {
                    System.err.println("Error: no has introducido un nombre válido.");
                    nombreArchivo = Optional.empty();
                }

            }while(nombreArchivo.isEmpty());

            if (Path.of(dir.getPath(), nombreArchivo.get() + "." + fileType).toFile().exists()) {
                archivoExistente = true;
            }

            if (archivoExistente) {
                int selection = -1;
                do {
                    System.out.println("El archivo con el nombre " + nombreArchivo.get() + " para el " +
                            "fichero " + fileType + " ya existe.\n" +
                            "1 -> Reemplazar archivo\n2 -> Guardar archivo con otro nombre.");
                    sc = new Scanner(System.in);

                    try {
                        selection = sc.nextInt();
                        if (selection < 0 || selection > 2) {
                            selection = -1;
                        } else if (selection == 1) {
                            savedToFile = saveAs(fileType, dir, export, nombreArchivo.get());
                        } else if (selection == 2) {
                            savedToFile = false;
                        }
                    } catch (InputMismatchException e) {
                        System.err.println("Error: no has introducido un número válido.");
                    }
                } while (selection == -1);

            }else{
                //Si no existe, crear
                savedToFile = saveAs(fileType, dir, export, nombreArchivo.get());
            }
        }while(!savedToFile);
    }

    /**
     * Convierte y prepara el guardado
     * @param fileType Tipo de archivo
     * @param dir Dirección
     * @param export Contenido a exportar
     * @param fileName Nombre del archivo
     * @return ¿Guardado realizado?
     */
    private boolean saveAs(String fileType, File dir, String export, String fileName){
        boolean saved = false;

        try {
            //Convertir a XML
            String finalString = export;

            if (Objects.equals(fileType, "xml")){
                finalString = U.jsonToXml(export);
            }

            Files.writeString(Path.of(dir.getPath(), fileName + "." + fileType), finalString);

            saved = true;
        } catch (IOException e) {
            System.out.println("Error al escribir archivo en el directorio: " + e.getMessage());
        }
        if (saved){
            System.out.println("Archivo " + fileType + " creado correctamente.");
            //System.out.println("⚠ Cierra la aplicación para ver el archivo generado.");
        }

        return saved;
    }

    /**
     * Pregunta al usuario por consola de qué forma quiere exportar los datos
     * @param path Dirección de exportación
     * @param result Contenido de exportación
     */
    public void exportUserSelect(String path, String result) {
        //Selección de exporte
        int nSelected = -1;
        boolean toJSON;
        boolean toXML;
        Scanner sc;

        do{
            sc = new Scanner(System.in);
            System.out.println("\n▶ ¿En qué formato quieres exportar los resultados de las consultas?\n" +
                    "1 -> JSON\t2 -> XML\t3 -> JSON y XML\n");
            toJSON = false;
            toXML = false;

            try{
                nSelected = sc.nextInt();

                //Evitar que se introduzca un número menor que 1 o mayor que 3
                if (nSelected < 1 || nSelected > 3){
                    nSelected = -1;
                }
            }
            catch (InputMismatchException e)
            {
                System.err.println("Error: no has introducido un número válido.");
                nSelected = -1;
            }
        }while(nSelected == -1);


        if (nSelected == 1){
            toJSON = true;
            System.out.println("\nSelección de exportación: JSON\n");
        }else if (nSelected == 2){
            toXML = true;
            System.out.println("\nSelección de exportación: XML\n");
        } else if (nSelected == 3){
            toJSON = true;
            toXML = true;
            System.out.println("\nSelección de exportación: JSON y XML\n");
        }

        this.exportData(toJSON, toXML, path, result);
    }
}
