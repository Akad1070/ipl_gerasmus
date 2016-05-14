package domaine.ucc.interfaces;

import core.exceptions.BizzException;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.DocumentDto;
import domaine.dto.LogicielDto;
import domaine.dto.PartenaireDto;
import domaine.dto.PaysDto;
import domaine.dto.ProgrammeDto;
import domaine.dto.TypeProgrammeDto;
import domaine.dto.UserDto;
import domaine.dto.UserInfoDto;

import java.util.List;
import java.util.Map;

/**
 * Chef d'orchestre pour toutes les actions possibles de l'étudiant.
 *
 * @author Aurelien.
 */
public interface EtudiantUcc {


  /**
   * Permet d'introduire les choix de mobilité d'un étudiant.
   *
   * @param demande - La demande de mobilité contenant tout les choix de l'étudiant.
   * @return true si l'introduction de la demande s'est bien déroulée.
   * @throws BizzException avec un message specifiant la raison de l'echec.
   */
  boolean declarerChoix(DemandeMobiliteDto demande);


  /**
   * Permet d'ajouter un partenaire dans la BD en spécifiant le createur avec son ID et son boolean
   * ({@link UserDto#isProf})
   *
   * @param createur - L'utilisateur qui désire ajouter le partenaire.
   * @param partenaire - Le {@link PartenaireDto} à ajouter.
   * @return un {@link PartenaireDto} si lajout s'est bien déroulée.
   * @throws BizzException avec un message specifiant la raison de l'echec.
   */
  PartenaireDto ajouterPartenaire(UserDto createur, PartenaireDto partenaire);


  /**
   * Permet d'indiquer qu'un choix de mobilité est annulé et la raison de l'annulation.
   *
   * @param mobilite - Une {@link ChoixMobiliteDto} contenant le motif d'annulation ainsi que
   *        l'utilisateur qui désire annuler la mobilité avec son ID et le boolean
   *        {@link UserDto#isProf()}.
   * @throws BizzException avec un message specifiant la raison de l'echec.
   */
  ChoixMobiliteDto annulerChoixMobilite(ChoixMobiliteDto mobilite);


  /**
   * Permet d'encoder les infos d'un utilisateur.
   *
   * @param infos - Les informations de l'utilisateur contenant un {@link UserDto}.
   * @return Les nouvelles informations.
   */
  UserInfoDto encoderInformationsPersonnelles(UserInfoDto infos);


  /**
   * Permet de récupérer un utilisateur grace à son id.
   *
   * @param user - Une {@link UserDto}.
   * @return Une {@link UserDto} rempli avec les données de l'utilisateur correspondant.
   */
  UserDto recupererUserParId(UserDto user);


  /**
   * Permet de recuperer les informations d'un utilisateur grace à son id.
   *
   * @param user - Une {@link UserDto}.
   * @return Une {@link UserInfoDto} rempli avec les informations de l'utilisateur.
   */
  UserInfoDto recupererUserInfoParUId(UserDto user);


  /**
   * Permet de récupérer un partenaire grace à son id.
   *
   * @param partenaire - Une {@link PartenaireDto}.
   * @return Une {@link PartenaireDto} rempli avec les informations du partenaire.
   */
  PartenaireDto recupererPartenaireParId(PartenaireDto partenaire);


  /**
   * Permet d'effectuer une recherhche sur les mobilites avec les champs de recherche prédéfinis
   * remplis avec l'objet de recherche.
   *
   * @param mobilite - Une {@link ChoixMobiliteDto} contenant l' Etat.
   * @return une liste de {@link ChoixMobiliteDto} correspondant au critère de recherche fournis.
   */
  List<ChoixMobiliteDto> rechercherMobilite(ChoixMobiliteDto mobilite);


  /**
   * Permet de lister les étudiants en fonction du nom ou du prénom.
   *
   * @param user - {@link UserDto}.
   * @return Une liste d' {@link UserDto} correspondant aux critères de recherche fournis.
   */
  List<UserDto> rechercherUser(UserDto user);


