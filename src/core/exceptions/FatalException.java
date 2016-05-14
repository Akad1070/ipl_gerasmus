package core.exceptions;


/**
 * Pour gerer les exceptions de type FatalException. Celles qui empechent l'application de
 * fonctionner correctement.<br>
 * <b>Renvoie un HttpStatus 500 - INTERNAL SERVER ERROR </b>
 *
 * @author Akad
 */
public class FatalException extends AppException {
  private static final long serialVersionUID = 1L;

  public FatalException() {
    super();
  }

  public FatalException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public FatalException(String msg) {
    super(msg);
  }

  public FatalException(Throwable cause) {
    super(cause);
  }

}
