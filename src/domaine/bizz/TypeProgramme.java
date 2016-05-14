package domaine.bizz;

import util.AppUtil;
import dal.dao.core.DbEntity;
import dal.dao.core.DbEntityColumn;
import domaine.bizz.interfaces.TypeProgrammeBizz;

@DbEntity(table = "types_programme")
public class TypeProgramme extends BaseEntiteImpl implements TypeProgrammeBizz {


  @DbEntityColumn("nom")
  private String intitule;


  @Override
  public String getNom() {
    return intitule;
  }

  @Override
  public void setNom(String nom) {
    AppUtil.checkString(nom, "L'intitulé ne doit pas être null");
    intitule = nom;
  }

}
