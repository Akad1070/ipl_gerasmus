package domaine.bizz.interfaces;

import domaine.dto.PaysDto;
import util.AppUtil;

public interface PaysBizz extends PaysDto {


  /**
   * Vérifie si tous les champs sont valides.
   *
   */
  default void checkBeforeInsert() {
    AppUtil.checkString(this.getCode());
    AppUtil.checkString(this.getNom());
  }
}
