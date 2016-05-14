package core.exceptions;

/**
 * Une exception pour toute action non-gerable par l'app. <br>
 * <b>Renvoie un HttpStatus 400 - BAD REQUEST</b>
 *
 * @author Akad
 */
public class UnknownActionException extends AppException {
  private static final long serialVersionUID = -8199496233420506868L;


  public UnknownActionException() {
    this("L'action requise n'existe pas");
  }

  public UnknownActionException(String msg) {
    super(msg);
  }

  public UnknownActionException(String msg, Throwable cause) {
    super(msg, cause);
  }



  public UnknownActionException(Throwable cause) {
    super(cause);
  }



}
