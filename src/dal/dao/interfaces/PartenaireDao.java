package dal.dao.interfaces;

import domaine.dto.ChoixMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;

import java.util.List;

/**
 * Gert les CRUD de partenaire.
 *
 * @author candy
 *
 */
public interface PartenaireDao extends Dao<PartenaireDto> {

  /**
   * Permet d'insérer d'un partenaire dans la BD.
   *
   * @param partenaire - Le {@link PartenaireDto} à insérer.
   * @return Un {@link PartenaireDto} si l'insertion du partenaire s'est bien déroulée.
   */
  PartenaireDto inserer(PartenaireDto partenaire);

  /**
   * Permet de lier un partenaire à un département.
   *
   * @param partenaire - Le partenaire recemment inseré
   * @param departement - Le departement de ce partenaire.
   * @return Le {@link PartenaireDto};
   */
  PartenaireDto lierADepartement(PartenaireDto partenaire, DepartementDto departement);


  /**
   * Permet de lister tous les partenaires correspondant à la recherchre souhaitée.
   *
   * @param partenaire - Le partenaire recherché.
   * @return Une liste de {@link PartenaireDto} correspondant au critére de recherche.
   */
  List<PartenaireDto> rechercher(PartenaireDto partenaire);


  /**
   * Permet de trouver le partenaire lié a une mobilité.
   *
   * @param mobi - La {@link ChoixMobiliteDto} pour laquelle on recherche le partenaire.
   * @return La {@link PartenaireDto} récupérée.
   */
  PartenaireDto findByMobilite(ChoixMobiliteDto mobi);

  /**
   * Permet de changer la visibilté d'un partenaire.
   *
   * @param part - Le partenaire à rendre invisible ou visible.
   * @return La {@link PartenaireDto} rendue visible ou visible.
   */
  PartenaireDto changerVisibilite(PartenaireDto part);


  /**
   * Permet de lister les partenaires selectionnables par les étudiants. C-à-d ceux qui ne sont pas
   * cachés.
   *
   * @return La {@link PartenaireDto} des partenaires selectionnables.
   */
  List<PartenaireDto> listerPartenairesSelectionnablesPourEtudiants();

  /**
   * Permet de lister les partenaires selectionnables par étudiant. C-à-d ceux qui ne sont pas
   * cachés et qui sont dans le même département que l'étudiant.
   *
   * @param etudiant - L'étudiant avec son département.
   * @return La {@link PartenaireDto} des partenaires selectionnables pour l'étudiant.
   */
  List<PartenaireDto> listerPartenairesSelectionnables(UserDto etudiant);


}
