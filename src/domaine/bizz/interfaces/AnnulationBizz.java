package domaine.bizz.interfaces;

import domaine.dto.AnnulationDto;
import util.AppUtil;

public interface AnnulationBizz extends AnnulationDto {

  /**
   * Vérifie si tous les champs sont valides.
   *
   */
  default void checkBizz() {
    AppUtil.checkObject(getCreateur(), "Le createur de cette motivation doit être défini");
    AppUtil.checkPositive(getCreateur().getId(),
        "Le createur de cette motivation doit être défini");
    // Il y aura pas d'ID si c'est une annulation qui vient d'être crée
    if (getId() == 0) {
      AppUtil.checkString(getMotif(), "Le motivation de cette annulation doit être défini");
    }
  }

}
