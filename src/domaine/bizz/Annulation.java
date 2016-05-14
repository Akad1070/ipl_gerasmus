package domaine.bizz;

import dal.dao.core.DbEntity;
import dal.dao.core.DbEntityColumn;
import dal.dao.core.DbEntityFk;
import domaine.bizz.interfaces.AnnulationBizz;
import domaine.dto.UserDto;
import util.AppUtil;

@DbEntity(table = "motifs_annulation")
public class Annulation extends BaseEntiteImpl implements AnnulationBizz {

  private String motif;

  @DbEntityFk
  private User createur;

  @DbEntityColumn("createur")
  private Integer fkUser;



  @Override
  public String getMotif() {
    return motif;
  }

  @Override
  public UserDto getCreateur() {
    return createur;
  }

  @Override
  public void setMotif(String motif) {
    this.motif = motif;
  }

  @Override
  public void setCreateur(UserDto createur) {
    this.createur = (User) createur;
  }


  @Override
  public String toString() {
    return super.toString() + "Motif : " + getMotif();
  }

}
