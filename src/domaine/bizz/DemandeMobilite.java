package domaine.bizz;

import dal.dao.core.DbEntity;
import dal.dao.core.DbEntityColumn;
import dal.dao.core.DbEntityFk;
import domaine.bizz.interfaces.DemandeMobiliteBizz;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.UserDto;
import util.AppUtil;

import java.time.LocalDate;
import java.util.List;

@DbEntity(table = "demandes_mobilites")
public class DemandeMobilite extends BaseEntiteImpl implements DemandeMobiliteBizz {

  @DbEntityColumn("date_introduction")
  private LocalDate date;

  @DbEntityColumn("annee_academique")
  private int annee;

  @DbEntityFk
  private UserDto etud;

  @DbEntityColumn("etudiant")
  private Integer cleEtud;

  @DbEntityFk
  private List<ChoixMobiliteDto> choixMobilites;


  @Override
  public LocalDate getDateIntroduction() {
    return date;
  }

  @Override
  public void setDateIntroduction(LocalDate date) {
    this.date = date;
  }

  @Override
  public int getAnneeAcademique() {
    return annee;
  }

  @Override
  public void setAnneeAcademique(int annee) {
    this.annee = annee;
  }

  @Override
  public UserDto getEtudiant() {
    return etud;
  }

  @Override
  public void setEtudiant(UserDto etud) {
    this.etud = etud;
  }


  @Override
  public void setChoixMobilites(List<ChoixMobiliteDto> choix) {
    AppUtil.checkObject(choix,"Les mobilités pour cette demande ne peuvent pas être nulles.");
    choixMobilites = choix;
  }

  @Override
  public List<ChoixMobiliteDto> getChoixMobilites() {
    return choixMobilites;
  }

}
