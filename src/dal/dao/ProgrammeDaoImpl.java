package dal.dao;

import core.config.AppConfig;
import core.exceptions.FatalException;
import dal.dao.interfaces.ProgrammeDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.ProgrammeDto;
import util.AppUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProgrammeDaoImpl extends DaoImpl<ProgrammeDto> implements ProgrammeDao {


  // TODO méthode pas utilisé
  @Override
  public ProgrammeDto findByName(ProgrammeDto programme) {
    AppUtil.checkObjects(programme, programme.getNom());
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_name_prog"), 0);
    try {
      ps.setString(1, programme.getNom());
      try (ResultSet res = ps.executeQuery()) {
        if (res.next()) {
          return fillEntityWithResult(res);
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return null;
  }


  @Override
  public ProgrammeDto findByMobilite(ChoixMobiliteDto mobi) {
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_mobi_prog"));
    try {
      ps.setInt(1, mobi.getNumCandidature());
      ps.setInt(2, mobi.getNumPreference());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          return super.fillEntityWithResult(rs);
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return null;
  }

}
