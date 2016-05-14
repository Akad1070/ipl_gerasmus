package domaine.bizz.interfaces;

import domaine.dto.DepartementDto;
import util.AppUtil;

public interface DepartementBizz extends DepartementDto {


  /**
   * Vérifie si tous les champs sont valides.
   *
   */
  default void checkBeforeInsert() {
    AppUtil.checkString(getCode(), "Le code du departement doit être definie.");
    AppUtil.checkString(getNom(), "Il faut assigner un nom à ce departement.");
  }
}
