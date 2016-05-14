package dal.dao.interfaces;

import domaine.dto.ChoixMobiliteDto;
import domaine.dto.UserDto;
import domaine.dto.UserInfoDto;

/**
 * Gert les CRUD de UserInfo.
 *
 * @author candy
 *
 */
public interface UserInfoDao extends Dao<UserInfoDto> {

  /**
   * Permet d'ajouter des info à un étudiant en DB.
   *
   * @param userInfo - Les informations à insérer.
   * @return Un {@link UserInfoDto} si l'ajout d'information s'est bien déroulée.
   */
  UserInfoDto insererInfo(UserInfoDto userInfo);

  /**
   * Permet de rechercher les informations d'un étudiant en BD.
   *
   * @param user - L'utilisateur pour lequel on désire récupérer les informations.
   * @return Un {@link UserInfoDto} si il exite des informations pour cette étudiant.
   */
  UserInfoDto rechercherInfo(UserDto user);

  /**
   * Permet de modifier les informations d'un étudiant.
   *
   * @param userInfo - Les info de l'utilisateur à modifier.
   * @return Une {@link UserInfoDto} avec les nouvelles informations.
   */
  UserInfoDto update(UserInfoDto userInfo);

  /**
   * Permet de savoir si l'étudiant concerné par une mobilité a indiquer son compte bancaire.
   *
   * @param mobilite - La mobilité de l'étudiant pour lequel on désire savoir s'il a indiquer son
   *        compte bancaire.
   * @return true si l'étudiant à indiquer son compte bancaire.
   */
  boolean compteBancaireNotNull(ChoixMobiliteDto mobilite);
}
