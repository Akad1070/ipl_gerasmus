package domaine.dto;

import domaine.bizz.interfaces.BaseEntite;


public interface AnnulationDto extends BaseEntite {

  /**
   * @return le motif de l'anulation.
   */
  String getMotif();

  /**
   * @return le créateur de l'annulation.
   */
  UserDto getCreateur();

  /**
   * Enregistre le motif de l'annulation.
   *
   * @param motif - le nouveau motif.
   */
  void setMotif(String motif);

  /**
   * Enregistre le crateur de l'application.
   *
   * @param createur - le nouveau créateur.
   */
  void setCreateur(UserDto createur);



}
