package dal.dao.interfaces;

import domaine.dto.AnnulationDto;
import domaine.dto.ChoixMobiliteDto;

import java.util.List;

/**
 * Gert les CRUD de annulation.
 *
 * @author candy
 */
public interface AnnulationDao extends Dao<AnnulationDto> {

  /**
   * Permet d'inserer d'un motif d'annulation.
   *
   * @param motif - Une {@link AnnulationDto} contenant le motif.
   * @return Le motif d'annulation inseré.
   */
  AnnulationDto insererAnnulation(AnnulationDto motif);

  /**
   * Permet de récupérer une liste des annulations écrites par les professeurs.
   *
   * @return une liste des {@link AnnulationDto} récupérés.
   */
  List<AnnulationDto> listerAnnulationPourProf();

  /**
   * Pemrmet de récupérer le motif d'annulation lié à une mobilité.
   *
   * @param mobi - La {@link ChoixMobiliteDto} contenant au moins son id et son num preférence.
   * @return L'annulation récupéree.
   */
  AnnulationDto findByMobilite(ChoixMobiliteDto mobi);


}
