package core.exceptions;


/**
 * Pour gerer les exceptions de type Business. <br>
 * <b>Renvoie un HttpStatus 404 - NOT FOUND</b>
 *
 * @author Akad
 *
 */
public class BizzException extends AppException {

  private static final long serialVersionUID = 1L;

  /**
   * Appele le constructeur vide de AppException.
   */
  public BizzException() {
    super();
  }


  public BizzException(String error, Throwable cause) {
    super(error, cause);
  }



  /**
   * Initialise l'exception avec son msg.
   *
   * @param msg - Msg de l'exception
   */
  public BizzException(String msg) {
    super(msg);
  }

  /**
   * Initialise l'exception avec le genre.
   *
   * @param cause - Le genre d'exception
   */
  public BizzException(Throwable cause) {
    super(cause);
  }



}
