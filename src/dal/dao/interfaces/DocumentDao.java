package dal.dao.interfaces;

import domaine.dto.ChoixMobiliteDto;
import domaine.dto.DocumentDto;

import java.util.List;

public interface DocumentDao extends Dao<DocumentDto> {


  /**
   * Permet d'ajouter 1 ou plusieurs documents signés.
   *
   * @param document - Le document qui a été signé.
   * @return - true si l'insertion s'est bien passée.
   */
  boolean ajouterDocumentRempli(DocumentDto document);



  /**
   * Permet de supprimer 1 ou plusieurs documents signés.
   *
   * @param document - Le document qui n'estplus signés.
   * @param mobilite - La mobilité pour laquelle les documents ne'est plus signé.
   * @return - true si la suppression s'est bien passé.
   */
  boolean supprimerDocumentRempli(ChoixMobiliteDto mobilite, DocumentDto document);



  /**
   * Permet d'obtenir une liste des documents à signer d'une mobilité.
   *
   * @param mobilite - La mobilité choisie.
   * @return Une liste de {@link DocumentDto} contenant les documents à signer.
   */
  List<DocumentDto> getListeDocumentsARemplir(ChoixMobiliteDto mobilite);



  /**
   * Permet d'obtenir une liste des documents déjà signés d'une mobilité.
   *
   * @param mobilite - La mobilité choisie.
   * @return Une liste de {@link DocumentDto} contenant les documents signés.
   */
  List<DocumentDto> getListeDocumentsRemplis(ChoixMobiliteDto mobilite);
}
