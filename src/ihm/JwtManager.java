package ihm;

import core.exceptions.FatalException;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe servant à créer et vérifier le JWT token.
 *
 * @author Maduka Junior
 */
public class JwtManager {

  private static String SECRET = "Portez ce vieux whisky au juge blond qui fume";

  static String generate(int id, String user, int departementId, String departement,
      Boolean estProf) {
    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put("ID", id);
    claims.put("mail", user);
    claims.put("departement", departement);
    claims.put("departement_ID", departementId);
    claims.put("estProf", estProf);
    // claims.put("created", System.currentTimeMillis());


    return new JWTSigner(JwtManager.SECRET).sign(claims);
  }

  static Map<String, Object> verify(String token) {
    Map<String, Object> decodedPayload;
    try {
      decodedPayload = new JWTVerifier(JwtManager.SECRET).verify(token);
    } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException
        | SignatureException | IOException | JWTVerifyException exception) {
      throw new FatalException("Erreur lors de la vérification du token", exception);
    }
    return decodedPayload;
  }
}
