package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Clase cifradora
 */
public class Cifrator {

    private Cifrator(){

    }

    /**
     * Cifra una contraseña en SHA256
     * @param password String
     * @return String cifrado
     */
    public static String SHA256(String password) {
        MessageDigest md;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assert hash != null;
        return convertToHex(hash);
    }

    /**
     * Convierte un array de byte[] a un String hexadecimal.
     * @param raw the byte[] to convert
     * @return the string the given byte[] represents
     */
    public static String convertToHex(byte[] raw) {
        StringBuffer sb = new StringBuffer();
        for (byte b : raw) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
