package util;

import core.config.AppConfig;
import core.exceptions.BizzException;
import domaine.dto.UserInfoDto.Civilite;
import domaine.dto.UserInfoDto.Sexe;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe utilitaire servant surtout pour les check.
 *
 * @author Akad
 */
public class AppUtil {
  private static enum RegexPattern {
    // MDP("(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$"),
    NOM("^([a-zA-z])+([ \\'\\-][a-zA-Z]+)*$"), TEL(
        "((\\+|00)[0-9]{2} (\\(0\\))?)? ?[0-9 /\\.]{8,12}"), MAIL(
            "^[a-zA-Z0-9\\_\\.\\+\\-]+@[a-zA-Z0-9\\-]" + "+\\.[a-zA-Z0-9\\-\\.]+$"), MAIL_ETUD_IPL(
                "^[a-zA-Z]+\\.[a-zA-Z]+" + "@student.vinci.be$"), MAIL_PROF_IPL(
                    "^[a-zA-Z]+\\.[a-zA-Z]+" + "@vinci.be$"), BIC_CODE(
                        "([a-zA-Z]{4}[a-zA-Z]{2}[a-zA-Z0-9]{2}([a-zA-Z0-9]{3})?)"), IBAN(
                            "[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}");

    private final String regex;
    private final Pattern pattern;

    private RegexPattern(String regex) {
      AppUtil.checkString(regex);
      this.regex = regex;
      pattern = Pattern.compile(this.regex);
    }

    public Pattern getPattern() {
      return pattern;
    }
  }

  public static enum CheckFormat {
    LOGIN, MDP, MAIL, MAIL_ETUD_IPL, MAIL_PROF_IPL, NOM, TEL, SEARCH, BIC, IBAN
  }

  private static long start;

  private static final String formatDatePattern = "dd-MM-yyyy HH:mm:ss";
  public static final DateTimeFormatter dateFormatter =
      DateTimeFormatter.ofPattern(AppUtil.formatDatePattern);



  /**
   * Check si la chaine est un nombre valide en se servant en partie de
   * {@link AppUtil#checkString(String)} .
   *
   * @param str - Le nombre à checker
   * @param msgErreur - Le msg pour l'exception en cas d'erreur.
   * @throws BizzException contenant le msgErreur.
   */
  public static void checkNumerique(String str, String msgErreur) {
    AppUtil.checkString(str);
    try {
      Long.parseLong(str);
    } catch (NumberFormatException exception) {
      throw new BizzException(msgErreur, exception);
    }
  }

  public static void checkNumerique(String str) {
    checkNumerique(str, AppConfig.getAppValueOf("STRING_INPUT"));
  }


  /**
   * Check si l'objet n'est pas null sinon lance une {@link BizzException}. Methode par-défaut.
   *
   * @param obj - L'objet à tester
   * @param msgErreur - Le msg pour l'exception en cas d'erreur.
   * @throws BizzException contenant le msgErreur.
   */
  public static void checkObject(Object obj, String msgErreur) {
    if (obj == null) {
      throw new BizzException(msgErreur, new NullPointerException());
    }

  }

  /**
   * Le message d'erreur est prédéfini.
   *
   * @param obj - L'objet à checker
   * @see AppUtil#checkObject(Object, String)
   */
  public static void checkObject(Object obj) {
    checkObject(obj, AppConfig.getAppValueOf("NULL_OBJET"));
  }


  /**
   * Check si les objets en args sont ne sont pas nulles.
   *
   * @see AppUtil#checkObject(Object,String)
   *
   * @param objs - les objets a vérifier.
   */
  public static void checkObjects(Object... objs) {
    for (Object obj : objs) {
      AppUtil.checkObject(obj);
    }
  }


  /**
   * Check si le nb en params est bien supérieur à 0.
   *
   * @param nb - Le nombre à tester
   * @param msgErreur - Le msg pour l'exception en cas d'erreur.
   * @throws BizzException contenant le msgErreur.
   */
  public static void checkPositive(int nb, String msgErreur) {
    if (nb <= 0) {
      throw new BizzException(msgErreur);
    }
  }

  /**
   * Le msgErreur est deja prédéfini.
   *
   * @param nb - Le nombre à checker.
   * @see AppUtil#checkPositive(int,String)
   */
  public static void checkPositive(int nb) {
    checkPositive(nb, " \"" + nb + "\" -  Ce nombre ne peut pas être nul ou négatif");
  }


  /**
   * Check si l'entier en paramètre est inférieur à 0 sinon lance une {@link BizzException}.
   *
   * @param nb - Le nombre à tester
   * @param msgErreur - Le msg pour l'exception en cas d'erreur.
   * @throws BizzException contenant le msgErreur.
   */
  public static void checkNegative(int nb, String msgErreur) {
    if (nb < 0) {
      throw new BizzException(msgErreur);
    }
  }

  /**
   * Le msgErreur est deja prédéfini.
   *
   * @param nb - Le nombre à checker
   * @see #checkNegative(int, String)
   */
  public static void checkNegative(int nb) {
    checkNegative(nb, "Ce nombre ne peut pas être négatif");
  }

