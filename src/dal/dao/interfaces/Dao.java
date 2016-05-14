package dal.dao.interfaces;

import java.util.List;


/**
 * Regroupe les methodes communes à toutes Dao.
 *
 * @param <E> Le type Dto que gere la Dao.
 */
public interface Dao<E> {


  /**
   * Find l'object grace à son id.
   *
   * @param id - L'id de la table a rechercher.
   * @return Le type de Dto de la classe appelante.
   */
  E findById(int id);


  /**
   * @return une list contenant que les types de Dto de classe appelante.
   */
  List<E> findAll();


}
