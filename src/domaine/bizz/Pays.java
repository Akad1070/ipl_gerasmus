package domaine.bizz;

import dal.dao.core.DbEntity;
import dal.dao.core.DbEntityColumn;
import dal.dao.core.DbEntityColumnTransient;
import domaine.bizz.interfaces.PaysBizz;
import domaine.dto.ProgrammeDto;
import util.AppUtil;

@DbEntity(schema = "gerasmus", table = "pays")
public class Pays extends BaseEntiteImpl implements PaysBizz {

  @DbEntityColumn("code_iso")
  private String codePays;

  @DbEntityColumn("nom")
  private String intitule;

  @DbEntityColumn("programme")
  private Integer programme;

  @DbEntityColumnTransient
  private ProgrammeDto prog;



  @Override
  public String getCode() {
    return codePays;
  }

  @Override
  public String getNom() {
    return intitule;
  }

  @Override
  public void setCode(String codePays) {
    AppUtil.checkString(codePays, "Le code pays ne doit pas être null");
    this.codePays = codePays;
  }

  @Override
  public void setNom(String intitule) {
    AppUtil.checkString(intitule, "Le nom du pays ne doit pas être null");
    this.intitule = intitule;
  }


  @Override
  public ProgrammeDto getProgramme() {
    return prog;
  }



  @Override
  public void setProgramme(ProgrammeDto programme) {
    AppUtil.checkObject(programme, "Le programme ne doit pas être null");
    prog = programme;
  }



}
