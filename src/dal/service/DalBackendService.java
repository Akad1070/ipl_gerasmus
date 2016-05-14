package dal.service;

import java.sql.PreparedStatement;

/**
 * Interface utilisée par les DAOs pour exécuter les query.
 *
 * @author Aurelien
 */
public interface DalBackendService {

  /**
   * Sert à renvoyer un {@link PreparedStatement} pour le query passé en paramètre.
   *
   * @param query - Le query à préparer.
   * @param flag - Le flag pour le prepareStatement.<br>
   *        Pour ommetre le flag, il faut lui donner la valeur 0.
   */
  PreparedStatement createPrepareStatement(String query, int flag);


  /**
   * Le flag est omis donc vaut directement <b>0</b>.
   * @param query - Le query à préparer.
   * @see #createPrepareStatement(String, int)
   *
   */
  PreparedStatement createPrepareStatement(String query);


}
