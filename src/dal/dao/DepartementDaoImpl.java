package dal.dao;

import core.config.AppConfig;
import core.exceptions.FatalException;
import dal.dao.interfaces.DepartementDao;
import domaine.dto.DepartementDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;
import util.AppUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartementDaoImpl extends DaoImpl<DepartementDto> implements DepartementDao {

  @Override
  public DepartementDto findByCode(DepartementDto departement) {
    try {
      ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_code_departement"), 0);
      ps.setString(1, departement.getCode());
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


  @Override
  public DepartementDto findByUser(UserDto user) {
    AppUtil.checkObject(user);
    try {
      ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_user_departement"));
      ps.setInt(1, user.getId());
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


  @Override
  public List<DepartementDto> findByPartenaire(PartenaireDto pat) {
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_part_departement"));
    List<DepartementDto> list = null;
    try {
      ps.setInt(1, pat.getId());
      try (ResultSet rs = ps.executeQuery()) {

        list = new ArrayList<DepartementDto>(rs.getMetaData().getColumnCount());
        while (rs.next()) {
          list.add(super.fillEntityWithResult(rs));
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return list;
  }


}
