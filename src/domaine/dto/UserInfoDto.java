package domaine.dto;

import domaine.bizz.interfaces.BaseEntite;

import java.time.LocalDate;

/**
 * C'est la DTO(Sac de données) des informations complementaire des Utilisateur (étudiants).
 * Contient toutes les infos suplémentaire de l'utilisateur.
 *
 * @author candy
 *
 */
public interface UserInfoDto extends BaseEntite {
  public enum Civilite {
    Mr, Mlle, Mme;
  }

  public enum Sexe {
    F, M;
  }

  /**
   * @return la civilité d'un utilisateur (étudiant).
   */
  Civilite getCivilite();

  /**
   * @return le sexe d'un utilisateur (étudiant).
   */
  Sexe getSexe();

  /**
   * @return la nationalité d'un utilisateur.
   */
  String getNationalite();

  /**
   * @return l'adresse d'un utilisateur.
   */
  String getAdresse();

  /**
   * @return le numéro de téléphone d'un utilisateur.
   */
  String getTel();

  /**
   * @return l'adresse mail d'un utilisateur.
   */
  String getEmail();

  /**
   * @return le numéro du compte bancaire de l'utilisateur.
   */
  String getCompteBancaire();

  /**
   * @return le titulaire du compte bancaire de l'utilisateur.
   */
  String getTitulaire();

  /**
   * @return le nom de la banque de l'utilisateur.
   */
  String getBanque();

  /**
   * @return le code bic de l'utilisateur.
   */
  String getBic();

  /**
   * @return la date de naissance de l'utilisateur.
   */
  LocalDate getDateNais();

  /**
   * @return le nombre d'année que l'utilisateur a réussie en supérieur.
   */
  int getNombreAnneeReussie();

  /**
   * Enregistre la civilité.
   *
   * @param civilite - la nouvelle civilité.
   */
  void setCivilite(Civilite civilite);

  /**
   * Enregistre la civilité.
   *
   * @param civilite - la nouvelle civilité.
   */
  void setCivilite(String civilite);


  /**
   * Enregistre le sexe.
   *
   * @param sexe - le sexe.
   */
  void setSexe(String sexe);

  /**
   * Enregistre le sexe.
   *
   * @param sexe - le sexe.
   */
  void setSexe(Sexe sexe);

  /**
   * Enregistre la nationalité.
   *
   * @param nationalite - la nouvelle nationalité.
   */
  void setNationalite(String nationalite);

  /**
   * Enregistre l'adresse.
   *
   * @param adresse - la nouvelle adresse.
   */
  void setAdresse(String adresse);

  /**
   * Enregistre le numéro de téléphone.
   *
   * @param tel - le nouveau numéro de téléphone.
   */
  void setTel(String tel);

  /**
   * Enregistre l'adresse email.
   *
   * @param email - la nouvelle adresse email.
   */
  void setEmail(String email);

  /**
   * Enregistre le compte bancaire.
   *
   * @param compteBancaire - le nouveau numéro de compte.
   */
  void setCompteBancaire(String compteBancaire);

  /**
   * Enregistre le nom du titulaire du compte.
   *
   * @param titulaire - le nouveau nom du titulaire du compte.
   */
  void setTitulaire(String titulaire);

  /**
   * Enregistre le nom de banque du compte.
   *
   * @param banque - le nouveau nom de banque du compte.
   */
  void setBanque(String banque);

  /**
   * Enregistre le code bic du compte.
   *
   * @param bic - le code bic du compte.
   */
  void setBic(String bic);

  /**
   * Enregistre la date de naissance.
   *
   * @param dateNais - la nouvelle date de naissance de l'utilisateur.
   */
  void setDateNais(LocalDate dateNais);

  /**
   * Enregistre le nombre d'année réussie dans l'enseignement superieur.
   *
   * @param nombreAnneeReussie - le nouveau nombre d'année réussie.
   */
  void setNombreAnneeReussie(int nombreAnneeReussie);

  /**
   * @return l'utilisateur concerné par les informations.
   */
  UserDto getUtilisateur();

  /**
   * Enregistre l'utilisateur concerné par les informations.
   *
   * @param utilisateur - L'utilisateur.
   */
  void setUtilisateur(UserDto utilisateur);

}
