package dal.dao;

import core.config.AppConfig;
import core.exceptions.FatalException;
import dal.dao.interfaces.DemandeMobiliteDao;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.UserDto;
import util.AppUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemandeMobiliteDaoImpl extends DaoImpl<DemandeMobiliteDto>
    implements DemandeMobiliteDao {

  @Override
  public DemandeMobiliteDto insererDemande(DemandeMobiliteDto dem) {
    AppUtil.checkObject(dem);
    ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("c_demande_mobi"),
        Statement.RETURN_GENERATED_KEYS);
    try {
      ps.setDate(1, Date.valueOf(dem.getDateIntroduction()));
      ps.setInt(2, dem.getAnneeAcademique());
      ps.setInt(3, dem.getEtudiant().getId());
      ps.executeUpdate();

      ResultSet keys = ps.getGeneratedKeys();
      if (keys.next()) {
        dem.setId(keys.getInt(1));
      }
      return dem;
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
  }


  @Override
  public DemandeMobiliteDto findByAnneeAndStudent(DemandeMobiliteDto demande) {
    AppUtil.checkObject(demande);
    ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("r_annee_user_mobi"));
    try {
      ps.setInt(1, demande.getAnneeAcademique());
      ps.setInt(2, demande.getEtudiant().getId());
      try (ResultSet res = ps.executeQuery()) {
        if (res.next()) {
          return super.fillEntityWithResult(res);
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception);
    }
    return null;
  }


  @Override
  public List<DemandeMobiliteDto> findByStudent(UserDto etudiant) {
    AppUtil.checkObject(etudiant);
    List<DemandeMobiliteDto> list = null;

    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_etud_mobi"));

    try {
      ps.setInt(1, etudiant.getId());
      // ps.setInt(2, AppUtil.determinerAnneeAcademiqueCourante());
      try (ResultSet rs = ps.executeQuery()) {
        list = new ArrayList<DemandeMobiliteDto>(rs.getMetaData().getColumnCount());
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
  public List<DemandeMobiliteDto> listerDemande(List<DepartementDto> departements) {
    AppUtil.checkObject(departements);
    List<DemandeMobiliteDto> list = null;
    String tab =
        departements.stream().map(d -> String.valueOf(d.getId())).collect(Collectors.joining(","));
    String query = String.format(AppConfig.getValueOf("r_annee_mobi"), tab);
    ps = dalbs.createPrepareStatement(query);

    try {
      ps.setInt(1, AppUtil.determinerAnneeAcademiqueCourante());
      try (ResultSet rs = ps.executeQuery()) {
        list = new ArrayList<DemandeMobiliteDto>(rs.getMetaData().getColumnCount());
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
  public List<Integer> findAllAnnee() {
    List<Integer> list = null;
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_all_annee"));
    try (ResultSet rs = ps.executeQuery()) {
      list = new ArrayList<Integer>();
      while (rs.next()) {
        list.add(rs.getInt("annee_academique"));
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return list;
  }


}
