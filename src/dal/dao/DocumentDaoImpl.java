package dal.dao;

import core.config.AppConfig;
import core.exceptions.FatalException;
import dal.dao.interfaces.DocumentDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.DocumentDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentDaoImpl extends DaoImpl<DocumentDto> implements DocumentDao {

  @Override
  public boolean ajouterDocumentRempli(DocumentDto document) {
    ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("c_document_rempli"));

    try {
      ps.setInt(1, document.getId());
      ps.setInt(2, document.getMobilite().getNumCandidature());
      ps.setInt(3, document.getMobilite().getNumPreference());
      ps.executeUpdate();
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }

    return true;
  }

  @Override
  public boolean supprimerDocumentRempli(ChoixMobiliteDto mobilite, DocumentDto document) {
    ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("d_document_rempli"));

    try {
      ps.setInt(1, document.getId());
      ps.setInt(2, mobilite.getNumCandidature());
      ps.setInt(3, mobilite.getNumPreference());
      ps.executeUpdate();
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return true;
  }

  @Override
  public List<DocumentDto> getListeDocumentsARemplir(ChoixMobiliteDto mobi) {
    ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("r_document_a_remplir"));
    List<DocumentDto> documents = null;
    try {
      ps.setInt(2, mobi.getNumPreference());
      ps.setInt(1, mobi.getNumCandidature());
      try (ResultSet res = ps.executeQuery()) {
        documents = new ArrayList<DocumentDto>();
        while (res.next()) {
          documents.add(super.fillEntityWithResult(res));
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return documents;
  }

  @Override
  public List<DocumentDto> getListeDocumentsRemplis(ChoixMobiliteDto mobilite) {
    ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("r_document_rempli"));
    List<DocumentDto> documents = null;
    try {
      ps.setInt(1, mobilite.getNumCandidature());
      ps.setInt(2, mobilite.getNumPreference());
      try (ResultSet res = ps.executeQuery()) {
        documents = new ArrayList<DocumentDto>();
        while (res.next()) {
          documents.add(super.fillEntityWithResult(res));
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }

    return documents;
  }


}
