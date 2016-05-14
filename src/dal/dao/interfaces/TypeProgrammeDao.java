package dal.dao.interfaces;

import domaine.dto.ChoixMobiliteDto;
import domaine.dto.TypeProgrammeDto;

/**
 * Gert les CRUD de type_programmes.
 *
 * @author candy
 *
 */
public interface TypeProgrammeDao extends Dao<TypeProgrammeDto> {

  /**
   * Permet de récupérer un type deprogramme grace à son nom;
   *
   * @param typeProgramme - Le {@link TypeProgrammeDto} contenant le nom.
   * @return Une {@link TypeProgrammeDto} correspondant au type de programme récupéré en DB.
   */
  TypeProgrammeDto findByName(TypeProgrammeDto typeProgramme);

  /**
   * Permet de récupérer un type de programme lié à une mobilté.
   *
   * @param mobi - La {@link ChoixMobiliteDto} remplie avec le num de Candidature;
   * @return Une {@link TypeProgrammeDto} correspondant au type de programme récupéré en DB.
   */
  TypeProgrammeDto findByMobilite(ChoixMobiliteDto mobi);
}
