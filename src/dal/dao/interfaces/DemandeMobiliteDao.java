package dal.dao.interfaces;

import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.UserDto;

import java.util.List;

/**
 * Gert les CRUD de demande_mobiliter.
 *
 * @author candy
 *
 */
public interface DemandeMobiliteDao extends Dao<DemandeMobiliteDto> {

  /**
   * Permet d'insérer une demande de mobilité.
   *
   * @param dem - la demande de mobilité.
   * @return un {@link DemandeMobiliteDto} si l'insertion s'est bien déroulée.
   */
  DemandeMobiliteDto insererDemande(DemandeMobiliteDto dem);


  /**
   * Rechercher une demande de mobilité en fonction d'une année académique et d'un utilisateur.
   *
   * @param demande - Une {@link DemandeMobiliteDto} contenant l'année académique et l'UserDto de
   *        l'étudiant.
   * @return Une {@link DemandeMobiliteDto} avec les champs remplies ou <b>NULL</b>
   */
  DemandeMobiliteDto findByAnneeAndStudent(DemandeMobiliteDto demande);

  /**
   * Permet de lister les demandes de mobilité introduite pour l'année académique courante.
   *
   * @param departements - Une liste contenant les {@link DepartementDto} pour lesquels on désire
   *        récupérer les demandes.
   * @return La liste des {@link DemandeMobiliteDto} récupérées.
   */
  List<DemandeMobiliteDto> listerDemande(List<DepartementDto> departements);

  /**
   * Permet de récupérer toutes les années académiques présentes en DB.
   *
   * @return Une liste les années académiques.
   */
  List<Integer> findAllAnnee();

  /**
   * Permet de retrouver toutes les demandes de mobilité d'un étudiant pour l'année académqiue
   * courante.
   *
   * @param etudiant - Une {@link UserDto} contenant l'ID de l'étudiant en question.
   * @return Une List {@link DemandeMobiliteDto};
   */
  List<DemandeMobiliteDto> findByStudent(UserDto etudiant);

}
