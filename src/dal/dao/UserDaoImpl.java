package dal.dao;

import core.config.AppConfig;
import core.exceptions.FatalException;
import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.interfaces.UserDao;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;
import util.AppUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@NoCache
public class UserDaoImpl extends DaoImpl<UserDto> implements UserDao {

  @Override
  public UserDto inscrire(UserDto user) {
    String query = AppConfig.getValueOf("c_user");

    ps = super.dalbs.createPrepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    try {
      ps.setString(1, user.getPseudo());
      ps.setString(2, user.getMdp());
      ps.setString(3, user.getNom());
      ps.setString(4, user.getPrenom());
      ps.setInt(5, user.getDepartement().getId());
      ps.setString(6, user.getMail());
      ps.setDate(7, Date.valueOf(user.getDateInscription()));
      ps.setBoolean(8, user.isProf());
      ps.executeUpdate();
      ResultSet keys = ps.getGeneratedKeys();
      if (keys.next()) {
        user.setId(keys.getInt(1));
      }
      return user;
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
  }

  @Override
  public UserDto getUserByPseudo(UserDto user) {
    String query = AppConfig.getValueOf("r_pseudo_user");
    ps = dalbs.createPrepareStatement(query);
    try {
      ps.setString(1, user.getPseudo());
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
  public UserDto getUserByMail(UserDto user) {
    String query = AppConfig.getValueOf("r_mail_user");
    ps = dalbs.createPrepareStatement(query);
    try {
      ps.setString(1, user.getMail());
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
  public List<UserDto> findByName(UserDto user) {
    AppUtil.checkObject(user);

    String nom = (user.getNom() != null) ? user.getNom() : "";
    String prenom = (user.getPrenom() != null) ? user.getPrenom() : "";
    String query = AppConfig.getValueOf("r_name_user");
    List<UserDto> list = null;
    ps = dalbs.createPrepareStatement(query);

    try {
      ps.setString(1, "%" + nom + "%");
      ps.setString(2, "%" + prenom + "%");
      try (ResultSet rs = ps.executeQuery()) {
        list = new ArrayList<UserDto>(rs.getMetaData().getColumnCount());
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
  public UserDto findByPartenaire(PartenaireDto pat) {
    AppUtil.checkObject(pat);
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_pat_createur"));
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


  @Override
  public UserDto findByDemandeMobilite(DemandeMobiliteDto candidature) {
    AppUtil.checkObject(candidature);
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_mobi_etud"));
    try {
      ps.setInt(1, candidature.getId());
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
