package dal.dao;

import core.config.AppConfig;
import core.exceptions.DalException;
import core.exceptions.FatalException;
import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.interfaces.UserInfoDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.UserDto;
import domaine.dto.UserInfoDto;
import util.AppUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoCache
public class UserInfoDaoImpl extends DaoImpl<UserInfoDto> implements UserInfoDao {


  @Override
  public UserInfoDto insererInfo(UserInfoDto userInfo) {
    String query = AppConfig.getValueOf("c_user_info");
    return upsert(userInfo, query);
  }


  @Override
  public UserInfoDto update(UserInfoDto userInfo) {
    AppUtil.checkObject(userInfo);
    return upsert(userInfo, AppConfig.getValueOf("u_user_info"));
  }

  // TODO méthode pas utilisé
  @Override
  public UserInfoDto rechercherInfo(UserDto user) {
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_user_id_user_info"));
    try {
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



  private UserInfoDto upsert(UserInfoDto userInfo, String sql) {
    AppUtil.checkObject(userInfo);
    ps = super.dalbs.createPrepareStatement(sql);
    try {
      ps.setString(1, userInfo.getCivilite().toString());
      ps.setString(2, userInfo.getNationalite());
      ps.setString(3, userInfo.getAdresse());
      ps.setString(4, userInfo.getTel());
      ps.setString(5, userInfo.getEmail());
      ps.setString(6, userInfo.getSexe().toString());
      ps.setString(7, userInfo.getCompteBancaire());
      ps.setString(8, userInfo.getTitulaire());
      ps.setString(9, userInfo.getBanque());
      ps.setString(10, userInfo.getBic());
      ps.setDate(11, Date.valueOf(userInfo.getDateNais()));
      ps.setInt(12, userInfo.getNombreAnneeReussie());

      if (userInfo.getId() > 0) {
        ps.setInt(13, userInfo.getId());
        ps.setInt(14, userInfo.getNumeroVersion());
      }
      return (ps.executeUpdate() > 0) ? userInfo : null;
    } catch (SQLException exception) {
      if (userInfo.getId() > 0) {
        throw new DalException(exception.getMessage());
      } else {
        throw new FatalException(exception.getMessage());
      }
    }

  }


  @Override
  public boolean compteBancaireNotNull(ChoixMobiliteDto mobilite) {
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_compte_bancaire"));
    try {
      ps.setInt(1, mobilite.getNumCandidature());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return true;
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return false;
  }

}
