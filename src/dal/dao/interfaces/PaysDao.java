package dal.dao.interfaces;

import domaine.dto.PartenaireDto;
import domaine.dto.PaysDto;
import domaine.dto.ProgrammeDto;

import java.util.List;

/**
 * Gert les CRUD de pays.
 *
 * @author candy
 *
 */
public interface PaysDao extends Dao<PaysDto> {

  /**
   * Permet de récupérer un pays en fonction de son code.
   *
   * @param pays - Le {@link PaysDto} contenant le code.
   * @return Un {@link PaysDto} si le code existe.
   */
  PaysDto findByCode(PaysDto pays);


  /**
   * Permet de récuperer un pays en fonction de son nom.
   *
   * @param pays - Le {@link PaysDto} contenant le nom.
   * @return Une {@link PaysDto} si le nom existe.
   */
  PaysDto findByNom(PaysDto pays);



  /**
   * Permet de récuperer le pays d'un programme.
   *
   * @param prog - Le {@link ProgrammeDto} contenant l'id.
   * @return Une List de {@link PaysDto} s'il existe des pays pour ce programme.
   */
  List<PaysDto> findByProgramme(ProgrammeDto prog);


  /**
   * Permet de récuperer le pays d'un partenaire.
   *
   * @param pat - Le {@link PartenaireDto} contenant un id.
   * @return Le {@link PaysDto} s'il existe un pays pour ce partenaire.
   */
  PaysDto findByPartenaire(PartenaireDto pat);



}
