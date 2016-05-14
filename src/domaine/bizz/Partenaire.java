package domaine.bizz;

import dal.dao.core.DbEntity;
import dal.dao.core.DbEntityColumn;
import dal.dao.core.DbEntityFk;
import domaine.bizz.interfaces.PartenaireBizz;
import domaine.dto.DepartementDto;
import domaine.dto.PaysDto;
import domaine.dto.UserDto;
import util.AppUtil;

import java.util.List;

@DbEntity(table = "partenaires")
public class Partenaire extends BaseEntiteImpl implements PartenaireBizz {

  @DbEntityColumn("nom_legal")
  private String nomLegal;

  @DbEntityColumn("nom_complet")
  private String nomComplet;

  @DbEntityColumn("nom_affaire")
  private String nomAffaire;

  @DbEntityColumn("type_organisation")
  private TypeOrganisation typeOrga;

  @DbEntityColumn("nb_employe")
  private int nbEmploye;

  private String adresse;

  private String departement;

  @DbEntityColumn("pays")
  private Integer fkPays;
  @DbEntityFk
  private PaysDto pays;

  private String region;

  @DbEntityColumn("cp")
  private String codePostal;

  private String ville;
  private String email;

  @DbEntityColumn("site_web")
  private String siteWeb;

  @DbEntityColumn("tel")
  private String telephone;

  @DbEntityColumn("createur")
  private Integer fkCreateur;
  @DbEntityFk
  private UserDto createur;

  @DbEntityFk
  private List<DepartementDto> iplDepartements;

  private boolean visible = true;


  @Override
  public String getNomLegal() {
    return nomLegal;
  }

  @Override
  public String getNomComplet() {
    return nomComplet;
  }

  @Override
  public String getNomAffaire() {
    return nomAffaire;
  }

  @Override
  public TypeOrganisation getTypeOrganisation() {
    return typeOrga;
  }

  @Override
  public Integer getNbEmploye() {
    return nbEmploye;
  }

  @Override
  public String getAdresse() {
    return adresse;
  }

  @Override
  public PaysDto getPays() {
    return pays;
  }

  @Override
  public String getVille() {
    return ville;
  }

  @Override
  public String getMail() {
    return email;
  }

  @Override
  public String getSiteWeb() {
    return siteWeb;
  }

  @Override
  public String getTelephone() {
    return telephone;
  }


  @Override
  public UserDto getCreateur() {
    return createur;
  }

  @Override
  public String getCodePostal() {
    return codePostal;
  }

  @Override
  public String getRegion() {
    return region;
  }

  @Override
  public void setNomLegal(String nom) {
    nomLegal = nom;
  }

  @Override
  public void setSiteWeb(String siteWeb) {
    AppUtil.checkString(siteWeb, "Le site web du partenaire ne peut pas être null.");
    this.siteWeb = siteWeb;
  }

  @Override
  public void setRegion(String region) {
    this.region = region;
  }

  @Override
  public void setNomcomplet(String nom) {
    nomComplet = nom;
  }

  @Override
  public void setNomAffaire(String nom) {
    nomAffaire = nom;
  }

  @Override
  public void setTypeOrganisation(TypeOrganisation typeOrganisation) {
    AppUtil.checkObject(typeOrganisation,
        "Le type d'organisation du partenaire ne peut pas être null.");
    typeOrga = typeOrganisation;
  }


  @Override
  public void setTypeOrganisation(String typeOrga) {
    AppUtil.checkObject(typeOrga, "Le type d'organisation du partenaire ne peut pas être null.");
    this.typeOrga = TypeOrganisation.valueOf(typeOrga);
  }


  @Override
  public void setNbEmploye(int nb) {
    nbEmploye = nb;
  }

  @Override
  public void setVille(String ville) {
    this.ville = ville;
  }

  @Override
  public void setCodePostal(String cp) {
    codePostal = cp;
  }

  @Override
  public void setPays(PaysDto pays) {
    this.pays = pays;
  }

  @Override
  public void setMail(String mail) {
    email = mail;
  }

  @Override
  public void setAdresse(String adresse) {
    AppUtil.checkString(adresse, "L'adresse du partenaire ne peut pas être null.");
    this.adresse = adresse;
  }

  @Override
  public void setTelephone(String telephone) {
    AppUtil.checkString(telephone, "Le téléphone du partenaire ne peut pas être null.");
    this.telephone = telephone;
  }


  @Override
  public void setCreateur(UserDto createur) {
    AppUtil.checkObject(createur, "Il faut désigner le créateur de ce partenaire.");
    this.createur = createur;
    fkCreateur = createur.getId();
  }

  @Override
  public boolean estSelectionable() {
    return (createur == null && isVisible());
  }

  @Override
  public void setIplDepartements(List<DepartementDto> departements) {
    AppUtil.checkObject(departements);
    iplDepartements = departements;
  }



  @Override
  public List<DepartementDto> getIplDepartements() {
    return iplDepartements;
  }


  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public void setVisible(boolean visibilite) {
    visible = visibilite;
  }

  @Override
  public String getDepartement() {
    return departement;
  }

  @Override
  public void setDepartement(String departement) {
    this.departement = departement;
  }

}
