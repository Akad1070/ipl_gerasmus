package dal.dao.mocks;

import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.PartenaireDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;
import util.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoCache
public class MockPartenaireDaoImpl extends DaoImpl<PartenaireDto> implements PartenaireDao {

  private final List<PartenaireDto> dbPartenaires =
      Collections.synchronizedList(new ArrayList<PartenaireDto>());

  @Override
  public PartenaireDto findById(int id) {
    return dbPartenaires.stream().filter(p -> id == p.getId()).findFirst().orElse(null);
  }


  @Override
  public List<PartenaireDto> findAll() {
    return Collections.unmodifiableList(dbPartenaires);
  }

  @Override
  public PartenaireDto inserer(PartenaireDto partenaire) {
    dbPartenaires.add(partenaire);
    partenaire.setId(dbPartenaires.size());
    return partenaire;
  }

  @Override
  public PartenaireDto lierADepartement(PartenaireDto pat, DepartementDto departement) {
    AppUtil.checkObjects(pat, departement);
    int id = pat.getId();
    pat = dbPartenaires.stream().filter(p -> p.getId() == id).findAny().orElse(null);
    if (pat == null) {
      pat = factory.getPartenaire();
      pat.setId(dbPartenaires.size());
      dbPartenaires.add(pat);
    }
    pat.setIplDepartements(Arrays.asList(departement));
    return pat;
  }


  @Override
  public List<PartenaireDto> rechercher(PartenaireDto partenaire) {
    AppUtil.checkObject(partenaire);
    List<PartenaireDto> liste = dbPartenaires.stream().filter(p -> {
      if (!p.getNomLegal().equalsIgnoreCase(partenaire.getNomLegal())) {
        if (!p.getVille().equalsIgnoreCase(partenaire.getVille())) {
          if (p.getPays() != null && partenaire.getPays() != null) {
            if (!p.getPays().getNom().equals(partenaire.getPays().getNom())) {
              return false;
            }
          }
        }
      }
      return true;
    })
      .collect(Collectors.toList());

    if (liste.size() == 0) {
      return null;
    }
    return liste;
  }

  @Override
  public PartenaireDto findByMobilite(ChoixMobiliteDto mobi) {
    return factory.getPartenaire();
  }

  @Override
  public PartenaireDto changerVisibilite(PartenaireDto part) {
    AppUtil.checkObject(part);
    PartenaireDto pt = dbPartenaires.stream().filter(p -> p.equals(part)).findAny().orElse(null);
    if (pt != null) {
      pt.setVisible(part.isVisible());
    }
    return pt;
  }

  @Override
  public List<PartenaireDto> listerPartenairesSelectionnablesPourEtudiants() {
    return dbPartenaires.stream().filter(p -> p.estSelectionable()).collect(Collectors.toList());
  }


  @Override
  public List<PartenaireDto> listerPartenairesSelectionnables(UserDto etudiant) {
    AppUtil.checkObject(etudiant);
    return dbPartenaires.stream()
        .filter(p -> (p.estSelectionable()) || p.getCreateur().equals(etudiant))
        .collect(Collectors.toList());
  }



}
