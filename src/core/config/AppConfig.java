package core.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import core.AppContext;
import core.AppContext.Profil;
import core.exceptions.FatalException;

/**
 * Gere la configuration de l'Application et contient aussi le {@link AppContext} actuel.<br>
 * Est aussi distribuée dans toute l'Application. <br>
 * Ne doit pas être instanciée et ne contient que des méthodes {@code static}
 */
public class AppConfig {
  public static final String RES_FOLDER = "ressources/";
  private static final Properties properties = new Properties();
  private static final Properties appProps = new Properties();
  private static Profil mode;


  /**
   * Charge le contenu du .properties. Recupere le contexte.
   *
   * @param mode - Le profil actuel de l'App.
   * @throws IOException - Si le fichier n'est en accessible LECTURE.
   * @throws FatalException - Si n'a pas pu charger le .properties.
   */
  public static void loadProperties(Profil mode) throws IOException {
    AppConfig.mode = mode;
    try (InputStream input =
        new BufferedInputStream(new FileInputStream(AppConfig.RES_FOLDER + mode.getPropUrl()))) {
      AppConfig.properties.load(input);
    } catch (IOException exception) {
      throw new FatalException("Could not load " + AppConfig.mode.getPropUrl());
    }

    try (InputStream input =
        new BufferedInputStream(new FileInputStream(AppConfig.RES_FOLDER + "app.properties"))) {
      AppConfig.appProps.load(input);
      AppConfig.properties.load(input);
    } catch (IOException exception) {
      throw new FatalException("Could not load app.properties");
    }

  }


  public static Properties getProps() {
    return properties;
  }

  /**
   * @return le {@link Profil} actuel.
   */
  public static Profil getProfil() {
    return AppConfig.mode;
  }

  /**
   * Envoie la valeur se trouvant dans le .properties.
   *
   * @param cle - La clé servant d'identifiant pour cettte valeur.
   * @return La valeur correspondant à la key
   * @throws FatalException - Si la key fournie n'est pas le .properties
   */
  public static String getValueOf(String cle) {
    String returnValue = AppConfig.properties.getProperty(cle);
    if (returnValue == null) {
      throw new FatalException("Could not find '" + cle + "' in " + AppConfig.mode.getPropUrl());
    }
    return returnValue;
  }


  /**
   * @param cle - La clé servant d'identifiant pour cettte valeur.
   * @see AppConfig#getValueOf(String)
   * @return Un conversion de la valeur en Int.
   */
  public static int getInt(String cle) {
    String val = AppConfig.getValueOf(cle);

    try {
      return Integer.parseInt(val);
    } catch (NumberFormatException exception) {
      throw new FatalException("There is no integer matching the key '" + cle + "' in "
          + AppConfig.mode.getPropUrl(), exception);
    }
  }

  /**
   * Récolte tous les contenus de l'app.properties.
   *
   * @return une map contenant le contenu d' app.properties
   */
  public static Map<String, String> getMapAppProps() {
    return AppConfig.appProps.entrySet().stream()
        .collect(Collectors.toMap(e -> (String) e.getKey(), e -> (String) e.getValue()));
  }

  /**
   * Envoie la valeur se trouvant dans le app.properties.
   *
   * @param cle - La clé servant d'identifiant pour cettte valeur.
   * @return La valeur correspondant à la key
   * @see AppConfig#getValueOf(String)
   * @throws FatalException - Si la key fournie n'est pas le .properties
   */
  public static String getAppValueOf(String cle) {
    String returnValue = AppConfig.appProps.getProperty(cle);
    if (returnValue == null) {
      throw new FatalException("Could not find '" + cle + "' in " + AppConfig.mode.getPropUrl());
    }
    return returnValue;
  }


}
