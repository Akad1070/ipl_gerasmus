package ihm;

import core.config.AppConfig;
import core.exceptions.AppException;
import core.exceptions.BizzException;
import core.exceptions.FatalException;
import core.exceptions.ForbiddenActionException;
import core.exceptions.UnknownActionException;
import util.AppUtil;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.http.HttpStatus;

/**
 * Sert de base à l'AppServlet. Ne gere aucun chemin URL. Delegué pour certains taches répétives et
 * contient tous les atttibuts/méthodes pour bien gérer la Requête.
 *
 * @author Akaad - Aurelien
 */
public class BaseServlet extends HttpServlet {
  enum TypeUser {
    Quidam(AppConfig.getAppValueOf("dossierPartialsQuidam")), Etud(
        AppConfig.getAppValueOf("dossierPartialsEtud")), Prof(
            AppConfig.getAppValueOf("dossierPartialsProf"));

    private String pathForPartials;

    private TypeUser(String path) {
      pathForPartials = path;
    }

    public String getPathForPartials() {
      return pathForPartials;
    }

  }

  private static final long serialVersionUID = 5671839099757594251L;

  protected final transient Genson genson = new GensonBuilder().exclude("mdp")
      .useDateFormat(new SimpleDateFormat("dd/MM/yyyy")).useIndentation(true).create();



  /**
   * Contient tout ce qui sera recu lors de la réponse à la requête.
   */
  protected final ConcurrentHashMap<String, Object> respJson =
      new ConcurrentHashMap<String, Object>();



  /**
   * Effectue un check sur le pararm ou la data en param.
   *
   * @param param - Le paramètre venant de la requête
   * @return <b>TRUE</b> si valide (Pas vide et != null && "undefined")
   */
  protected boolean checkParameter(String param) {
    return !(param == null || param.isEmpty() || param.equals("null")
        || param.equalsIgnoreCase("undefined"));
  }



  /*
   * (non-Javadoc)
   *
   * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected synchronized void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      AppUtil.startChrono(); // Petit chrono pour savoir cmb de temps pr exec la req
      System.out.println("\n***************** DEBUT Request Thread #"
          + Thread.currentThread().getId() + " *****************");
      System.out.println("[" + req.getMethod() + "] Params  : " + req.getParameterMap());

      // Init la map de response
      respJson.put("err", "");
      respJson.put("msg", "");

      super.service(req, resp);

    } catch (ForbiddenActionException exception) {
      respJson.put("err", exception.getMessage());
      respJson.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    } catch (UnknownActionException | BizzException exception) {
      respJson.put("err", exception.getMessage());
      respJson.put("status", HttpServletResponse.SC_BAD_REQUEST);
    } catch (AppException exception) {
      String msg = exception.getMessage();
      respJson.put("err", (msg != null) ? msg : AppConfig.getAppValueOf("FATAL_ERROR"));
      respJson.put("status", HttpServletResponse.SC_NOT_FOUND);
    }

    sendResponse(req, resp); // Quoiqu'il arrive, j'envoie une response
  }

  /**
   * Check si la requete est authentique.
   *
   * @param req - La requete recue de l'user
   * @param resp - La response
   * @return <b>TRUE</b> si bien authentifié, possède un UserID en session
   * @throws ForbiddenActionException si la requête ne contient pas dans les header, un token.
   */
  protected boolean isAuth(HttpServletRequest req, HttpServletResponse resp) {
    HttpSession sess = SessionManager.getSession(req, resp); // Essaie de recup la Session
    // ? Y'as une session et y'as un ID dedans ?
    return (sess != null && sess.getAttribute("ID") != null);
  }

  /**
   * Prepare les données recues en POST et parse le tout dans un Map.
   *
   * @param req - Celui du doPost(req,resp)
   * @param resp - Celui du doPost(req,resp)
   * @throws IOException - Pour toute exception apparue lors du traitement des data.
   */
  @SuppressWarnings("unchecked")
  protected Map<String, Object> getPostedData(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    // Recup les data du post en gerant les saut de lignes
    String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    System.out.println("Data Recu : " + data);
    AppUtil.checkString(data, "Aucune donnée envoyée");
    Map<String, Object> posted = genson.deserialize(data.toString(), Map.class);
    String vue = (String) posted.get("vue");
    if (!vue.equalsIgnoreCase("quidam") && !isAuth(req, resp)) {
      // Doit être authentifié pour des actions autre ke base
      throw new ForbiddenActionException("Veuillez vous connecter d'abord");
    }

    String inputData = (String) posted.get("input_data");
    AppUtil.checkString(inputData);
    posted.put("input_data", genson.deserialize(inputData.toString(), Map.class));

    respJson.put("status", HttpStatus.OK_200);
    // TODO ? Par-defaut, on assume que la req n'aboutira pas ?
    // Si, une methode va traiter la req, on lui met le Status adéquat
    return posted;
  }

  /**
   * Charge les vues en fonction du type de l'user.<br>
   * Le quidam ne verra ke l'inscription et la connexion.
   *
   * @param typeUser - Sert à determiner quelles vues doivent être 'send' pour cet user.
   * @throws IOException Est embalé dans la FatalException.
   * @throws FatalException Si une erreur intervient pendant le traitement du fichier HTML.
   */
  protected void chargeVuePartials(TypeUser typeUser) throws IOException {
    final List<String> fragments = new ArrayList<>();
    final List<String> menus = new ArrayList<>();

    // Je recupere chak menus;
    Files.walk(Paths.get(AppConfig.getAppValueOf("dossierMenus"))).forEach(menu -> {
      try {
        if (Files.isRegularFile(menu)) {
          menus.add(new String(Files.readAllBytes(menu), StandardCharsets.UTF_8));
        }
      } catch (Exception excep) {
        throw new FatalException("Erreur lors de la récupation des menus pour un " + typeUser,
            excep);
      }
    });
    respJson.put("menus", menus);

    // Je recupere mnt ts les partials pour l'user;
    Files.walk(Paths.get(typeUser.getPathForPartials())).forEach(vue -> {
      try {
        if (Files.isRegularFile(vue)) {
          fragments.add(new String(Files.readAllBytes(vue), StandardCharsets.UTF_8));
        }
      } catch (Exception excep) {
        throw new FatalException("Erreur lors de la récupation des fragments pour un " + typeUser,
            excep);
      }
    });


    // Je rajoute la liste pour renvoit au JS
    respJson.put("vues", fragments);

  }


  /**
   * Appeler en fin de traitement de la requête de tout type. <br>
   * Sérialiser la réponse à envoyer et effectue l'envoie.
   *
   * @param req - La requête recue.
   * @param resp - La réponse à envoyer
   * @throws IOException - Pour toute exception apparue lors du traitement de la réponse.
   */
  protected synchronized void sendResponse(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    resp.setContentType("text/json");
    if (respJson.containsKey("status")) {
      resp.setStatus((int) respJson.get("status"));
    }
    resp.setCharacterEncoding("utf-8");
    resp.getWriter().print(genson.serialize(respJson));
    resp.flushBuffer();
    // Reponse envoyé --> Vide la map aussi
    respJson.clear();

    System.out.printf("%nReq terminée avec Status %d : %s ~ %s %n", resp.getStatus(),
        AppUtil.resultatEnMs(), AppUtil.resultatEnSec());
    System.out.println("***************** FINI Request Thread #" + Thread.currentThread().getId()
        + " ***************** ");

  }
}
