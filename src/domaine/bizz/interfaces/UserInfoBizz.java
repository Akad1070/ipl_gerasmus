package domaine.bizz.interfaces;

import domaine.dto.UserInfoDto;

public interface UserInfoBizz extends UserInfoDto {


  /**
   * Vérifie si tous les champs sont valides.
   *
   */
  public void checkBeforeInsert();

}
