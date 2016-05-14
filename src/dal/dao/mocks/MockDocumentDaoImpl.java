package dal.dao.mocks;

import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.DocumentDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.DocumentDto;
import domaine.dto.DocumentDto.Genre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoCache
public class MockDocumentDaoImpl extends DaoImpl<DocumentDto> implements DocumentDao {

  private final List<DocumentDto> dbDocs =
      Collections.synchronizedList(new ArrayList<DocumentDto>());
  private final List<DocumentDto> dbDocsAll =
      Collections.synchronizedList(new ArrayList<DocumentDto>());

  private void injecterDb() {

    if (dbDocs.size() > 0 || dbDocsAll.size() > 0) {
      return;
    }
    dbDocs.add(factory.getDocument());

    DocumentDto doc = factory.getDocument();
    dbDocsAll.add(doc);

    doc = factory.getDocument();
    doc.setId(2);
    doc.setNom("A Remplir");
    dbDocsAll.add(doc);

    doc = factory.getDocument();
    doc.setId(3);
    doc.setNom("A Remplir2");
    doc.setGenre(Genre.RETOUR);
    dbDocsAll.add(doc);

    doc = factory.getDocument();
    doc.setId(4);
    doc.setNom("A Remplir3");
    dbDocsAll.add(doc);

  }

  @Override
  public DocumentDto findById(int id) {
    injecterDb();
    return dbDocs.stream().filter(d -> d.getId() == id).findFirst().orElse(factory.getDocument());
  }


  @Override
  public List<DocumentDto> findAll() {
    injecterDb();
    return dbDocsAll;
  }


  @Override
  public boolean ajouterDocumentRempli(DocumentDto document) {
    injecterDb();
    if (document == null || document.getId() == 0) {
      return false;
    }


    dbDocs.add(document);
    return true;
  }


  @Override
  public boolean supprimerDocumentRempli(ChoixMobiliteDto mobilite, DocumentDto document) {
    injecterDb();
    DocumentDto doc = null;
    if ((doc =
        dbDocs.stream().filter(d -> d.getId() == document.getId()).findFirst().get()) == null) {
      return false;
    }
    dbDocs.remove(dbDocs.indexOf(doc));
    return true;
  }


  @Override
  public List<DocumentDto> getListeDocumentsARemplir(ChoixMobiliteDto mobilite) {
    injecterDb();
    return dbDocsAll;
  }

  @Override
  public List<DocumentDto> getListeDocumentsRemplis(ChoixMobiliteDto mobilite) {
    injecterDb();
    return dbDocs.stream().filter(d -> d.getMobilite().equals(mobilite))
        .collect(Collectors.toList());
  }



}