  /**
   * Check si le String en arg est d'abord null puis vide ou est bien une chaine de caractères.
   *
   * @param str - Le string à tester
   * @param msgErreur - Le msg pour l'exception en cas d'erreur.
   * @throws BizzException contenant le msgErreur.
   */
  public static void checkString(String str, String msgErreur) {
    AppUtil.checkObject(str, msgErreur);
    if (str.isEmpty()) {
      throw new BizzException(msgErreur, new IllegalArgumentException());
    }
    if (str.matches("\\s*")) {
      throw new BizzException(AppConfig.getAppValueOf("ALPHANUM_INPUT"));
    }
  }



  public static void checkString(String str) {
    checkString(str, AppConfig.getAppValueOf("ALPHANUM_INPUT"));
  }



  /**
   * Check si le String en paramètre est bien conforme au format MAIL.
   *
   * @param mail - Le mail à checker
   * @return un boolean si conforme ou non.
   */
  public static boolean checkMail(String mail) {
    return AppUtil.checkSelonFormat(CheckFormat.MAIL, mail);
  }

  /**
   * Check si le String en paramètre est bien conforme au format MAIL.
   *
   * @param mail - Le mail à checker
   * @return un boolean si conforme ou non.
   */
  public static boolean checkMailEtud(String mail) {
    return AppUtil.checkSelonFormat(CheckFormat.MAIL_ETUD_IPL, mail);
  }

  /**
   * Check si le String en paramètre est bien conforme au format MAIL.
   *
   * @param mail - Le mail à checker
   * @return un boolean si conforme ou non.
   */
  public static boolean checkMailProf(String mail) {
    return AppUtil.checkSelonFormat(CheckFormat.MAIL_PROF_IPL, mail);
  }

  /**
   * Check si le String en paramètre est bien conforme au format LOGIN.
   *
   * @param login - Le login à tester.
   * @return un boolean si conforme ou non.
   */
  public static boolean checkPseudo(String login) {
    return AppUtil.checkSelonFormat(CheckFormat.LOGIN, login);
  }

  /**
   * Check si le String en paramètre est bien conforme au format NOM/PRENOM.
   *
   * @param nomOuPrenom - Le nom - prenom à tester.
   * @return un boolean si conforme ou non.
   */
  public static boolean checkNomOuPrenom(String nomOuPrenom) {
    return AppUtil.checkSelonFormat(CheckFormat.NOM, nomOuPrenom);
  }

  /**
   * Check si le String en paramètre correspond au Pattern établie pour le format MDP.
   *
   * @param mdp - Le String à checker.
   * @return un boolean si conforme ou non.
   */
  public static boolean checkMdp(String mdp) {
    return AppUtil.checkSelonFormat(CheckFormat.MDP, mdp);
  }

  /**
   * Check si le String en paramètre correspond au Pattern établie pour le format TEL.
   *
   * @param tel - Le String à checker.
   * @return un boolean si conforme ou non.
   */
  public static boolean checkTel(String tel) {
    return AppUtil.checkSelonFormat(CheckFormat.TEL, tel);
  }

  /**
   * Check si le String en paramètre correspond au Pattern établie pour le format SEARCH.
   *
   * @param search - Le String à checker.
   * @return un boolean si conforme ou non.
   */
  public static boolean checkRecherche(String search) {
    return AppUtil.checkSelonFormat(CheckFormat.SEARCH, search);
  }

  /**
   * Check si le string en params dépasse d'au moins la longueur en params.
   *
   * @param str - Le string à checker
   * @param len - longueur minimum du string
   */
  public static void verifierLongueurMin(String str, int len) {
    AppUtil.checkString(str);
    if (str.length() <= len) {
      throw new BizzException("' " + str + "' est trop court (min. 3).\nVeuillez corriger");
    }
  }

  /**
   * Check si le string en params ne depasse pas la longueur en params.
   *
   * @param str - Le string à checker
   * @param len - La limite max à atteindre.
   */
  public static void verifierLongueurMax(String str, int len) {
    AppUtil.checkString(str);
    if (str.length() >= len) {
      throw new BizzException("' " + str + "' " + AppConfig.getAppValueOf("TROP_LONG"));
    }
  }

