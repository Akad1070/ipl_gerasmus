package domaine.dto;

import domaine.bizz.interfaces.BaseEntite;

/**
 * C'est la DTO(Sac de données) d'un logiciel. Contient toutes les infos nécessaires sur le
 * logiciel.
 *
 * @author candy
 *
 */
public interface LogicielDto extends BaseEntite {

  /**
   * @return le nom du logiciel.
   */
  String getNom();

  /**
   * Definis nom du logiciel.
   *
   * @param nom - le nouveau nom du logiciel.
   */
  void setNom(String nom);
}
