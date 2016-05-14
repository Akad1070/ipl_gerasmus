package dal.service;

import core.exceptions.BizzException;


public interface DalService {

  /**
   * Sert à démarrer une transaction en bloquant l'autocommit pour mieux controler la transaction.
   *
   * @return <b>TRUE</b> si effectuée.
   * @throws BizzException - Une erreur est survenue.
   */
  boolean startTransaction() throws BizzException;

  /**
   * Effectue tous les changements pour la transaction en BD;
   *
   * @return <b>TRUE</b> si effectuée.
   * @throws BizzException - Une erreur est survenue.
   */
  boolean commit() throws BizzException;

  /**
   * Annule tous les changements à effectuer pour la transaction en BD.
   *
   * @return <b>TRUE</b> si effectuée.
   * @throws BizzException - Une erreur est survenue.
   */
  boolean rollback() throws BizzException;

  /**
   * Ferme la connection. A éviter
   */
  void closeConnection();


}
