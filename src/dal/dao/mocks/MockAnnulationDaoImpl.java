package dal.dao.mocks;

import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.AnnulationDao;
import domaine.dto.AnnulationDto;
import domaine.dto.ChoixMobiliteDto;
import util.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoCache
public class MockAnnulationDaoImpl extends DaoImpl<AnnulationDto> implements AnnulationDao {
  private final List<AnnulationDto> dbAnnulations =
      Collections.synchronizedList(new ArrayList<AnnulationDto>());

  @Override
  public AnnulationDto findById(int id) {
    return dbAnnulations.stream().filter(a -> a.getId() == id).findFirst()
        .orElse(factory.getAnnulation());
  }

  @Override
  public List<AnnulationDto> findAll() {
    return dbAnnulations;
  }

  @Override
  public AnnulationDto insererAnnulation(AnnulationDto motif) {
    AppUtil.checkObject(motif);
    dbAnnulations.add(motif);
    return motif;
  }

  @Override
  public List<AnnulationDto> listerAnnulationPourProf() {
    return dbAnnulations.stream().filter(a -> a.getCreateur().isProf())
        .collect(Collectors.toList());
  }

  @Override
  public AnnulationDto findByMobilite(ChoixMobiliteDto mobi) {
    return factory.getAnnulation();
  }


}
