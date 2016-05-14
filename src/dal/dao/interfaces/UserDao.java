package dal.dao.interfaces;

import domaine.dto.DemandeMobiliteDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;

import java.util.List;

/**
 * Gert les CRUD de User.
 *
 * @author Aurelien
 */
public interface UserDao extends Dao<UserDto> {

  /**
   * Permet d'insérer un utilisateur dans la BD.
   *
   * @param user - La {@link UserDto}
   * @return Un {@link UserDto} si l'inscription s'est bien déroulée.
   */
  UserDto inscrire(UserDto user);


  /**
   * Permet de récupérer un utilsiateur grace à son pseudo.
   *
   * @param user - Un {@link UserDto} contenant au moins le pseudo.
   * @return Un {@link UserDto} contenant toutes ses infos.
   */
  UserDto getUserByPseudo(UserDto user);


  /**
   * Permet de récupérer un utilsiateur grace à son mail.
   *
   * @param user - Un {@link UserDto} contenant au moins le mail.
   * @return Un {@link UserDto} contenant toutes ses infos.
   */
  UserDto getUserByMail(UserDto user);

  /**
   * Permet de récupérer des étudiants en fonction de leur nom ou prénom fourni dans le
   * {@link UserDto}.
   *
   * @param user - Un {@link UserDto} contenant le nom, prénom ou ID.
   * @return Une liste de {@link UserDto} correspondant au param fournis.
   */
  List<UserDto> findByName(UserDto user);

  /**
   * Permet d'btenir toutes les infos sur le créateur d'un partenaire.
   *
   * @param pat - Le {@link PartenaireDto} contenant un {@link UserDto} avec un id.
   * @return Une {@link UserDto} remplie.
   */
  UserDto findByPartenaire(PartenaireDto pat);


  /**
   * Permet d'btenir toutes les infos d'un étudiant grace à une demande de mobilité.
   *
   * @param candidature - La {@link DemandeMobiliteDto} contenant un numéro de candidature.
   * @return Une {@link UserDto} remplie.
   */
  UserDto findByDemandeMobilite(DemandeMobiliteDto candidature);


}