  /**
   * Permet de lister des partenaires en fonction de leur nom, pays ou ville.
   *
   * @param partenaire - {@link PartenaireDto}.
   *
   * @return Une de {@link PartenaireDto} correspondant au critère de recherche fournis.
   */
  List<PartenaireDto> rechercherPartenaire(PartenaireDto partenaire);


  /**
   * Permet de lister tous les départements présents en DB.
   *
   * @return Une liste de {@link DepartementDto}.
   */
  List<DepartementDto> listerDepartements();


  /**
   * Permet de lister les pays de tous les partenaires présents en DB.
   *
   * @return Une liste des {@link PaysDto} triée par ordre aplha-croissant.
   */
  List<PaysDto> listerPays();


  /**
   * Permet de lister les programme présents en DB.
   *
   * @return Une liste des {@link ProgrammeDto} récupérés.
   */
  List<ProgrammeDto> listerProgrammes();


  /**
   * Permet de lister les types de programmes présents en DB.
   *
   * @return La liste des {@link TypeProgrammeDto} récupérés.
   */
  List<TypeProgrammeDto> listerTypesProgramme();


  /**
   * Permet de récupérer les annnées académiques enregistrées.
   *
   * @return La liste des années académiques récupérés.
   */
  List<Integer> listerAnneesAcademiqueDejaIntroduites();

  /**
   * Permet de confimer une mobilité auprès d'un partenaire.
   *
   * @param mobilite - La mobilite qu'on désire confirmé contenant le partenaire auquel elle sera
   *        liée.
   *
   * @return La {@link ChoixMobiliteDto} si la confimation s'est bien déroulée.
   */
  ChoixMobiliteDto confirmerPartenaire(ChoixMobiliteDto mobilite);


  /**
   * Recuperer toutes les choix mobilites introduites pour cette demande.
   *
   * @param demande - Une {@link DemandeMobiliteDto} contenant son id
   * @return List<{@link ChoixMobiliteDto}> correspondant au numero de candidature.
   */
  List<ChoixMobiliteDto> recupererMobilitesPourDemande(DemandeMobiliteDto demande);


  /**
   * Permet de changer l'état d'une mobilité.
   *
   * @param mobilite - La mobilité qui doit changer d'état.
   * @return La {@link ChoixMobiliteDto} si le changement d'état s'est bien déroulé.
   */
  ChoixMobiliteDto changerEtat(ChoixMobiliteDto mobilite);


  /**
   * Recuperer les choix de mobilite introduits par un étudiant.
   *
   * @param etudiant - Un {@link UserDto} contenant au moins l'Id de l'étduaint.
   * @return List<{@link ChoixMobiliteDto}> de toutes ses choix introduits
   */
  List<DemandeMobiliteDto> listerMobilitesPourEtud(UserDto etudiant);


  /**
   * Permet de lister tous les partenaires selectionnable d'un étudiant. On tiens compte du
   * départemment de l'étudiant.
   *
   * @return La liste de {@link PartenaireDto} récupérés.
   */
  List<PartenaireDto> listerPartenairesPourEtudiant(UserDto etudiant);



  /**
   * Permet de récupérer l'état des documents pour une mobilité.
   *
   * @param mobilite - La mobilite en question contenant son num de candidature et préference.
   * @return Une Map avec pour clé une <{@link DocumentDto}> et pour valeur un Booléan indiquant si
   *         le document à été signé ou pas.
   */
  Map<DocumentDto, Boolean> listerEtatDocuments(ChoixMobiliteDto mobilite);

  /**
   * Permet de lister l'état d'encodage des logiciel d'une mobilité.
   *
   * @param mobilite - La mobilité concernée contenant son num de candidature et le numéro de
   *        préference.
   * @return Une Map avec pour clé des {@link LogicielDto} et pour valeur un Boolean indiquant si
   *         l'encodage a été fait ou pas.
   */
  Map<LogicielDto, Boolean> listerEtatLogicielsEncodes(ChoixMobiliteDto mobilite);



}
