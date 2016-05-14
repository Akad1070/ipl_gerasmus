package dal.dao.interfaces;

import java.util.List;

import domaine.dto.DepartementDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;

/**
 * Gert les CRUD de département.
 *
 * @author candy
 *
 */
public interface DepartementDao extends Dao<DepartementDto> {


  /**
   * Permet de récupérer un département en fonction de son id.
   *
   * @param departement - La {@link DepartementDto}
   * @return Un {@link DepartementDto} si trouvé, sinon null.
   */
  DepartementDto findByCode(DepartementDto departement);



  /**
   * Permet de récupérer le département d'un utilisateur.
   *
   * @param user - La {@link UserDto} contenant l'Id
   * @return un {@link DepartementDto} si trouvé, sinon null.
   */
  DepartementDto findByUser(UserDto user);


  /**
   * Permet de récupérer tout les departements d'un partenaire.
   *
   * @param part - Le {@link PartenaireDto} contenant son id.
   * @return une List de {@link DepartementDto} contenant les départements trouvés.
   */
  List<DepartementDto> findByPartenaire(PartenaireDto part);



}
