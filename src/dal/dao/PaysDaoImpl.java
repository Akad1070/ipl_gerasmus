package dal.dao;

import core.config.AppConfig;
import core.exceptions.FatalException;
import dal.dao.interfaces.PaysDao;
import domaine.dto.PartenaireDto;
import domaine.dto.PaysDto;
import domaine.dto.ProgrammeDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaysDaoImpl extends DaoImpl<PaysDto> implements PaysDao {


  // TODO méthode pas utilisé
  @Override
  public PaysDto findByCode(PaysDto pays) {
    String query = AppConfig.getValueOf("r_code_pays");
    ps = dalbs.createPrepareStatement(query, 0);
    try {
      ps.setString(1, pays.getCode());
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


  // TODO méthode pas utilisé
  @Override
  public PaysDto findByNom(PaysDto pays) {
    String query = AppConfig.getValueOf("r_nom_pays");
    ps = dalbs.createPrepareStatement(query, 0);
    try {
      ps.setString(1, pays.getNom());
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


  // TODO méthode pas utilisé
  @Override
  public List<PaysDto> findByProgramme(ProgrammeDto proga) {
    String query = AppConfig.getValueOf("r_programme_pays");
    ps = dalbs.createPrepareStatement(query, 0);
    List<PaysDto> list = null;
    try {
      // this.ps.setInt(1, progra.getetId());
      try (ResultSet rs = ps.executeQuery()) {
        list = new ArrayList<PaysDto>();
        while (rs.next()) {
          list.add(super.fillEntityWithResult(rs));
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return list;
  }


  @Override
  public PaysDto findByPartenaire(PartenaireDto pat) {
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_part_pays"));
    try {
      ps.setInt(1, pat.getId());
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
