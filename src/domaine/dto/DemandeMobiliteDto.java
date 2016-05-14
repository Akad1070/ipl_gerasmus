package domaine.dto;

import domaine.bizz.interfaces.BaseEntite;

import java.time.LocalDate;
import java.util.List;


/**
 * C'est la DTO(Sac de données) pour une candidature de mobilite pour un étudiant. <br>
 * Contient toutes les infos sur cette candidature :
 * <ul>
 * <li>L'année d'introduction en</li>
 * <li>L'année académique en String</li>
 * <li>L'étudiant l'ayant introduite</li>
 * </ul>
 *
 */
public interface DemandeMobiliteDto extends BaseEntite {

  /**
   * La date d'introduction.
   *
   * @return La date d'introduction.
   */
  public LocalDate getDateIntroduction();

  /**
   * La date d'introduction.
   *
   * @param date - La date d'introduction.
   */
  public void setDateIntroduction(LocalDate date);

  /**
   * L'année académique.
   *
   * @return L'année académique.
   */
  public int getAnneeAcademique();

  /**
   * L'année académique.
   *
   * @param annee - L'année académique.
   */
  public void setAnneeAcademique(int annee);

  /**
   * L'étudiant faisant la demande.
   *
   * @return L'étudiant faisant la demande.
   */
  public UserDto getEtudiant();

  /**
   * L'étudiant faisant la demande.
   *
   * @param etud - L'étudiant faisant la demande.
   */
  public void setEtudiant(UserDto etud);

  /**
   * Définis pour cette demande, les choix de mobilités.
   * @param choixMobilites - La liste de choix.
   */
  public void setChoixMobilites(List<ChoixMobiliteDto> choixMobilites);

  List<ChoixMobiliteDto> getChoixMobilites();


}