  /**
   * Check si le string en params correspond au Fomart en argument.
   *
   * @param format - Type de check à effectuer
   * @param str - Le string à checker
   * @return true si le format envoyée répond au exigence sinon false.
   */
  private static boolean checkSelonFormat(CheckFormat format, String str) {
    // Aucun string ne doit dépasser 45 caractères.
    AppUtil.verifierLongueurMax(str, 45);
    switch (format) {
      case TEL:
        Matcher check = AppUtil.RegexPattern.TEL.getPattern().matcher(str);
        if (!check.matches()) {
          throw new BizzException("\"" + str + "\" - Ce numéro de telephone est incorrecte");
        }
        return true;

      case SEARCH:
        if (str.isEmpty()) {
          throw new BizzException(AppConfig.getAppValueOf("CHAMP_REQUIS_VIDE"),
              new IllegalArgumentException());
        }
        if (str.length() < 3) {
          throw new BizzException(AppConfig.getAppValueOf("TROP_COURTE_RECHERCHE"));
        }

        return true;
      case MAIL:
        check = AppUtil.RegexPattern.MAIL.getPattern().matcher(str);
        if (!check.matches()) {
          throw new BizzException(AppConfig.getAppValueOf("FORMAT_EMAIL_INVALIDE"));
        }
        return true;

      case MAIL_ETUD_IPL:
        check = AppUtil.RegexPattern.MAIL_ETUD_IPL.getPattern().matcher(str);
        if (!check.matches()) {
          throw new BizzException(AppConfig.getAppValueOf("FORMAT_EMAIL_ETUD_INVALIDE"));

        }
        return true;

      case MAIL_PROF_IPL:
        check = AppUtil.RegexPattern.MAIL_PROF_IPL.getPattern().matcher(str);
        if (!check.matches()) {
          throw new BizzException(AppConfig.getAppValueOf("FORMAT_EMAIL_PROF_INVALIDE"));
        }
        return true;

      case LOGIN:
        AppUtil.verifierLongueurMin(str, 3);
        return true;

      case NOM:
        check = AppUtil.RegexPattern.NOM.getPattern().matcher(str);
        if (!check.matches()) {
          throw new BizzException(AppConfig.getAppValueOf("ALPHANUM_INPUT"));
        }
        return true;

      case MDP:
        // Le mot de passe doit correspond à ce Pattern:
        // ((?=.*[a-z])(?=.*[A-Z])(?=.*[@&|+-_!#$%*ù])(?=.*[0-9]).{4,13} )
        //
        // (?=.*[a-z]) : Au moins un caractères en minuscule.
        // (?=.*[A-Z]) : Au moins un caractère en MAJUSCULE.
        // (?=.*[@&|+-_!#$%*ù]) : Au moins un caractère spéciale
        // [@&|+-_!#$%*ù].
        // (?=.*d) : Au moins un chiffre.
        // {4,13} : Etre de longueur min. 4 et max 13.

        // Je vais construis un regex partie par partie. D'abord le
        // minuscule
        StringBuilder regexPat = new StringBuilder("((?=.*[a-z])");
        // Ensuite le MAJUSCULE
        regexPat.append("(?=.*[A-Z])");
        // Puis, le chiffre
        regexPat.append("(?=.*[0-9])");
        // Pour finir la longueur.
        regexPat.append(".{4,13}");
        // Fermeture du regex
        regexPat.append(")");

        Matcher match = Pattern.compile(regexPat.toString()).matcher(str);
        if (!match.matches()) {
          throw new BizzException(AppConfig.getAppValueOf("FORMAT_MDP_INVALIDE"));
        }
        return true;

      default:
        return false;
    }

  }

  /**
   * Initialise un pseudo chrono.
   */
  public static void startChrono() {
    AppUtil.start = System.currentTimeMillis();
  }

  /**
   * Affiche le resultat du chrono en MILLISEC.
   *
   * @return un temps en SEC.
   */
  public static String resultatEnMs() {
    return ((System.currentTimeMillis() - AppUtil.start)) + " ms.";
  }

  /**
   * Affiche le resultat du chrono en SEC.
   *
   * @return un temps en SEC.
   */
  public static String resultatEnSec() {
    return ((System.currentTimeMillis() - AppUtil.start) / 1000) + " sec.";
  }


  /**
   * @param clazzInstance - La classe contenant les attributs à récuper.
   * @return tous les attributs dans la class voir meme sa class parent.
   */
  public static List<Field> getAllFieldsFrom(Class<?> clazzInstance) {
    List<Field> allField = new ArrayList<Field>();

    // Va checker sa classe parent si existe
    Class<?> superClazz = clazzInstance.getSuperclass();
    if (superClazz != null && !superClazz.isInterface() && superClazz != Object.class) {
      allField.addAll(getAllFieldsFrom(superClazz));
    }
    allField.addAll(Arrays.asList(clazzInstance.getDeclaredFields()));

    return allField;
  }

  public static boolean checkCivilite(Civilite civilite) {
    return civilite.equals(Civilite.Mlle) || civilite.equals(Civilite.Mme)
        || civilite.equals(Civilite.Mr);
  }

  public static boolean checkSexe(Sexe sexe) {
    return sexe.equals(Sexe.F) || sexe.equals(Sexe.M);
  }



  public static boolean checkCompteBancaire(String compteBancaire) {
    return AppUtil.checkSelonFormat(CheckFormat.IBAN, compteBancaire);

  }

  public static boolean checkBic(String bic) {
    return AppUtil.checkSelonFormat(CheckFormat.BIC, bic);
  }

  /**
   * Permet de déterminer l'année académque courante.
   *
   * @return l'année académique courante.
   */
  public static int determinerAnneeAcademiqueCourante() {
    LocalDate now = LocalDate.now();
    int anneeCourante = now.getYear();
    return (now.getMonthValue() < 9) ? anneeCourante : ++anneeCourante;
  }
}
