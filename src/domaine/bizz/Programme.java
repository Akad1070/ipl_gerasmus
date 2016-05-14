package domaine.bizz;

import util.AppUtil;
import dal.dao.core.DbEntity;
import dal.dao.core.DbEntityColumn;
import domaine.bizz.interfaces.ProgrammeBizz;

@DbEntity(table = "programmes")
public class Programme extends BaseEntiteImpl implements ProgrammeBizz {


  @DbEntityColumn("nom")
  private String intitule;

  @Override
  public String getNom() {
    return this.intitule;
  }

  @Override
  public void setNom(String nom) {
    AppUtil.checkString(nom, "L'intitulé ne doit pas être null");
    this.intitule = nom;
  }

}
