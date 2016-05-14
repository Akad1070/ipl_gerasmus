package domaine.bizz.interfaces;

import domaine.dto.ProgrammeDto;
import util.AppUtil;

public interface ProgrammeBizz extends ProgrammeDto {


  /**
   * Vérifie si tous les champs sont valides.
   *
   */
  default void checkBeforeInsert() {
    AppUtil.checkString(getNom());
  }
}
