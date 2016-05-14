package domaine.dto;

import java.time.LocalDate;

import domaine.bizz.interfaces.BaseEntite;

/**
 * C'est la DTO(Sac de données) de Utilisateur. Contient toutes les infos nécessaires sur
 * l'utilisateur.
 *
 * @author Akad
 */
public interface UserDto extends BaseEntite {


  /**
   * @return boolean indiquant si l'user est un professeur.
   */
  boolean isProf();

  /**
   * @return l'identifiant de connexion lié à l'utilisateur.
   */
  String getIdentifiant();

  /**
   * @return le pseudo de l'utilisateur.
   */
  String getPseudo();

  /**
   * @return le nom de l'utilisateur.
   */
  String getNom();

  /**
   * @return le prénom de l'utilisateur.
   */
  String getPrenom();

  /**
   * @return le mdp de l'utilisateur.
   */
  String getMdp();

  /**
   * @return la date d'inscription de l'utilisateur.
   */
  LocalDate getDateInscription();


  /**
   * @return le departement de l'utilisateur.
   */
  DepartementDto getDepartement();


  /**
   * @return email de l'utilisateur.
   */
  String getMail();


  /**
   * Definis un mot de passe pour l'user.
   *
   * @param mdp - le nouveau mot de passe.
   */
  void setMdp(String mdp);


  /**
   * Assigne un nom pour l'user.
   *
   * @param nom - le nouveu nom.
   */
  void setNom(String nom);

  /**
   * Assigne un prenom pour l'user.
   *
   * @param prenom - le nouveau prénom.
   */
  void setPrenom(String prenom);


  /**
   * Assigne un pseudo pour l'user.
   *
   * @param pseudo - le nouveau pseudo.
   */
  void setPseudo(String pseudo);


  /**
   * Assigne un mail pour l'user.
   *
   * @param mail - le nouveau mail.
   */
  void setMail(String mail);


  /**
   * Définis si l'user est un prof ou pas. <br/>
   *
   * @param val - boolean
   */
  void setProf(boolean val);


  /**
   * Definis la date d'inscription pour l'user.
   *
   * @param date la nouvelle date.
   */
  void setDateInscription(LocalDate date);


  /**
   * Definis la département l'user.
   *
   * @param departement - le nouveau {@link DepartementDto}.
   */
  void setDepartement(DepartementDto departement);



  /**
   * Definis l'identifiant de connexion lié à l'user.
   *
   * @param identifiant - le nouvel identifiant.
   */
  void setIdentifiant(String identifiant);

  // TODO set et get avatar.

}
