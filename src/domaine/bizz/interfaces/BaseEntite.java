package domaine.bizz.interfaces;


/**
 * Regroupe l'ensemble des methodes communes au Business.
 *
 * @author Akaad
 */
public interface BaseEntite {

  /**
   * Renvoie l'id de l'objet.
   *
   * @return un int
   */
  int getId();

  /**
   * Attribue un id à un objet Bizz.
   *
   * @param id - l'id de l'objet
   */
  void setId(int id);

  /**
   * Renvoie le numéro de version de l'objet Bizz.
   *
   * @return un int
   */
  int getNumeroVersion();

  /**
   * Attribue un numéro de version à un objet.
   *
   * @param numVersion - le numéro.
   */
  void setNumeroVersion(int numVersion);

  /**
   * Incrémente le numéro de version de l'objet.
   *
   * @return le numéro de version +1 ou 1 si le numéro de version est égal à Integer.MAX_VALUE
   */
  int incNumeroVersion();

}
