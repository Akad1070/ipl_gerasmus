package domaine.dto;

import domaine.bizz.interfaces.BaseEntite;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface PartenaireDto extends BaseEntite {

  public enum TypeOrganisation {
    TPE, PME, ETU, TGE;
    public static final List<String> listOrgas =
        Arrays.stream(TypeOrganisation.values()).map(e -> e.name()).collect(Collectors.toList());
  }

  /**
   * Renvoi la visibilite du partenaire.
   *
   * @return la visibilitee du partenaire.
   */
  boolean isVisible();

  /**
   * Renvoi le nom légal du partenaire.
   *
   * @return le nom légal du partenaire.
   */
  String getNomLegal();

  /**
   * Renvoi le nom complet du partenaire.
   *
   * @return le nom complet du partenaire.
   */
  String getNomComplet();

  /**
   * Renvoi le nom d'affaire du partenaire.
   *
   * @return le nom d'affaire du partenaire.
   */
  String getNomAffaire();

  /**
   * Renvoi le type d'organisation du partenaire.
   *
   * @return le le type d'organisation du partenaire.
   */
  TypeOrganisation getTypeOrganisation();

  /**
   * Renvoi le nombre d'employé du partenaire.
   *
   * @return le nombre d'employé du partenaire.
   */
  Integer getNbEmploye();

  /**
   * Renvoi l'adresse du partenaire.
   *
   * @return l'adresse du partenaire.
   */
  String getAdresse();


  /**
   * @return le pays du partenaire.
   */
  PaysDto getPays();


  /**
   * Renvoi la ville du partenaire.
   *
   * @return la ville du partenaire.
   */
  String getVille();

  /**
   * Renvoi le code postal du partenaire.
   *
   * @return le code postal du partenaire.
   */
  String getCodePostal();

  /**
   * Renvoi l'adresse mail du partenaire.
   *
   * @return l'adresse mail du partenaire.
   */
  String getMail();

  /**
   * Renvoi la région du partenaire.
   *
   * @return la région du partenaire.
   */
  String getRegion();

  /**
   * Renvoi le site web du partenaire.
   *
   * @return le site web du partenaire.
   */
  String getSiteWeb();

  /**
   * Renvoi le numéro de téléphone du partenaire.
   *
   * @return le numéro de téléphone du partenaire.
   */
  String getTelephone();

  /**
   * Détermine si le partenaire est selectionnable par tout les utilisateur.
   *
   * @return Boolean indiquant si le partenaire est selectionable.
   */
  boolean estSelectionable();

  /**
   * Renvoi le créateur du partenaire.
   *
   * @return Le créateur du partenaire.
   */
  UserDto getCreateur();

  /**
   * Enregistre la visibilité.
   *
   * @param visibilite - La nouvelle visibilite.
   */
  void setVisible(boolean visibilite);

  /**
   * Enregistre le nom.
   *
   * @param nom - Le nouveau nom.
   */
  void setNomLegal(String nom);

  /**
   * Enregistre le site web.
   *
   * @param siteWeb - Le nouveau site web.
   */
  void setSiteWeb(String siteWeb);

  /**
   * Enregistre la région.
   *
   * @param region - La nouvelle région.
   */
  void setRegion(String region);

  /**
   * Enregistre le nom.
   *
   * @param nom - Le nouveau nom.
   */

  void setNomcomplet(String nom);

  /**
   * Enregistre le nom.
   *
   * @param nom - Le nouveau nom.
   */
  void setNomAffaire(String nom);

  /**
   * Enregistre le type d'organisation.
   *
   * @param typeOrganisation - {@link TypeOrganisation} du Partenaire.
   */
  void setTypeOrganisation(TypeOrganisation typeOrganisation);

  /**
   * Enregistre le type d'organisation.
   *
   * @param typeOrga - Une organisation en String.
   */
  void setTypeOrganisation(String typeOrga);


  /**
   * Enregistre le nombre d'employé.
   *
   * @param nb - le nombre.
   */
  void setNbEmploye(int nb);

  /**
   * Enregistre la ville.
   *
   * @param ville - La nouvelle ville.
   */
  void setVille(String ville);

  /**
   * Enregistre le code.
   *
   * @param code - Le nouveau code.
   */
  void setCodePostal(String code);

  /**
   * Enregistre le pays.
   *
   * @param pays - Le pays du partenaire.
   */
  void setPays(PaysDto pays);


  /**
   * Enregistre le mail.
   *
   * @param mail - Le nouveau mail.
   */
  void setMail(String mail);

  /**
   * Enregistre l'adresse.
   *
   * @param adresse - La nouvelle adresse.
   */
  void setAdresse(String adresse);

  /**
   * Enregistre le numéro de téléphone.
   *
   * @param telephone - Le nouveau mot de téléphone.
   */
  void setTelephone(String telephone);

  /**
   * Enregistre le createur de ce Partenaire.
   *
   * @param createur - {@link UserDto}
   */
  void setCreateur(UserDto createur);

  /**
   * Enregistre la liste des départements du partenaire.
   *
   * @param departements - {@link UserDto}
   */
  void setIplDepartements(List<DepartementDto> departements);

  /**
   * Renvoi les départements du partenaire.
   *
   * @return Le créateur du partenaire.
   */
  List<DepartementDto> getIplDepartements();

  /**
   * @return le departement externe(non-dependant de l'IPL) du Partenaire.
   */
  String getDepartement();

  /**
   * Definis le departement externe (non-dependant de l'IPL) du Partenaire.
   * @param departement - Leur department.
   */
  void setDepartement(String departement);



}
