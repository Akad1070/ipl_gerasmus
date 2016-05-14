package dal.dao.mocks;

import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.ChoixMobiliteDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.ChoixMobiliteDto.Etat;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;
import util.AppUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoCache
public class MockChoixMobiliteDaoImpl extends DaoImpl<ChoixMobiliteDto>
    implements ChoixMobiliteDao {

  private final List<ChoixMobiliteDto> dbMobilites =
      Collections.synchronizedList(new ArrayList<ChoixMobiliteDto>());

  @Override
  public ChoixMobiliteDto insererMobilite(ChoixMobiliteDto mobilite) {
    dbMobilites.add(mobilite);
    mobilite.setNumPreference(dbMobilites.size());
    return mobilite;
  }

  @Override
  public List<ChoixMobiliteDto> findMobilityByAnneeAndEtat(ChoixMobiliteDto mob) {
    AppUtil.checkObjects(mob, mob.getDemande());
    List<ChoixMobiliteDto> liste = dbMobilites.stream()
        .filter(c -> c.getDemande().getAnneeAcademique() == mob.getDemande().getAnneeAcademique()
            && c.getEtat().equals(mob.getEtat()))
        .collect(Collectors.toList());
    if (liste.size() == 0) {
      return null;
    }
    return liste;
  }

  @Override
  public ChoixMobiliteDto findById(int id) {
    return dbMobilites.stream().filter(c -> c.getId() == id).findAny().orElse(null);
  }

  @Override
  public List<ChoixMobiliteDto> findAll() {
    return dbMobilites;
  }

  @Override
  public List<ChoixMobiliteDto> listerMobiliteDuneDemande(DemandeMobiliteDto demande) {
    List<ChoixMobiliteDto> liste = dbMobilites.stream()
        .filter(c -> c.getDemande().getId() == demande.getId()).collect(Collectors.toList());
    if (liste.size() == 0) {
      return null;
    }
    return liste;
  }

  @Override
  public ChoixMobiliteDto annulerChoixMobilite(ChoixMobiliteDto mobilite) {

    List<ChoixMobiliteDto> liste =
        dbMobilites.stream().filter(mob -> mob.equals(mobilite)).collect(Collectors.toList());
    if (liste.size() == 0) {
      return null;
    }
    liste.get(0).setAnnule(true);
    return liste.get(0);

  }

  @Override
  public ChoixMobiliteDto updateEtat(ChoixMobiliteDto mobilite) {

    List<ChoixMobiliteDto> liste =
        dbMobilites.stream().filter(mob -> mob.equals(mobilite)).collect(Collectors.toList());
    if (liste.size() == 0) {
      return null;
    }
    liste.get(0).setEtat(mobilite.getEtat());
    return liste.get(0);

  }



  @Override
  public ChoixMobiliteDto findProcessingMobility(DemandeMobiliteDto dem) {
    return dbMobilites.stream()
        .filter(cm -> cm.getDemande().getEtudiant().equals(dem.getEtudiant())).findAny().get();
  }



  @Override
  public List<ChoixMobiliteDto> listerMobiliteAvecDemandePayement(int annee) {
    return dbMobilites.stream()
        .filter(cm -> cm.getEtat().equals(Etat.DEMANDE_PAYEMENT)
            || cm.getEtat().equals(Etat.A_PAYER_SOLDE)
            || cm.getEtat().equals(Etat.DEMANDE_PAYEMENT_SOLDE))
        .collect(Collectors.toList());
  }


  @Override
  public boolean hasMobiliteConfirmed(UserDto etudiant) {

    List<ChoixMobiliteDto> liste = dbMobilites.stream()
        .filter(mob -> mob.getDemande().getEtudiant().getId() == etudiant.getId()
            && !mob.getEtat().equals(Etat.INTRO))
        .collect(Collectors.toList());

    if (liste.size() == 0) {
      return false;
    }
    return true;
  }


  @Override
  public boolean isLinked(PartenaireDto part) {

    if (dbMobilites.stream().filter(mob -> mob.getPartenaire().equals(part)).findFirst()
        .get() != null) {
      return true;
    }
    return false;
  }


}
