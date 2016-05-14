package domaine.dto;

import domaine.bizz.interfaces.BaseEntite;
import util.AppUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * C'est la DTO(Sac de données) pour un choix de mobilite lié à la candidature d'un étudiant. <br>
 * Contient toutes les infos sur ce choix telle que :
 * <ul>
 * <li>La {@link DemandeMobiliteDto} lié à ce choix</li>
 * <li>L'état actuel de ce choix (INTRO,CREE,A_PAYER,....)</li>
 * <li>L'étudiant l'ayant introduite</li>
 * </ul>
 *
 */
public interface ChoixMobiliteDto extends BaseEntite {

  /**
   * Represente l'état actuel du {@link ChoixMobiliteDto}.
   *
   * @author Akad
   */
  public enum Etat {
    INTRO("Introduit"), CONFIRME("Confirmé"), PREPA("En préparation"), A_PAYER(
        "A payer"), DEMANDE_PAYEMENT("Demande de payement envoyé"), A_PAYER_SOLDE(
            "A payer solde"), DEMANDE_PAYEMENT_SOLDE("Demande de payement du solde envoyé");
    /**
     * Représente la position des valeurs de l'Enum. Celle-ci est uniq.
     */
    private final String intitule;


    public static final Map<String, String> listEtat = Arrays.asList(Etat.values()).stream()
        .collect(Collectors.toMap(Etat::name, e -> e.getIntitule()));

    Etat(String intitule) {
      AppUtil.checkString(intitule, "L'état du choix de Mobilité ne peut pas être null");
      this.intitule = intitule;
    }


    public String getIntitule() {
      return intitule;
    }

  }



  /**
   *
   * @return La {@link DemandeMobiliteDto}.
   */
  public DemandeMobiliteDto getDemande();


  /**
   * @param demande - La {@link DemandeMobiliteDto} de ce choix.
   */
  public void setDemande(DemandeMobiliteDto demande);


  /**
   * @return L'état de la mobilité.
   */
  public Etat getEtat();

  /**
   * L'état de la mobilité.
   *
   * @param etat - L'etat de la mobilité.
   */
  public void setEtat(Etat etat);


  /**
   * @return La localité de la mobilité.
   */
  public PaysDto getLocalite();


  void setLocalite(PaysDto localite);

  /**
   * @return Le quadrimestre où la mobilité a lieu.
   */
  public Integer getQuadri();

  /**
   * @param quadri - Le quadrimestre où la mobilité a lieu.
   */
  public void setQuadri(int quadri);

  /**
   * @return Si la mobilité est annulé.
   */
  public boolean isAnnule();

  /**
   * @param annule - Un booleén correspondant à l'annulation de la mobilité.
   */
  public void setAnnule(boolean annule);


  /**
   * @return Le {@link ProgrammeDto} de ce choix.
   */
  public ProgrammeDto getProgramme();


  /**
   * @param programme - Le {@link ProgrammeDto} de ce choix.
   */
  public void setProgramme(ProgrammeDto programme);

  /**
   * @return Le {@link TypeProgrammeDto} de ce choix.
   */
  TypeProgrammeDto getTypeProgramme();


  /**
   * Assigne une type de Programme à ce choix.
   *
   * @param typeProgramme - Un {@link TypeProgrammeDto} contenant l'id.
   */
  void setTypeProgramme(TypeProgrammeDto typeProgramme);


  /**
   * @return Le partenaire.
   */
  public PartenaireDto getPartenaire();

  /**
   * @param partenaire - Le PartennaireDto pour ce choix.
   */
  public void setPartenaire(PartenaireDto partenaire);


  /**
   * @return Le motif d'annulation.
   */
  public AnnulationDto getMotifAnnulation();


  /**
   * @param motifAnnulation - Le motif d'annulation.
   */
  public void setMotifAnnulation(AnnulationDto motifAnnulation);

  /**
   * @return L'id de la préference.
   */
  public Integer getNumPreference();

  /**
   * @param preference - L'id de la préference.
   */
  public void setNumPreference(Integer preference);

  /**
   * @return L'id de la candidature.
   */
  Integer getNumCandidature();

  /**
   * @param candidature - L'id de la candidature.
   */
  void setNumCandidature(int candidature);


  /**
   * @return Liste des documents signées.
   */
  List<DocumentDto> getDocumentsSignes();

  /**
   * @param documentsSignes - La liste des documents signés.
   */
  void setDocumentsSignes(List<DocumentDto> documentsSignes);

  /**
   * @return Liste des logiciel encodés.
   */
  List<LogicielDto> getLogiciels();

  /**
   * @param logiciels - La liste des logiciel encodés.
   */
  void setLogiciels(List<LogicielDto> logiciels);



  void setEtatsDocumentsSignes(Map<DocumentDto, Boolean> etatsDocs);

  /**
   * @return Un Map avec {@link DocumentDto} comme clé et boolean pour indiquer la signature ou non.
   */
  Map<DocumentDto, Boolean> getEtatsDocumentsSignes();


  void setEtatsLogicielsEncodes(Map<LogicielDto, Boolean> etatsLogis);

  /**
   * @return Un Map avec {@link LogicielDto} comme clé et boolean pour indiquer l'encodage ou non.
   */
  Map<LogicielDto, Boolean> getEtatsLogicielsEncodes();

  /**
   * @return true si la mobilité est confirmée.
   */
  boolean isConfirme();
}
