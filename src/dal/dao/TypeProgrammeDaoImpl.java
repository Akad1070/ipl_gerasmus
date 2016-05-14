package dal.dao;

import core.config.AppConfig;
import core.exceptions.FatalException;
import dal.dao.interfaces.TypeProgrammeDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.TypeProgrammeDto;
import util.AppUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TypeProgrammeDaoImpl extends DaoImpl<TypeProgrammeDto> implements TypeProgrammeDao {


  // TODO méthode pas utilisé
  @Override
  public TypeProgrammeDto findByName(TypeProgrammeDto programme) {
    AppUtil.checkObjects(programme, programme.getNom());
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_name_type_prog"));
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
  public TypeProgrammeDto findByMobilite(ChoixMobiliteDto mobi) {
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_mobi_type_prog"));
    try {
      ps.setInt(1, mobi.getNumCandidature());
      ps.setInt(2, mobi.getNumPreference());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return super.fillEntityWithResult(rs);
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return null;
  }


}
