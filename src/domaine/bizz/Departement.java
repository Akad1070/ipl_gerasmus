package domaine.bizz;

import dal.dao.core.DbEntity;
import domaine.bizz.interfaces.DepartementBizz;
import util.AppUtil;

@DbEntity(table = "departements")
public class Departement extends BaseEntiteImpl implements DepartementBizz {
  private String code;
  private String nom;


  @Override
  public String getNom() {
    return nom;
  }

  @Override
  public void setNom(String nom) {
    AppUtil.checkString(nom, "Le nom du département ne peut pas être null.");
    this.nom = nom;
  }

  @Override
  public void setCode(String code) {
    AppUtil.checkString(code, " Il faut un code pour ce département");
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }


  @Override
  public String toString() {
    StringBuilder str = new StringBuilder(String.format("Departement %s", super.toString()));
    str.append("Code ").append(code).append(" - Section ").append(nom);
    return str.toString();
  }



}
