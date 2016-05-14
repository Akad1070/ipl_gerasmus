package domaine.bizz.interfaces;

import domaine.dto.DemandeMobiliteDto;
import util.AppUtil;

public interface DemandeMobiliteBizz extends DemandeMobiliteDto {


  /**
   * Vérifie si tous les champs sont valides.
   *
   */
  default void checkBeforeInsert() {
    AppUtil.checkObject(getDateIntroduction());
    AppUtil.checkPositive(getAnneeAcademique());
    AppUtil.checkObject(getEtudiant());
  }


}
