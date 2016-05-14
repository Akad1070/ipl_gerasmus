package dal.dao;

import core.config.AppConfig;
import core.exceptions.FatalException;
import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.interfaces.LogicielDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.LogicielDto;
import util.AppUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@NoCache
public class LogicielDaoImpl extends DaoImpl<LogicielDto> implements LogicielDao {

  @Override
  public boolean annulerEncodageLogiciel(LogicielDto logiciel, ChoixMobiliteDto mobilite) {
    this.ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("d_encodage_logiciel"));

    try {
      this.ps.setInt(1, logiciel.getId());
      this.ps.setInt(2, mobilite.getNumCandidature());
      this.ps.setInt(3, mobilite.getNumPreference());
      this.ps.executeUpdate();
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return true;
  }

  @Override
  public boolean confirmerEncodageLogiciel(LogicielDto logiciel, ChoixMobiliteDto mobilite) {
    AppUtil.checkObject(mobilite);
    AppUtil.checkObject(logiciel);
    this.ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("c_encodage_logiciel"));
    try {
      this.ps.setInt(1, logiciel.getId());
      this.ps.setInt(2, mobilite.getNumCandidature());
      this.ps.setInt(3, mobilite.getNumPreference());
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return true;
  }

  @Override
  public List<LogicielDto> logicielsEncodes(ChoixMobiliteDto mobilite) {
    AppUtil.checkObject(mobilite);
    this.ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("r_logiciel_encod"));

    List<LogicielDto> logiciels = null;
    try {
      this.ps.setInt(1, mobilite.getNumCandidature());
      this.ps.setInt(2, mobilite.getNumPreference());
      try (ResultSet res = this.ps.executeQuery()) {
        logiciels = new ArrayList<LogicielDto>(res.getMetaData().getColumnCount());
        while (res.next()) {
          logiciels.add(super.fillEntityWithResult(res));
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return logiciels;
  }

  @Override
  public List<LogicielDto> logicielsAEncoder(ChoixMobiliteDto mobilite) {
    AppUtil.checkObject(mobilite);
    this.ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("r_logi_encodable"));

    List<LogicielDto> logiciels = null;
    try {
      this.ps.setInt(1, mobilite.getProgramme().getId());
      try (ResultSet res = this.ps.executeQuery()) {
        logiciels = new ArrayList<LogicielDto>(res.getMetaData().getColumnCount());
        while (res.next()) {
          logiciels.add(super.fillEntityWithResult(res));
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return logiciels;
  }

}
