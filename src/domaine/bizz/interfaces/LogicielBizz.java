package domaine.bizz.interfaces;

import domaine.dto.LogicielDto;
import util.AppUtil;

public interface LogicielBizz extends LogicielDto {


  /**
   * Vérifie si tous les champs sont valides.
   *
   */
  default void checkBizz() {
    AppUtil.checkString(getNom(), "Le nom du logiciel doit être défini");
  }

}
