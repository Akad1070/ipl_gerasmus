package domaine.bizz.interfaces;

import domaine.dto.ChoixMobiliteDto;
import util.AppUtil;

public interface ChoixMobiliteBizz extends ChoixMobiliteDto {


  /**
   * Vérifie si tous les champs sont valides.
   *
   */
  default void checkBizz() {
    AppUtil.checkPositive(getDemande().getId());
    AppUtil.checkObject(getEtat());
    AppUtil.checkPositive(getQuadri());
    AppUtil.checkPositive(getProgramme().getId());
    AppUtil.checkPositive(getTypeProgramme().getId());

    // Test pour les champs pouvant être omis
    if (getPartenaire() != null) {
      AppUtil.checkPositive(getPartenaire().getId());
    }
    if (getMotifAnnulation() != null) {
      AppUtil.checkPositive(getMotifAnnulation().getId());
    }
  }
}
