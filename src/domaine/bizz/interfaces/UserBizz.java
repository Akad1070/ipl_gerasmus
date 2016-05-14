package domaine.bizz.interfaces;

import core.exceptions.FatalException;
import domaine.dto.UserDto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.mindrot.jbcrypt.BCrypt;

public interface UserBizz extends UserDto {

  /**
   * Effectue une suite de check sur un utilisateur voulant s'inscrire.
   */
  public void checkBeforeInscription();

  /**
   * Effectue des vérifications sur les identtifians de l'user désirant se connecter.
   */
  public void verifierIdentifiants();

  /**
   * Vérifie si l'identifiant entré par l'utilisateur est un mail.
   *
   * @return TODO true si l'utilisateur entre une adresse mail qui appartient au domaine IPL pour
   *         s'identifier false dans le cas contraire.
   */
  public boolean verifierMail();


  /**
   * Assigner le type d'user (PROF ou ETUD) dependant du mail introduit.
   */
  public void determinerType();


  /**
   * Effectue une encryption du mdp.
   */
  default void crypterMdp() {
    try {
      // Génère un salt avec l'algo de random en faisant tourner 2^12
      // fois.
      String salt = BCrypt.gensalt(12, SecureRandom.getInstance("SHA1PRNG"));
      setMdp(BCrypt.hashpw(getMdp(), salt));
    } catch (NoSuchAlgorithmException exception) {
      throw new FatalException("Erreur lors de la création du SALT", exception);
    }
  }

  /**
   * Vérifie si le mdp entré par l'user correspond avec le mdp crypté dans la BD.
   *
   * @param mdpEnDb - Le mdp stocké en BD de l'utilisateur.
   */
  default boolean verifierMdp(String mdpEnDb) {
    return BCrypt.checkpw(mdpEnDb, getMdp());
  }


}
