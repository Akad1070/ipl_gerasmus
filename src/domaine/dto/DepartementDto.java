package domaine.dto;

import domaine.bizz.interfaces.BaseEntite;

public interface DepartementDto extends BaseEntite {

  /**
   * @return le nom du département.
   */
  String getNom();

  /**
   * @return le code du département.
   */
  String getCode();


  /**
   * Enregistre le nom du département.
   *
   * @param nom - le nouveau nom.
   */
  void setNom(String nom);

  /**
   * Enregistre le code du département.
   *
   * @param code - le nouveau code.
   */
  void setCode(String code);

}
