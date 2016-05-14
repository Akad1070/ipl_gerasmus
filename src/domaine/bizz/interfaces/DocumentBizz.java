package domaine.bizz.interfaces;

import domaine.dto.DocumentDto;
import util.AppUtil;

public interface DocumentBizz extends DocumentDto {



  /**
   * Vérifie si tous les champs sont valides.
   *
   */
  default void checkBeforeInsert() {
    AppUtil.checkObject(getGenre(), "De quel genre est ce document");
    AppUtil.checkString(getNom(), "Il faut un nom à ce document");
    AppUtil.checkObject(getProgramme(), "Pour quel programme est ce document");
    AppUtil.checkObject(getTypeProgramme(), "Pour quel type programme est ce document");
  }

}
