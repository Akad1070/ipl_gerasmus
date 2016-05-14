package domaine.bizz.interfaces;

import domaine.dto.PartenaireDto;
import util.AppUtil;

public interface PartenaireBizz extends PartenaireDto {


  /**
   * Vérifie si tous les champs sont valides.
   *
   */
  default void checkBizz() {
    AppUtil.checkString(getNomLegal(), "Il faut définir le nom légal du partenaire");
    AppUtil.checkString(getNomAffaire(), "Il faut définir le nom d'affaire du partenaire");
    AppUtil.checkPositive(getPays().getId(), "Il faut définir le pays du partenaire");
    if (getRegion() != null) {
      AppUtil.checkString(getRegion());
    }
    AppUtil.checkString(getVille(), "Il faut définir la ville du partenaire");
    AppUtil.checkMail(getMail());
    // AppUtil.checkTel(this.getTelephone());
    // Test pour les champs pouvant être omis
    if (getNomComplet() != null) {
      AppUtil.checkString(getNomComplet());
    }
    if (getCodePostal() != null) {
      AppUtil.checkString(getCodePostal());
    }

  }

}
