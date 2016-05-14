package domaine.dto;

import domaine.bizz.interfaces.BaseEntite;

/**
 * C'est la DTO(Sac de données) des documents à signés pour un programme et un type de programme
 * donnée.
 *
 * @author candy
 *
 */
public interface DocumentDto extends BaseEntite {
  public enum Genre {
    DEPART, RETOUR;
  }

  /**
   * @return le genre de document (départ | retour).
   */
  Genre getGenre();

  /**
   * @return le nom du document.
   */
  String getNom();

  /**
   * @return le programme du document.
   */
  ProgrammeDto getProgramme();


  /**
   * @return le type programme du document.
   */
  TypeProgrammeDto getTypeProgramme();

  /**
   * définit le genre.
   *
   * @param genre - le genre de document.
   */
  void setGenre(Genre genre);

  /**
   * définit le genre.
   *
   * @param genre - le genre de document.
   */
  void setGenre(String genre);

  /**
   * définit le nom.
   *
   * @param nom - le nom du document.
   */
  void setNom(String nom);


  /**
   * Définis le programme du document.
   *
   * @param programme - le {@link ProgrammeDto}
   */
  void setProgramme(ProgrammeDto programme);


  /**
   * Définis le type programme du document.
   *
   * @param typeProgramme - le {@link TypeProgrammeDto}
   */
  void setTypeProgramme(TypeProgrammeDto typeProgramme);

  /**
   * @return la mobilité qui a signé le document.
   */
  ChoixMobiliteDto getMobilite();

  /**
   * Définis la mobilité qui a signé le document.
   *
   * @param mobilite - le {@link ChoixMobiliteDto}
   */
  void setMobilite(ChoixMobiliteDto mobilite);

}
