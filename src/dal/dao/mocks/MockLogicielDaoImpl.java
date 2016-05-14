package dal.dao.mocks;

import core.config.AppConfig;
import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.LogicielDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.LogicielDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoCache
public class MockLogicielDaoImpl extends DaoImpl<LogicielDto> implements LogicielDao {

  private final List<LogicielDto> dbLogiciels =
      Collections.synchronizedList(new ArrayList<LogicielDto>());
  private final List<LogicielDto> dbLogicielsAll =
      Collections.synchronizedList(new ArrayList<LogicielDto>());

  private void injecterDb() {
    if (dbLogicielsAll.size() > 0) {
      return;
    }
    dbLogicielsAll.add(factory.getLogiciel());

    LogicielDto dto = factory.getLogiciel();
    dto.setId(AppConfig.getInt("id2_logi"));
    dto.setNom(AppConfig.getValueOf("nom2_logi"));
    dbLogicielsAll.add(dto);
  }


  @Override
  public LogicielDto findById(int id) {
    injecterDb();
    return dbLogiciels.stream().filter(l -> l.getId() == id).findFirst()
        .orElse(factory.getLogiciel());
  }

  @Override
  public List<LogicielDto> findAll() {
    injecterDb();
    return dbLogicielsAll;
  }

  @Override
  public boolean annulerEncodageLogiciel(LogicielDto logiciel, ChoixMobiliteDto mobilite) {

    if (logiciel == null || mobilite == null) {
      return false;
    }
    dbLogiciels.remove(logiciel);
    return true;
  }

  @Override
  public boolean confirmerEncodageLogiciel(LogicielDto logiciel, ChoixMobiliteDto mobilite) {

    if (logiciel == null || mobilite == null) {
      return false;
    }
    dbLogiciels.add(logiciel);
    return true;
  }

  @Override
  public List<LogicielDto> logicielsEncodes(ChoixMobiliteDto mobilite) {

    return dbLogiciels.stream().filter(log -> mobilite.getLogiciels().contains(log))
        .collect(Collectors.toList());
  }

  @Override
  public List<LogicielDto> logicielsAEncoder(ChoixMobiliteDto mobilite) {
    injecterDb();
    return dbLogicielsAll;
  }


}
