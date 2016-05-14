package core.exceptions;

import core.AppContext;
import core.injecteur.InjecteurDependance.Injecter;

/**
 * Permet de gerer les exceptions survenant dans l'application. Peut être de type Business ou Fatal.
 *
 * @author Akad
 *
 */
public class AppException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  @Injecter
  private AppContext context;

  public AppException() {
    super();
  }


  public AppException(String msg) {
    super(msg);
  }

  public AppException(Throwable cause) {
    super(cause);
  }


  public AppException(String msg, Throwable cause) {
    super(msg, cause);
  }



}
