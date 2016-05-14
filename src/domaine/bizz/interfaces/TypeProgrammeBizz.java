package domaine.bizz.interfaces;

import domaine.dto.TypeProgrammeDto;
import util.AppUtil;

public interface TypeProgrammeBizz extends TypeProgrammeDto {


  /**
   * Vérifie si tous les champs sont valides.
   *
   */
  default void checkBeforeInsert() {
    AppUtil.checkString(this.getNom());
  }
}
