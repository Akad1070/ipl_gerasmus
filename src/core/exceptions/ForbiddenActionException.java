package core.exceptions;

import core.config.AppConfig;

/**
 * Une exception pour une action requirant d'être connecté ou en mode 'PROF' pour l'exécuter. <br>
 * <b>Renvoie un HttpStatus 403 - FORBIDDEN </b>
 *
 * @author Akad
 */
public class ForbiddenActionException extends AppException {
  private static final long serialVersionUID = -3741111363400669626L;

  /**
   * Envoie un msg demandant d'être connectée comme professeur.
   */
  public ForbiddenActionException() {
    this(AppConfig.getAppValueOf("ACTION_FOR_PROF_ONLY"));
  }


  public ForbiddenActionException(String msg, Throwable cause) {
    super(msg, cause);
  }


  public ForbiddenActionException(String msg) {
    super(msg);
  }


  public ForbiddenActionException(Throwable cause) {
    super(cause);
  }



}
