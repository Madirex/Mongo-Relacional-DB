package util;

import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Token {
    private static Token tokenInstance;
    private final String key = "98iuh43gi5yy6556tu4i5h4iu";

    private Token() {
    }

    public static Token getInstance() {
        if (tokenInstance == null) {
            tokenInstance = new Token();
        }
        return tokenInstance;
    }

    /**
     * Genera un token según el contenido
     * @param content Contenido
     * @return Token generado
     */
    public String generateToken(String content){
        Signer signer = HMACSigner.newSHA256Signer(content);

        JWT jwt = new JWT().setIssuer("www.acme.com")
                .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .setSubject(key)
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(5));

        return JWT.getEncoder().encode(jwt, signer);
    }

    /**
     * Valida el token pasado por parámetro
     * @param content contenido
     * @param encodedJWT codificación JWT
     * @return ¿Token válido?
     */
    public boolean checkToken(String content, String encodedJWT){
        Verifier verifier = HMACVerifier.newVerifier(content);

        JWT j = JWT.getDecoder().decode(encodedJWT, verifier);

        return j.subject.equals(key);
    }
}
