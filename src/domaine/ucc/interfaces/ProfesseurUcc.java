package domaine.ucc.interfaces;

import domaine.dto.AnnulationDto;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.DocumentDto;
import domaine.dto.PartenaireDto;

import java.util.List;
import java.util.Map;

public interface ProfesseurUcc extends EtudiantUcc {

  /**
   * Permet gérer l'encodage des logiciels d'une demande.
   *
   * @param mobilite - La mobilité pour laquelle on désire gérer l'encodage des logiciels.
   * @return true si l'encodage des logiciel a été effectuée.
   */
  boolean gererEncodageLogiciels(ChoixMobiliteDto mobilite);


  /**
   * Permet de lister les demandes selon les departements en paramètre.
   *
   * @param departements - Les departements concernés par le listing.
   * @return La liste des {@link DemandeMobiliteDto} récupérées en DB.
   */
  List<DemandeMobiliteDto> listerDemandeMobilites(List<DepartementDto> departements);





  /**
   * Permet de de confirmer la signature d'un document.
   *
   * @param mobilite - La mobilité pour laquelle on désire confirmer la signature d'un document.
   * @return La liste des <{@link DocumentDto}> .
   */
  List<DocumentDto> encoderSignatureDocuments(ChoixMobiliteDto mobilite);


  /**
   * Permet de récupérer les mobilités pour lesquelles au moins une demande de payement a été
   * effcetuée.
   *
   * @param annee - L'année académique pour laquelle on désire récupérer les mobilités.
   * @return Une Map avec pour clé une <{@link ChoixMobiliteDto}> et pour valeur un Integer. <br>
   *         S'il vaut 1 : Uniquement la demande pour le 1er payement a été faite. <br>
   *         S'il vaut 2 : Les 2 demandes de payements ont été faites.
   */
  Map<ChoixMobiliteDto, Integer> listerDemandeDePayement(int annee);

  /**
   * Permet de récupérer la liste des motif d'annulation écris par les professeur.
   *
   * @return La liste des <{@link AnnulationDto}> .
   */
  List<AnnulationDto> listerMotifsAnnulations();

  /**
   * Permet de changer la visibilité d'un partenaire. S'il est visible il sera caché et vice-versa.
   * NB : Un partenaire pourra être caché uniquement s'il n'est lié à aucune mobilité.
   *
   * @return le partenaire qui a changé de visibilité.
   */
  PartenaireDto changerVisibiliterPartenaire(PartenaireDto part);


  /**
   * Permet de lister tous les partenaires selectionnables par étudiants. Les partenaires invisibles
   * ne seront pas selectionnés.
   *
   * @return La liste de {@link PartenaireDto} récupérés..
   */
  List<PartenaireDto> listerPartenairesSelectionnablesPourTous();


}
