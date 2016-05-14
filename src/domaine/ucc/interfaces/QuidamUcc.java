package domaine.ucc.interfaces;

import core.exceptions.BizzException;
import domaine.dto.UserDto;

/**
 * Chef d'orchestre pour les actions du Quidam.
 *
 * @author Aurelien
 */
public interface QuidamUcc {

  /**
   * Permet d'inscrire une personne dans la BD si toutes les données sont valides.
   *
   * @param user - le {@link UserDto} à inscrire
   * @return un {@link UserDto} si l'inscription s'est bien déroulée.
   * @throws BizzException avec un message specifiant la raison de l'echec.
   */
  UserDto inscrire(UserDto user);


  /**
   * Permet à un utilisateur de se connecter si ses identifiants sont valides.
   *
   * @param user - la {@link UserDto} contenant le login et le MDP
   * @return un {@link UserDto}
   * @throws BizzException avec un message specifiant la raison.
   */
  UserDto seConnecter(UserDto user);
}
