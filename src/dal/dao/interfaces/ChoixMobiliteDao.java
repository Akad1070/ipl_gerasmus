package dal.dao.interfaces;

import domaine.dto.AnnulationDto;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;

import java.util.List;

/**
 * Gert les CRUD de choix_mobilite.
 *
 * @author candy
 *
 */
public interface ChoixMobiliteDao extends Dao<ChoixMobiliteDto> {

  /**
   * Permet d'nsérer une mobilité.
   *
   * @param mob - La mobilité à inserer.
   * @return un {@link ChoixMobiliteDto} si l'insertion s'est bien déroulée.
   */
  ChoixMobiliteDto insererMobilite(ChoixMobiliteDto mob);

  /**
   * Permet de lister les mobilités d'une demande en fonction de l'état de la mobilité et de l'année
   * académique de la demande.
   *
   * @param mob - Contenant l'état du choix de mobilité.
   * @return la liste des {@link ChoixMobiliteDto} qui correspondent aux critères.
   */
  List<ChoixMobiliteDto> findMobilityByAnneeAndEtat(ChoixMobiliteDto mob);

  /**
   * Permet de récupérer le choix de mobilité d'un étudiant en cours de traitement. C-à-d une
   * mobilité dont l'état n'est ni "ANNULE" ni "INTRODUITE" ni "PAYEMENT SOLDE DEMANDE".
   *
   * @param dem - La demande de la mobilité avec l'année académique.
   * @return la {@link ChoixMobiliteDto} correspondant aux critères. Une seul max normalement.
   */
  ChoixMobiliteDto findProcessingMobility(DemandeMobiliteDto dem);

  /**
   * Permet de listes les choix de mobilité d'une demande.
   *
   * @param demande - La demande de mobilité avec son id.
   * @return une liste {@link ChoixMobiliteDto} correspondant a la demande.
   */
  List<ChoixMobiliteDto> listerMobiliteDuneDemande(DemandeMobiliteDto demande);



  /**
   * Permet de lister les choix de mobilité avec au moins une demande de payement effectuée.
   *
   * @param annee - L'année académique pour laquelle on désire récupérer les données.
   * @return La liste de {@link ChoixMobiliteDto} récupérée en DB.
   */
  List<ChoixMobiliteDto> listerMobiliteAvecDemandePayement(int annee);

  /**
   * Permet d'annuler un choix de mobilité.
   *
   * @param mobilite - Un {@link ChoixMobiliteDto} contenant le num de candidature et une
   *        {@link AnnulationDto} avec son ID ou le motif.
   * @return un {@link ChoixMobiliteDto} si la mobilité était présente et a été annulé.
   */
  ChoixMobiliteDto annulerChoixMobilite(ChoixMobiliteDto mobilite);

  /**
   * Permet de mettre à jour l'état de la mobilité.
   *
   * @param mobilite - La mobilité dont l'etat dois être changer.
   * @return - un {@link ChoixMobiliteDto} si la modification a été effectuée.
   */
  ChoixMobiliteDto updateEtat(ChoixMobiliteDto mobilite);

  /**
   * Permet de savoir si un étudiant a une demande de mobilité qui a été confirmée. On ne prend pas
   * en compte les mobilités annulé.
   *
   * @param etudiant - L'étudiant pour lequel on veut savoir s'il a une mobilité confirmée.
   * @return true si l'étudiant à une mobilité confirmé.
   */
  boolean hasMobiliteConfirmed(UserDto etudiant);


  /**
   * Permet de savoir si un partenaire est lié à une ou plusieurs mobilités.
   *
   * @param part - Le partenaire pour lequel on veut savoir est lié.
   * @return true si le partenaire est lié à une ou plusieurs mobilités.
   */
  boolean isLinked(PartenaireDto part);



}
