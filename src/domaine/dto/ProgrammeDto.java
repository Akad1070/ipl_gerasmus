package domaine.dto;

import domaine.bizz.interfaces.BaseEntite;


public interface ProgrammeDto extends BaseEntite {

  /**
   * @return Le nom du programme.
   */
  public String getNom();

  /**
   * défini le nom du programme.
   *
   * @param nom - Le nom du progamme.
   */
  public void setNom(String nom);

}
