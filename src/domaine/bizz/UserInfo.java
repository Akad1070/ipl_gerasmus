package domaine.bizz;

import dal.dao.core.DbEntity;
import dal.dao.core.DbEntityColumn;
import domaine.bizz.interfaces.UserInfoBizz;
import domaine.dto.UserDto;
import util.AppUtil;

import java.time.LocalDate;

@DbEntity(schema = "gerasmus", table = "informations_etudiants")
public class UserInfo extends BaseEntiteImpl implements UserInfoBizz {

  private Civilite civilite;
  private Sexe sexe;
  private String nationalite;
  private String adresse;
  private String tel;
  private String email;

  @DbEntityColumn("compte_bancaire")
  private String compteBancaire;

  @DbEntityColumn("compte_titulaire")
  private String titulaire;

  @DbEntityColumn("nom_banque")
  private String banque;

  @DbEntityColumn("code_bic")
  private String bic;

  @DbEntityColumn("date_naissance")
  private LocalDate dateNais;

  @DbEntityColumn("nb_annees_reussies")
  private Integer nombreAnneeReussie;

  private UserDto utilisateur;

  @Override
  public Civilite getCivilite() {
    return this.civilite;
  }

  @Override
  public Sexe getSexe() {
    return this.sexe;
  }

  @Override
  public String getNationalite() {
    return this.nationalite;
  }

  @Override
  public String getAdresse() {
    return this.adresse;
  }

  @Override
  public String getTel() {
    return this.tel;
  }

  @Override
  public String getEmail() {
    return this.email;
  }

  @Override
  public String getCompteBancaire() {
    return this.compteBancaire;
  }

  @Override
  public String getTitulaire() {
    return this.titulaire;
  }

  @Override
  public String getBanque() {
    return this.banque;
  }

  @Override
  public String getBic() {
    return this.bic;
  }

  @Override
  public LocalDate getDateNais() {
    return this.dateNais;
  }

  @Override
  public int getNombreAnneeReussie() {
    return this.nombreAnneeReussie;
  }


  @Override
  public void setCivilite(Civilite civilite) {
    this.civilite = civilite;
  }

  @Override
  public void setCivilite(String civilite) {
    this.civilite = Civilite.valueOf(civilite);
  }

  @Override
  public void setSexe(Sexe sexe) {
    this.sexe = sexe;
  }

  @Override
  public void setSexe(String sexe) {
    this.sexe = Sexe.valueOf(sexe);
  }

  @Override
  public void setNationalite(String nation) {
    this.nationalite = nation;
  }

  @Override
  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  @Override
  public void setTel(String tel) {
    this.tel = tel;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public void setCompteBancaire(String compteBancaire) {
    this.compteBancaire = compteBancaire;
  }

  @Override
  public UserDto getUtilisateur() {
    return utilisateur;
  }

  @Override
  public void setUtilisateur(UserDto utilisateur) {
    this.utilisateur = utilisateur;
  }

  @Override
  public void setTitulaire(String titulaire) {
    this.titulaire = titulaire;
  }

  @Override
  public void setBanque(String banque) {
    this.banque = banque;
  }

  @Override
  public void setBic(String bic) {
    this.bic = bic;
  }

  @Override
  public void setDateNais(LocalDate dateNais) {
    this.dateNais = dateNais;
  }

  @Override
  public void setNombreAnneeReussie(int nbAnReussie) {
    this.nombreAnneeReussie = nbAnReussie;
  }

  @Override
  public void checkBeforeInsert() {
    AppUtil.checkCivilite(this.civilite);
    AppUtil.checkString(this.nationalite);
    AppUtil.checkString(this.adresse);
    AppUtil.checkTel(this.tel);
    AppUtil.checkMail(this.email);
    AppUtil.checkSexe(this.sexe);
    AppUtil.checkCompteBancaire(this.compteBancaire);
    AppUtil.checkString(this.titulaire);
    AppUtil.checkString(this.banque);
    AppUtil.checkBic(this.bic);
    AppUtil.checkNumerique(this.nombreAnneeReussie + "");

  }


}
