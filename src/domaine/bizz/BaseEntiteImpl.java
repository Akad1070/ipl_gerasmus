package domaine.bizz;

import util.AppUtil;
import dal.dao.core.DbEntityColumn;
import dal.dao.core.DbEntityPk;
import domaine.bizz.interfaces.BaseEntite;

/**
 * Regroupe les methodes et attributs communs des objets Business.
 *
 * @author Akad
 */
public abstract class BaseEntiteImpl implements BaseEntite {
  private static final long serialVersionUID = 1L;

  @DbEntityPk
  protected Integer id = 0;

  @DbEntityColumn("num_version")
  protected Integer numVersion = 1; // Le num de version ne sert pas au front-end


  @Override
  public int getId() {
    return this.id;
  }


  @Override
  public void setId(int id) {
    this.id = id;
  }


  @Override
  public int getNumeroVersion() {
    return this.numVersion;
  }

  @Override
  public void setNumeroVersion(int numVersion) {
    AppUtil.checkPositive(numVersion);
    this.numVersion = numVersion;
  }


  @Override
  public int incNumeroVersion() {
    if (++this.numVersion == Integer.MAX_VALUE) {
      this.numVersion = 1;
    }
    return this.numVersion;
  }


  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.id;
    result = prime * result + this.numVersion;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    BaseEntiteImpl other = (BaseEntiteImpl) obj;
    if (!this.id.equals(other.id)) {
      return false;
    }
    if (!this.numVersion.equals(other.numVersion)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return String.format("#%d - Version : %d %n", this.id, this.numVersion);
  }



}
