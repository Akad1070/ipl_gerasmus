package domaine.dto;

import domaine.bizz.interfaces.BaseEntite;

/**
 * C'est la DTO(Sac de données) d'un pays. Contient toutes les infos nécessaires sur le pays.
 *
 * @author candy
 *
 */
public interface PaysDto extends BaseEntite {

  /**
   * @return le code du pays.
   */
  String getCode();

  /**
   * @return l'intitulé du pays.
   */
  String getNom();

  /**
   * @return le programme du pays.
   */
  ProgrammeDto getProgramme();


  /**
   * Definis le code du pays.
   *
   * @param codePays - le nouveau code pays.
   */
  void setCode(String codePays);

  /**
   * Définis l'intitulé du pays.
   *
   * @param intitule - le nouveau intitulé.
   */
  void setNom(String intitule);



  /**
   * Définis le programme du pays.
   *
   * @param programme - le {@link ProgrammeDto}
   */
  void setProgramme(ProgrammeDto programme);


}
