package domaine.dto;

import domaine.bizz.interfaces.BaseEntite;


public interface TypeProgrammeDto extends BaseEntite {

  /**
   * @return Le nom du type de programme.
   */
  public String getNom();

  /**
   * défini le nom du type de programme.
   *
   * @param nom - Le nom du type de progamme.
   */
  public void setNom(String nom);

}
