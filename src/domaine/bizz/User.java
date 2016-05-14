package domaine.bizz;

import core.exceptions.AppException;
import core.exceptions.BizzException;
import dal.dao.core.DbEntity;
import dal.dao.core.DbEntityColumn;
import dal.dao.core.DbEntityColumnTransient;
import dal.dao.core.DbEntityFk;
import domaine.bizz.interfaces.UserBizz;
import domaine.dto.DepartementDto;
import util.AppUtil;

import java.time.LocalDate;

@DbEntity(schema = "gerasmus", table = "utilisateurs")
public class User extends BaseEntiteImpl implements UserBizz {
  private String nom;

  @DbEntityColumnTransient
  private String identifiant;

  private String prenom;
  private String pseudo;
  private String mdp;
  private String email;

  @DbEntityColumn("prof")
  private boolean estProf;

  @DbEntityColumn("departement")
  private Integer fkDepartement;

  @DbEntityFk
  private DepartementDto departement;

  @DbEntityColumn("date_inscription")
  private LocalDate dateInscription;



  @Override
  public String getNom() {
    return nom;
  }

  @Override
  public String getPrenom() {
    return prenom;
  }

  @Override
  public String getPseudo() {
    return pseudo;
  }

  @Override
  public DepartementDto getDepartement() {
    return departement;
  }

  @Override
  public String getMdp() {
    return mdp;
  }

  @Override
  public void setMdp(String mdp) {
    AppUtil.checkString(mdp, "Il faut un mot de passe.");
    this.mdp = mdp;
  }

  @Override
  public String getMail() {
    return email;
  }

  @Override
  public LocalDate getDateInscription() {
    return dateInscription;
  }

  @Override
  public void setNom(String nom) {
    AppUtil.checkString(nom, "Le nom ne peut pas être null.");
    this.nom = nom;
  }

  @Override
  public void setPrenom(String prenom) {
    AppUtil.checkString(prenom, "Le prénom ne peut pas être null.");
    this.prenom = prenom;
  }

  @Override
  public void setPseudo(String pseudo) {
    AppUtil.checkString(pseudo, "Le pseudo ne peut pas être null.");
    this.pseudo = pseudo;
  }

  @Override
  public void setMail(String mail) {
    AppUtil.checkString(mail, "L'e-mail ne peut pas être null.");
    email = mail;
  }

  @Override
  public void setProf(boolean estProf) {
    this.estProf = estProf;
  }


  @Override
  public boolean isProf() {
    return estProf;
  }

  @Override
  public void setDepartement(DepartementDto dep) {
    AppUtil.checkObject(dep, "Le departement ne peut pas être null.");
    departement = dep;
  }

  @Override
  public void setDateInscription(LocalDate date) {
    AppUtil.checkObject(date, "La date d'inscription ne peut pas être nulle.");
    dateInscription = date;
  }


  @Override
  public String getIdentifiant() {
    return identifiant;
  }

  @Override
  public void setIdentifiant(String identifiant) {
    AppUtil.checkString(identifiant, "L'identifiant ne peut pas être null.");
    this.identifiant = identifiant;
  }

  @Override
  public void checkBeforeInscription() {
    AppUtil.checkPseudo(pseudo);
    AppUtil.checkMail(email);
    AppUtil.checkMdp(mdp);
    AppUtil.checkNomOuPrenom(nom);
    AppUtil.checkNomOuPrenom(prenom);
    AppUtil.checkObject(departement);
  }

  @Override
  public boolean verifierMail() {
    try {
      return AppUtil.checkMail(identifiant);
    } catch (AppException exception) {
      return false;
    }
  }


  @Override
  public void verifierIdentifiants() {
    AppUtil.checkString(identifiant, "L'identifiant ne peut pas être null.");
    AppUtil.checkString(mdp, "Le mot de passe ne peut pas être null.");
  }

  @Override
  public void determinerType() {
    boolean estMailDeProf = false;
    try {
      estMailDeProf = AppUtil.checkMailProf(getMail());
    } catch (AppException excep) {
      try {
        estMailDeProf = !AppUtil.checkMailEtud(getMail());
      } catch (AppException exception) {
        throw new BizzException("Le mail introduit ne correspond pas au format de la Haute-Ecole.");
      }
    }
    setProf(estMailDeProf);
  }
}
