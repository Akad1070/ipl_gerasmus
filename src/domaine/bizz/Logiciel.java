package domaine.bizz;

import dal.dao.core.DbEntity;
import domaine.bizz.interfaces.LogicielBizz;
import domaine.dto.ChoixMobiliteDto;
import util.AppUtil;

@DbEntity(table = "logiciels")
public class Logiciel extends BaseEntiteImpl implements LogicielBizz {

  private String nom;
  
  private ChoixMobiliteDto mobilite;

  @Override
  public String getNom() {
    return nom;
  }

  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }

}
