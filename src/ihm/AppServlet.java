package ihm;

import core.config.AppConfig;
import core.exceptions.BizzException;
import core.exceptions.ForbiddenActionException;
import core.exceptions.UnknownActionException;
import core.injecteur.InjecteurDependance.Injecter;
import domaine.dto.AnnulationDto;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.ChoixMobiliteDto.Etat;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.PartenaireDto;
import domaine.dto.PaysDto;
import domaine.dto.ProgrammeDto;
import domaine.dto.TypeProgrammeDto;
import domaine.dto.UserDto;
import domaine.dto.UserInfoDto;
import domaine.factory.EntiteFactory;
import domaine.ucc.interfaces.ProfesseurUcc;
import domaine.ucc.interfaces.QuidamUcc;
import util.AppUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet principal de l'application répondant aux requêtes.
 *
 * @author Akaad - Aurelien
 */
@SuppressWarnings("serial")
public class AppServlet extends BaseServlet {

  @Injecter
  private EntiteFactory factory;

  @Injecter
  private QuidamUcc quidamUcc;

  @Injecter
  private ProfesseurUcc profUcc;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    if (super.isAuth(req, resp)) {
      boolean estAuthEtProf =
          (boolean) SessionManager.getSession(req, resp).getAttribute("estProf");
      chargeVuePartials((estAuthEtProf) ? TypeUser.Prof : TypeUser.Etud);
    } else {
      chargeVuePartials(TypeUser.Quidam);
    }
  }

  /**
   * Pour toute donnée soumise par l'user, ses requetes atterissent ici.
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    // Un param pour tous les petits details ne dépendant pas d'une vue.
    String paramReq = req.getParameter("req");
    if (super.checkParameter(paramReq)) {
      switch (paramReq) {
        case "departements":
          respJson.put("departements", profUcc.listerDepartements());
          return;
        case "isAuth":
          respJson.put("isAuth", isAuth(req, resp));
          return;
        case "user":
          if (!isAuth(req, resp)) {
            throw new ForbiddenActionException(AppConfig.getAppValueOf("UNAUTHORIZED_ACTION"));
          }
          recupererUser(extraireUserParId(req.getParameter("uid"),
              SessionManager.getSession(req, resp)));
          return;
        case "organisations":
          respJson.put("organisations", PartenaireDto.TypeOrganisation.listOrgas);
          return;
        case "etats_mobilite":
          respJson.put("etats_mobilite", ChoixMobiliteDto.Etat.listEtat);
          return;
        case "programmes":
          respJson.put("programmes", profUcc.listerProgrammes());
          return;
        case "types_programme":
          respJson.put("types_programme", profUcc.listerTypesProgramme());
          return;
        case "annees_academique":
          respJson.put("annees_academique", profUcc.listerAnneesAcademiqueDejaIntroduites());
          return;
        case "nb_total_programmes":
          return;
        case "nb_total_pays":
          return;
        case "nb_choix_mobilites":
          return;
        case "motifs":
          if (!isAuth(req, resp)) {
            throw new ForbiddenActionException(AppConfig.getAppValueOf("UNAUTHORIZED_ACTION"));
          }
          respJson.put("motifs", profUcc.listerMotifsAnnulations());
          return;

        case "partenaires":
          UserDto etudiant =
          extraireUserParId(req.getParameter("uid"), SessionManager.getSession(req, resp));
          respJson.put("partenaires", profUcc.listerPartenairesPourEtudiant(etudiant));
          return;
        case "pays":
          respJson.put("pays", profUcc.listerPays());
          resp.setStatus(HttpServletResponse.SC_OK);
          return;
        case "out":
          SessionManager.clear(req, resp);
          resp.setStatus(HttpServletResponse.SC_OK);
          return;
        default:
          throw new UnknownActionException();
      } // switch
    } // if:CheckParameter

    // Je recuperer la vrai data posté par l'User
    Map<String, Object> posted = super.getPostedData(req, resp);
    Map<String, String> data = (Map<String, String>) posted.get("input_data");
    String action = (String) posted.get("action");

    switch ((String) posted.get("vue")) {
      case "user":
        gererActionsUser(req, resp, action, data);
        return;
      case "recherche":
        gererActionsRecherche(req, resp, data.get("type"), data);
        return;
      case "partenaire":
        gererActionsPartenaire(req, resp, action, (Map<String, Object>) posted.get("input_data"));
        return;
      case "mobilite":
        gererActionsMobilite(req, resp, action, (Map<String, Object>) posted.get("input_data"));
        return;
      case "quidam":
        gererActionsQuidam(req, resp, action, data);
        return;
      default:
        throw new UnknownActionException();

    } // switch Auth

  }


  private void gererActionsUser(HttpServletRequest req, HttpServletResponse resp, String action,
      Map<String, String> data) {
    switch (action) {
      case "details":
      case "profil":
        UserDto user =
        extraireUserParId(data.get("user_id"), SessionManager.getSession(req, resp));
        recupererUser(user);

        UserInfoDto userInfo = profUcc.recupererUserInfoParUId(user);
        if (userInfo != null) {
          respJson.put("infos", userInfo);
        }
        return;

      default:
        return;
    }

  }

  @SuppressWarnings("unchecked")
  private void gererActionsMobilite(HttpServletRequest req, HttpServletResponse resp,
      String action, Map<String, Object> data) {

    ChoixMobiliteDto mobilite = factory.getChoixMobilite();
    HttpSession sess = SessionManager.getSession(req, resp);

    switch (action) {
      case "ajout":
        ajouterMobilites(data, (int) sess.getAttribute("ID"));
        return;
      case "annuler":
        AnnulationDto annulation = factory.getAnnulation();
        annulation.setCreateur(extraireUserParId((String) data.get("uid"), sess));
        if ((boolean) sess.getAttribute("estProf")) {
          annulation.setId(Integer.parseInt((String) data.get("motif")));
        } else {
          annulation.setMotif((String) data.get("motif"));
        }

        mobilite.setNumCandidature(Integer.parseInt((String) data.get("mobilite_id")));
        mobilite.setNumPreference(Integer.parseInt((String) data.get("mobilite_preference")));

        mobilite.setMotifAnnulation(annulation);

        profUcc.annulerChoixMobilite(mobilite);
        respJson.put("msg", "La mobiité a été annulée.");
        return;
      case "details":
        DemandeMobiliteDto demande = factory.getDemandeMobilite();
        demande.setId(Integer.parseInt((String) data.get("mobilite_id")));
        respJson.put("mobilites", profUcc.recupererMobilitesPourDemande(demande));
        return;
      case "maj":
        majMobilite(data, mobilite, sess);
        return;
      case "list":
        boolean estProf = (boolean) sess.getAttribute("estProf");
        if (estProf && data.get("uid") == null) {
          List<DepartementDto> deps;
          if (data.get("departements") != null) {
            deps = ((List<String>) data.get("departements")).stream().map(depId -> {
              DepartementDto dep = factory.getDepartement();
              dep.setId(Integer.parseInt(depId));
              return dep;
            }).collect(Collectors.toList());
          } else {
            // Aucun list de departement envoyés, alors lister seulement ceux de son
            // departement
            DepartementDto dep = factory.getDepartement();
            dep.setId((int) SessionManager.getSession(req, resp).getAttribute("departement_ID"));
            deps = Arrays.asList(dep);
          }
          Map<String, List<DemandeMobiliteDto>> mapList =
              profUcc
                  .listerDemandeMobilites(deps)
                  .stream()
                  .collect(
                      Collectors.groupingBy(dem -> dem.getEtudiant().getDepartement().getNom()));
          respJson.put("mobilites", mapList);
        } else { // C'est un etud et il veut lister ses mobilités
          respJson.put("mobilites",
              profUcc.listerMobilitesPourEtud(extraireUserParId((String) data.get("uid"), sess)));
        }
        return;
      default:
        throw new UnknownActionException();
    }

  }

  private void gererActionsPartenaire(HttpServletRequest req, HttpServletResponse resp,
      String action, Map<String, Object> data) {
    switch (action) {
      case "ajout":
        ajouterPartenaire(data, SessionManager.getSession(req, resp));
        return;
      case "details":
        detaillerPartenaire(data);
        return;
      case "maj":
        // this.majPartenaire(data);
        return;
      case "list":
        boolean estProf = (boolean) SessionManager.getSession(req, resp).getAttribute("estProf");
        // TODO pour David
        // Quand on est un prof, on veux tout les partenaire ou seulement ceux qui ne sont pas
        // cachés ?
        // Si tous ? alors il faudra changer.
        if (estProf) {
          respJson.put("partenaires", profUcc.listerPartenairesSelectionnablesPourTous());
        } else {
          UserDto etudiant =
              extraireUserParId(req.getParameter("uid"), SessionManager.getSession(req, resp));
          respJson.put("partenaires", profUcc.listerPartenairesPourEtudiant(etudiant));
        }
        return;
      default:
        throw new UnknownActionException();

    } // switch

  }

  private void gererActionsRecherche(HttpServletRequest req, HttpServletResponse resp,
      String type, Map<String, String> data) {
    List<?> list;
    String emptyResultMsg;
    switch (type) {
      case "user":
        list = rechercherUser(data);
        emptyResultMsg = "Aucun utilisateur correspondant à votre recherche.";
        break;
      case "partenaire":
        list = rechercherPartenaire(data);
        emptyResultMsg = "Aucun partenaire correspondant à votre recherche.";
        break;
      case "mobilite":
        list = rechercherMobilite(data);
        emptyResultMsg = "Aucune mobilité correspondante à votre recherche.";
        break;
      default:
        rechercherUser(data);
        rechercherPartenaire(data);
        rechercherMobilite(data);
        return;
    }

    if (list == null || list.isEmpty()) {
      respJson.put("msg", emptyResultMsg);
    } else {
      respJson.put("msg", list.size() + " correspondance(s) à votre recherche.");
      respJson.put("resultat", list);
    }

  }

  private void gererActionsQuidam(HttpServletRequest req, HttpServletResponse resp, String action,
      Map<String, String> data) {
    switch (action) {
      case "connexion":
        seConnecter(req, resp, data);
        return;
      case "inscription":
        inscrireUser(data);
        return;
      case "deconnexion":
      case "out":
        SessionManager.clear(req, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
        return;
      default:
        throw new UnknownActionException();

    } // switch
  }



  private void seConnecter(HttpServletRequest req, HttpServletResponse resp,
      Map<String, String> data) {
    UserDto userDto = factory.getUser();
    userDto.setIdentifiant(data.get("identifiant"));
    userDto.setMdp(data.get("mdp"));
    userDto = quidamUcc.seConnecter(userDto);

    req.setAttribute("ID", userDto.getId());
    req.setAttribute("pseudo", userDto.getPseudo());
    req.setAttribute("mail", userDto.getMail());
    req.setAttribute("departement_ID", userDto.getDepartement().getId());
    req.setAttribute("departement", userDto.getDepartement().getCode());
    req.setAttribute("estProf", userDto.isProf());

    respJson.put("token", SessionManager.initSession(req, resp));
    respJson.put("user", userDto);
    respJson.put("user.type", (userDto.isProf() ? "prof" : "etud"));
    respJson.put("status", HttpServletResponse.SC_OK);
  }

  private void inscrireUser(Map<String, String> data) {
    UserDto userDto = factory.getUser();
    userDto.setPseudo(data.get("pseudo"));
    userDto.setMail(data.get("mail"));
    userDto.setMdp(data.get("mdp"));
    userDto.setNom(data.get("nom"));
    userDto.setPrenom(data.get("prenom"));
    DepartementDto departement = factory.getDepartement();
    departement.setId(Integer.parseInt(data.get("departement")));
    userDto.setDepartement(departement);

    userDto = quidamUcc.inscrire(userDto);
    respJson.put("msg", "Inscription effectuée pour " + userDto.getPseudo());
    respJson.put("status", HttpServletResponse.SC_OK);
  }

  /**
   * Recuper un UserDto et le met directement dans la Map de responseJSON.
   *
   * @param user - L'utilisateur.
   */
  private void recupererUser(UserDto user) {
    respJson.put("user", user = profUcc.recupererUserParId(user));
    respJson.put("user.type", (user.isProf() ? "prof" : "etud"));
    super.respJson.put("status", HttpServletResponse.SC_OK);
  }

  /**
   * Genere un UserDTO avec le userId en params si valide après check. <br>
   * Sinon, utilise l'ID en session pour cet user;
   *
   * @param userId - L'Id de l'user à extraire.
   * @param sess - La session actuelle :
   *        {@link SessionManager#getSession(HttpServletRequest, HttpServletResponse)}
   * @return une {@link UserDto} contenant au moins son ID et si provient de la
   *         {@link SessionManager} avec le boolen pour déterminer si prof ou pas.
   */
  private UserDto extraireUserParId(String userId, HttpSession sess) {
    UserDto user = factory.getUser();
    if (checkParameter(userId)) {
      int uid = Integer.parseInt(userId); // Y'a un param uid
      AppUtil.checkPositive(uid, "L'utilisateur est inexistant");
      user.setId(uid);
    } else { // Y'a pas de param uid, donc c'est sur le User actuel
      user.setId((int) sess.getAttribute("ID"));
      user.setProf((boolean) sess.getAttribute("estProf"));
    }
    return user;
  }

  @SuppressWarnings("unchecked")
  private void ajouterPartenaire(Map<String, Object> data, HttpSession sess) {
    PartenaireDto partenaire = factory.getPartenaire();

    partenaire.setNomAffaire((String) data.get("nom_affaire"));
    partenaire.setNomLegal((String) data.get("nom_legal"));
    if (checkParameter((String) data.get("nom_complet"))) {
      partenaire.setNomcomplet((String) data.get("nom_complet"));
    }
    if (checkParameter((String) data.get("adresse"))) {
      partenaire.setNomcomplet((String) data.get("adresse"));
    }
    if (checkParameter((String) data.get("cp"))) {
      partenaire.setCodePostal((String) data.get("cp"));
    }
    if (checkParameter((String) data.get("type_organisation"))) {
      partenaire.setTypeOrganisation((String) data.get("type_organisation"));
    }

    partenaire.setNbEmploye(Integer.parseInt((String) data.get("nb_employe")));

    PaysDto pays = factory.getPays();
    pays.setId(Integer.parseInt((String) data.get("pays")));
    partenaire.setPays(pays);

    if (checkParameter((String) data.get("region"))) {
      partenaire.setRegion((String) data.get("region"));
    }
    partenaire.setVille((String) data.get("ville"));

    partenaire.setMail((String) data.get("mail"));
    if (checkParameter((String) data.get("site_web"))) {
      partenaire.setSiteWeb((String) data.get("site_web"));
    }
    if (checkParameter((String) data.get("tel"))) {
      partenaire.setTelephone((String) data.get("tel"));
    }

    List<DepartementDto> deps;
    if ((boolean) sess.getAttribute("estProf")) {
      deps = ((List<Object>) data.get("departements_ipl")).stream().map(depId -> {
        DepartementDto dep = factory.getDepartement();
        dep.setId(Integer.parseInt((String) depId));
        return dep;
      }).collect(Collectors.toList());
    } else {
      DepartementDto dep = factory.getDepartement();
      dep.setId(Integer.parseInt((String) sess.getAttribute("depID")));
      deps = Arrays.asList(dep);
    }

    partenaire.setIplDepartements(deps);

    UserDto createur = extraireUserParId((String) data.get("createur"), sess);

    if (profUcc.ajouterPartenaire(createur, partenaire) != null) {
      respJson.put("msg", "Ajout effectué pour " + partenaire.getNomAffaire());
    }

  }

  private List<UserDto> rechercherUser(Map<String, String> data) {
    UserDto user = factory.getUser();
    String champ = data.get("nom");
    if (super.checkParameter(champ)) {
      user.setNom(champ);
    }

    champ = data.get("prenom");
    if (super.checkParameter(champ)) {
      user.setPrenom(champ);
    }

    champ = data.get("mail");
    if (super.checkParameter(champ)) {
      user.setMail(champ);
    }

    return profUcc.rechercherUser(user);
  }

  private List<PartenaireDto> rechercherPartenaire(Map<String, String> data) {
    PartenaireDto partenaire = factory.getPartenaire();
    String champ = data.get("nom_legal");
    if (super.checkParameter(champ)) {
      partenaire.setNomLegal(champ);
    }

    champ = data.get("pays");
    if (super.checkParameter(champ)) {
      PaysDto pays = factory.getPays();
      pays.setNom(champ);
      partenaire.setPays(pays);
    }

    champ = data.get("ville");
    if (super.checkParameter(champ)) {
      partenaire.setVille(champ);
    }

    return profUcc.rechercherPartenaire(partenaire);
  }

  private List<ChoixMobiliteDto> rechercherMobilite(Map<String, String> data) {
    ChoixMobiliteDto mobi = factory.getChoixMobilite();
    DemandeMobiliteDto demande = factory.getDemandeMobilite();
    String champ = data.get("mobi_annee_academique");
    if (super.checkParameter(champ)) {
      demande.setAnneeAcademique(Integer.parseInt(champ));
    }

    champ = data.get("mobi_etat");
    if (super.checkParameter(champ)) {
      mobi.setEtat(Etat.valueOf(champ));
    }
    mobi.setDemande(demande);

    return profUcc.rechercherMobilite(mobi);

  }



  @SuppressWarnings("unchecked")
  private void ajouterMobilites(Map<String, Object> data, int idCreateur) {
    DemandeMobiliteDto demande = factory.getDemandeMobilite();
    // C'est un prof et il se fait passer pour un eleve
    if (checkParameter((String) data.get("uid"))) {
      idCreateur = Integer.parseInt((String) data.get("uid"));
    }
    UserDto etudiant = factory.getUser();
    etudiant.setId(idCreateur);
    demande.setEtudiant(etudiant);

    demande.setAnneeAcademique(Integer.parseInt((String) data.get("annee_academique")));
    int nbChoix = 0;
    if (checkParameter((String) data.get("nb_choix"))) {
      nbChoix = Integer.parseInt((String) data.get("nb_choix"));
    } else {
      throw new BizzException("Le nombre de choix doit être défini.");
    }
    List<ChoixMobiliteDto> mobilites = new ArrayList<>(nbChoix);

    for (int i = 0; i < nbChoix; i++) {
      Map<String, String> inputChoix = (Map<String, String>) data.get(i + "");
      ChoixMobiliteDto choix = factory.getChoixMobilite();

      if (checkParameter(inputChoix.get("pays"))) {
        PaysDto localite = factory.getPays();
        localite.setId(Integer.parseInt(inputChoix.get("pays")));
        choix.setLocalite(localite);
      }

      ProgrammeDto prog = factory.getProgramme();
      prog.setId(Integer.parseInt(inputChoix.get("programme")));
      choix.setProgramme(prog);

      TypeProgrammeDto typeProg = factory.getTypeProgramme();
      typeProg.setId(Integer.parseInt(inputChoix.get("type_programme")));
      choix.setTypeProgramme(typeProg);

      choix.setQuadri(Integer.parseInt(inputChoix.get("quadrimestre")));

      if (checkParameter(inputChoix.get("partenaire"))) {
        PartenaireDto partenaire = factory.getPartenaire();
        partenaire.setId(Integer.parseInt(inputChoix.get("partenaire")));
        choix.setPartenaire(partenaire);
      }

      mobilites.add(choix);
    }
    demande.setChoixMobilites(mobilites);
    if (profUcc.declarerChoix(demande)) {
      respJson.put("msg", "Ajout effectué pour les choix introduits");
    } else {
      respJson.put("msg", "Ajout impossible pour les choix introduits");
    }
  }


  private void majMobilite(Map<String, Object> data, ChoixMobiliteDto mobilite, HttpSession sess) {
    String type = (String) data.get("type");
    switch (type) {
      case "confirmer":
        mobilite.setId(Integer.parseInt((String) data.get("choix_id")));
        mobilite.setNumPreference(Integer.parseInt((String) data.get("preference_id")));
        mobilite.setEtat(Etat.CONFIRME);
        respJson.put("mobilite", profUcc.changerEtat(mobilite));
        respJson.put("msg", "Cette mobilité est maintenant confirmé.");
        return;
      case "indiquer_payement":
        mobilite.setId(Integer.parseInt((String) data.get("choix_id")));
        mobilite.setNumPreference(Integer.parseInt((String) data.get("preference_id")));
        if (data.get("num_payement").equals("1")) {
          mobilite.setEtat(Etat.DEMANDE_PAYEMENT);
        } else {
          mobilite.setEtat(Etat.DEMANDE_PAYEMENT_SOLDE);
        }
        respJson.put("mobilite", profUcc.changerEtat(mobilite));
        respJson.put("msg", "Le statut du payement pour cette mobilite a été mis à jour.");
        return;
      case "confirmer_encodage":

        return;
      case "confirmer_signature":
        // respJson.put("",
        // profUcc.listerEtatDocuments(extraireUserParId((String) data.get("uid"), sess)));
        return;
      default:
        throw new UnknownActionException();

    }
  }



  private void detaillerPartenaire(Map<String, Object> data) {
    PartenaireDto partenaire = factory.getPartenaire();
    if (checkParameter((String) data.get("pat_id"))) {
      partenaire.setId(Integer.parseInt((String) data.get("pat_id")));
    } else {
      throw new BizzException("Veuillez au moins fournir l'ID du partenaire.");
    }

    partenaire = profUcc.recupererPartenaireParId(partenaire);
    if (partenaire != null) {
      respJson.put("partenaire", partenaire);
    } else {
      respJson.put("err", "Le Partenaire #" + data.get("pat_id") + " n'existe pas.");
    }
  }


}
