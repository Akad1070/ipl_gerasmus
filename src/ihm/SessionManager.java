package ihm;

import core.exceptions.FatalException;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Gestionnaire de Session. <br>
 * Permet la création et la recupération de la Session.<br>
 * La création d'un JWToken et la déconnexion de l'user.
 *
 * @author Maduka Junior
 */
class SessionManager {

  /**
   * Cree une {@link HttpSession} pour cet user.
   *
   * <ol>
   * <li>Genère une JWToken avec certaine informations sur cet user contenue dans la req</li>
   * <li>Ensuite, cree une Session pour cette user avec ces mêmes informations</li>
   * <li>Enfin, cree un cookie contenant le token et durant le temps du Browser</li>
   * <li><b>Pour finir, Le cookie est accessible depuis toute requête envoyée a l'App.</b></li>
   * </ol>
   *
   * @param req - La {@link HttpServletRequest} contenant les attributs <br>
   *        {ID,mail,departement et estProf}
   * @param resp - La {@link HttpServletResponse} pour enregistrer le token.
   * @return le token generée pour cet user.
   */
  static String initSession(HttpServletRequest req, HttpServletResponse resp) {
    int id = (int) req.getAttribute("ID");
    String mail = (String) req.getAttribute("mail");
    String departement = (String) req.getAttribute("departement");
    int departementId = (int) req.getAttribute("departement_ID");
    Boolean estProf = (Boolean) req.getAttribute("estProf");

    String token = SessionManager.genJwtToken(id, mail, departementId, departement, estProf);

    HttpSession sess = req.getSession();
    sess.setAttribute("IP", req.getRemoteHost());
    sess.setAttribute("ID", id);
    sess.setAttribute("mail", mail);
    sess.setAttribute("departement", departement);
    sess.setAttribute("departement_ID", departementId);
    sess.setAttribute("estProf", estProf);

    Cookie cookie = new Cookie("App-Token", token);
    // cookie.setMaxAge(60 * 60 * 24 * 30 * 3); // Reste pendant 3 mois.
    cookie.setMaxAge(-1); // Des ke le browser ferme,plus de cookie
    cookie.setHttpOnly(true);
    resp.addCookie(cookie);

    return token;
  }

  /**
   * Efface la {@link HttpSession} de cet user et tous les cookies générées pour cet user.
   *
   * @param req - La {@link HttpServletRequest} de l'user
   * @param resp - La {@link HttpServletResponse} pour enregistrer ce changement.
   */
  static void clear(HttpServletRequest req, HttpServletResponse resp) {
    req.getSession().invalidate();
    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        cookie.setDomain("null");
        cookie.setValue("null");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setVersion(0);
        resp.addCookie(cookie);
      }
    }
  }

  /**
   * Genree un JWToken avec les informations primordiaux sur l'User
   *
   * @param id - L'ID de l'user
   * @param mail - Le mail de l'user
   * @param departementId - L' Id du departement de cet user.
   * @param departement - Le departement de l'user
   * @param estProf - Est-ce un prof
   * @return le String contenant le token.
   */
  static String genJwtToken(int id, String mail, int departementId, String departement,
      Boolean estProf) {
    return JwtManager.generate(id, mail, departementId, departement, estProf);
  }


  /**
   * Chope la session dispo sinon la reconstitue grace au token <br>
   * contenue dans le Cookie attaché la requête.
   *
   * @param req - La {@link HttpServletRequest} de l'user
   * @param resp - La {@link HttpServletResponse} pour enregistrer ce changement.
   * @return la {@link HttpSession} existant ou crée.
   * @throws FatalException Si aucun cookie n'existe ou le token n'est pas enregistré.
   */
  static HttpSession getSession(HttpServletRequest req, HttpServletResponse resp) {
    // ? Y'a-til deja un session init. pour cette requête ?
    if (req.getSession(false) != null) {
      return req.getSession(false);
    }
    // Aucune session crée, Je chope le token depuis les cookies dans la Requete
    String token = SessionManager.getTokenFromCookies(req);
    if (token != null) {
      try {
        // Je check si valide
        Map<String, Object> claims = JwtManager.verify(token);
        if (claims != null) {
          // Je cree la Session avec les bonnes données.
          req.setAttribute("ID", claims.get("ID"));
          req.setAttribute("pseudo", claims.get("mail"));
          req.setAttribute("departement", claims.get("departement"));
          req.setAttribute("departement_ID", claims.get("departement_ID"));
          req.setAttribute("estProf", claims.get("estProf"));
          SessionManager.initSession(req, resp);
          return req.getSession(false);
        }
      } catch (Exception exception) {
        throw new FatalException("Erreur lors de la récupération de la Session", exception);
      }
    }
    return null;

  }

  /**
   * Check les cookies pour recuperer le token.
   *
   * @param req - La requete contenant des {@link Cookie}. Miam :-p
   * @return le String de token.
   */
  static String getTokenFromCookies(HttpServletRequest req) {
    // Va chercher le token dans les cookies
    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
      for (Cookie c : cookies) {
        if (c.getName().equals("App-Token")) {
          return c.getValue();
        }
      }
    }

    return null;

  }
}
