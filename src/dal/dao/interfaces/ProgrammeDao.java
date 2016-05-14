package dal.dao.interfaces;

import domaine.dto.ChoixMobiliteDto;
import domaine.dto.ProgrammeDto;

/**
 * Gert les CRUD de programme.
 *
 * @author candy
 *
 */
public interface ProgrammeDao extends Dao<ProgrammeDto> {

  /**
   * Permet de récupérer un programme grace à son nom;
   *
   * @param programme - {@link ProgrammeDto} contenant le nom.
   * @return Une {@link ProgrammeDto} du programme récupéré en DB.
   */
  ProgrammeDto findByName(ProgrammeDto programme);


  /**
   * Permet de récupérer un programme lié à une mobilité.
   *
   * @param mobi - La {@link ChoixMobiliteDto} remplie avec le num de Candidature;
   * @return Une {@link ProgrammeDto} du programme récupéré en DB.
   */
  ProgrammeDto findByMobilite(ChoixMobiliteDto mobi);
}
