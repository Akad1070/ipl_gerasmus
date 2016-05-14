package dal.dao.interfaces;

import domaine.dto.ChoixMobiliteDto;
import domaine.dto.LogicielDto;

import java.util.List;

/**
 * Gert les CRUD de logiciel.
 *
 * @author candy
 *
 */
public interface LogicielDao extends Dao<LogicielDto> {

  /**
   * Permet d'annuler l'encodage d'un logiciel.
   *
   * @param mobilite - La mobilité dont l'encodage n'avais pas été réaliser.
   * @return true si l'encodage_logiciel a bien été supprimer.
   */
  boolean annulerEncodageLogiciel(LogicielDto logiciel, ChoixMobiliteDto mobilite);

  /**
   * Permet d'indique que la mobilité X a bien été encoder dans le logiciel Y.
   *
   * @param logiciel - le logiciel qui est selectionne.
   * @param mobilite - la mobiliter pour lequel les donnée on été encoder.
   * @return - true si l'encodage a bien été inserer.
   */
  boolean confirmerEncodageLogiciel(LogicielDto logiciel, ChoixMobiliteDto mobilite);

  /**
   * Permet de lister les logiciel dans lesquels une mobilité a été encodée.
   *
   * @param mobilite - La mobilite choisie.
   * @return La lisde des {@link LogicielDto} récupérés.
   */
  List<LogicielDto> logicielsEncodes(ChoixMobiliteDto mobilite);


  /**
   * Permet de lister les logiciels dans lesquels une mobilité doit être encodée.
   *
   * @param mobilite - La mobilite choisie.
   * @return La lisde des {@link LogicielDto} récupérés.
   */
  List<LogicielDto> logicielsAEncoder(ChoixMobiliteDto mobilite);
}
