package dal.dao.mocks;

import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.DemandeMobiliteDao;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.UserDto;
import util.AppUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoCache
public class MockDemandeMobiliteDaoImpl extends DaoImpl<DemandeMobiliteDto>
    implements DemandeMobiliteDao {

  private final List<DemandeMobiliteDto> dbDemandes =
      Collections.synchronizedList(new ArrayList<DemandeMobiliteDto>());

  @Override
  public DemandeMobiliteDto insererDemande(DemandeMobiliteDto dem) {
    dbDemandes.add(dem);
    dem.setId(dbDemandes.size());
    return dem;
  }


  @Override
  public DemandeMobiliteDto findByAnneeAndStudent(DemandeMobiliteDto demande) {
    return dbDemandes.stream().filter(d -> {
      return demande.getAnneeAcademique() == d.getAnneeAcademique()
          && demande.getEtudiant().getId() == d.getEtudiant().getId();
    }).findFirst().orElse(null);
  }


  @Override
  public List<DemandeMobiliteDto> findByStudent(UserDto etudiant) {
    AppUtil.checkObject(etudiant);

    return dbDemandes.stream().filter(dm -> dm.getEtudiant().getId() == etudiant.getId())
        .collect(Collectors.toList());
  }

  @Override
  public DemandeMobiliteDto findById(int id) {
    return dbDemandes.stream().filter(p -> id == p.getId()).findFirst()
        .orElse(factory.getDemandeMobilite());
  }


  @Override
  public List<DemandeMobiliteDto> findAll() {
    return dbDemandes;
  }


  @Override
  public List<DemandeMobiliteDto> listerDemande(List<DepartementDto> departements) {
    AppUtil.checkObjects(departements);
    return dbDemandes.stream().filter(d -> {
      return departements.contains(d.getEtudiant().getDepartement());
    }).collect(Collectors.toList());
  }



  @Override
  public List<Integer> findAllAnnee() {
    List<Integer> liste =
        dbDemandes.stream().map(d -> d.getAnneeAcademique()).collect(Collectors.toList());
    if (liste.size() == 0) {
      return null;
    }
    return liste;
  }



}
