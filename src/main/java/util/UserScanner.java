package util;

import java.sql.Date;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserScanner {

    private static UserScanner userScannerInstance;

    private UserScanner() {
    }

    public static UserScanner getInstance() {
        if (userScannerInstance == null) {
            userScannerInstance = new UserScanner();
        }
        return userScannerInstance;
    }

    /**
     * Pregunta al usuario por consola una fecha (año, mes y día) y devuelve el Date
     * @param msg Mensaje inicial a mostrar por consola
     * @return Retorna la fecha
     */
    public Date scannerDate(String msg){
        int year = 1;
        int month = 1;
        int day = 1;
        String fechaStr;
        boolean exitYear = false;
        boolean exitMonth = false;
        boolean exitDay = false;

        System.out.println(msg + ":");

        do{
            //AÑO
            System.out.println("Introduce el año:");
            Scanner sc = new Scanner(System.in);
            try{
                year = sc.nextInt();

                int minYear = 1000;
                int maxYear = Calendar.getInstance().get(Calendar.YEAR);
                if (year >= minYear && year <= maxYear){
                    exitYear = true;
                }else{
                    System.err.println("Error: no has introducido un año válido. Debe ser menor que " + minYear + " y " +
                            "mayor que " + maxYear);
                }

            } catch (InputMismatchException e) {
                System.err.println("Error: no has introducido un año válido.");
                exitYear = false;
            }}while(!exitYear);

        int maxDays = 31;
        do{
            //MES
            System.out.println("Introduce el mes:");
            Scanner sc = new Scanner(System.in);
            try{
                month = sc.nextInt();

                if (month > 0 && month < 13){
                    exitMonth = true;

                    if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
                        maxDays = 31;
                    } else if (month == 4 || month == 6 || month == 9 || month == 11){
                        maxDays = 30;
                    } else if (month == 2){
                        //Comprobar si el año es bisiesto
                        if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))){
                            maxDays = 29;
                        }else{
                            maxDays = 28;
                        }
                    }
                }else{
                    System.err.println("Error: no has introducido un mes válido.");
                }

            } catch (InputMismatchException e) {
                System.err.println("Error: no has introducido un mes válido.");
                exitMonth = false;
            }}while(!exitMonth);

        do{
            //DÍA
            System.out.println("Introduce el día:");
            Scanner sc = new Scanner(System.in);
            try{
                day = sc.nextInt();

                if (day > 0 && day <= maxDays){
                    exitDay = true;
                }else{
                    System.err.println("Error: no has introducido un día válido.\n" +
                            "El mes que has introducido (" + month + ") tiene un máximo de " + maxDays + " días.");
                }

            } catch (InputMismatchException e) {
                System.err.println("Error: no has introducido un día válido.");
                exitDay = false;
            }}while(!exitDay);

        fechaStr = year + "-" + String.format("%02d" , month) + "-" + String.format("%02d" , day);
        System.out.println(fechaStr);
        return Date.valueOf(fechaStr);
    }

    /**
     * Pregunta al usuario un double por consola y se realiza la conversión
     * @param msg String
     * @return double
     */
    public double scannerDouble(String msg){
        double result = 0.0;
        boolean exit;

        do{
            Scanner sc = new Scanner(System.in);
            System.out.println(msg + ":");

            try{
                result = sc.nextDouble();
                exit = true;
            } catch (InputMismatchException e) {
                System.err.println("Error: no has introducido un dato double válido.");
                exit = false;
            }}while(!exit);

        return result;
    }

    /**
     * Pregunta al usuario un String por consola, se comprueba y se devuelve
     * @param msg Mensaje
     * @return String
     */
    public String scannerString(String msg){
        String result = "";
        boolean exit;

        do{
            Scanner sc = new Scanner(System.in);
            System.out.println(msg + ":");

            try{
                result = sc.next();
                exit = true;
            } catch (InputMismatchException e) {
                System.err.println("Error: no has introducido un dato String válido.");
                exit = false;
            }}while(!exit);

        return result;
    }

    /**
     * Pregunta al usuario una pregunta de 'Sí o No' que se pasa por parámetro. Se devuelve el resultado en boolean
     * @param msg Pregunta
     * @return boolean que devuelve la respuesta de la pregunta
     */
    public boolean scannerBoolean(String msg){
        boolean result = false;
        int resultn = -1;
        boolean exit = false;

        do{
            Scanner sc = new Scanner(System.in);
            System.out.println(msg + ":\n1 -> " + "Falso" + "\n2 -> " + "Verdadero" + "\n");

            try{
                resultn = sc.nextInt();
                if (resultn > 0 && resultn < 3){
                    if (resultn == 2){
                        result = true;
                    }
                    exit = true;
                }
            } catch (InputMismatchException e) {
                System.err.println("Error: no has introducido un dato válido (1 o 2).");
                exit = false;
            }}while(!exit);

        return result;
    }
}
