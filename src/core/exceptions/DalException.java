package core.exceptions;

/**
 * Pour toute exception survenant dans la couche DAL. <br>
 * Elle herite de {@link FatalException} car celle-ci empeche l'app de fonctionner normalement. <br>
 * Elle est catché par l'UCC et nulle part ailleurs.
 *
 * @author Akaad
 */
public class DalException extends FatalException {
  private static final long serialVersionUID = 1L;

  public DalException() {
    super();
  }

  public DalException(String msg, Throwable cause) {
    super(msg, cause);
    // TODO Auto-generated constructor stub
  }

  public DalException(String msg) {
    super(msg);
    // TODO Auto-generated constructor stub
  }

  public DalException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }



}
