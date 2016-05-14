package dal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import core.config.AppConfig;
import core.exceptions.FatalException;
import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.interfaces.PartenaireDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;
import util.AppUtil;

@NoCache
public class PartenaireDaoImpl extends DaoImpl<PartenaireDto> implements PartenaireDao {

  @Override
  public PartenaireDto inserer(PartenaireDto partenaire) {
    AppUtil.checkObjects(partenaire, partenaire.getPays());
    String query = AppConfig.getValueOf("c_partenaire");
    ps = super.dalbs.createPrepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    try {
      ps.setString(1, partenaire.getNomLegal());
      ps.setString(2, partenaire.getNomAffaire());
      if (partenaire.getTypeOrganisation() != null) {
        ps.setString(3, partenaire.getTypeOrganisation().toString());
      } else {
        ps.setNull(3, Types.VARCHAR);
      }
      ps.setInt(4, partenaire.getNbEmploye());
      ps.setString(5, partenaire.getAdresse());
      ps.setInt(6, partenaire.getPays().getId());
      ps.setString(7, partenaire.getRegion());
      ps.setString(8, partenaire.getVille());
      ps.setString(9, partenaire.getMail());
      ps.setString(10, partenaire.getSiteWeb());
      ps.setString(11, partenaire.getTelephone());
      if (partenaire.getCreateur() != null) {
        ps.setInt(12, partenaire.getCreateur().getId());
      } else {
        ps.setNull(12, Types.INTEGER);
      }
      ps.setString(13, partenaire.getNomComplet());
      ps.executeUpdate();
      ResultSet keys = ps.getGeneratedKeys();
      if (keys.next()) {
        partenaire.setId(keys.getInt(1));
      }
      return partenaire;
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
  }


  @Override
  public PartenaireDto lierADepartement(PartenaireDto partenaire, DepartementDto departement) {
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("c_departement_part"));
    try {
      ps.setInt(1, departement.getId());
      ps.setInt(2, partenaire.getId());
      ps.executeUpdate();
      return partenaire;
    } catch (SQLException exception) {
      throw new FatalException(exception);
    }
  }


  @Override
  public List<PartenaireDto> rechercher(PartenaireDto partenaire) {
    AppUtil.checkObject(partenaire);

    String nom = (partenaire.getNomLegal() != null) ? partenaire.getNomLegal() : "";
    String pays = (partenaire.getPays()) != null ? partenaire.getPays().getNom() : "";
    String ville = (partenaire.getVille() != null) ? partenaire.getVille() : "";
    List<PartenaireDto> list = null;

    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_part_demander"));
    try {
      ps.setString(1, "%" + nom + "%");
      ps.setString(2, "%" + ville + "%");
      ps.setString(3, "%" + pays + "%");

      try (ResultSet rs = ps.executeQuery()) {
        list = new ArrayList<PartenaireDto>(rs.getMetaData().getColumnCount());
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
  public PartenaireDto findByMobilite(ChoixMobiliteDto mobi) {
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_mobi_part"));
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

  @Override
  public PartenaireDto changerVisibilite(PartenaireDto part) {
    AppUtil.checkObject(part);
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("u_part_visibilite"));

    try {
      ps.setBoolean(1, part.isVisible());
      ps.setInt(2, part.getNumeroVersion());
      if (ps.executeUpdate() == 0) {
        return null;
      }
      part.setNumeroVersion(part.getNumeroVersion() + 1);
      return part;
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
  }


  @Override
  public List<PartenaireDto> listerPartenairesSelectionnablesPourEtudiants() {
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_part_for_all"));
    List<PartenaireDto> partenaires = new ArrayList<>();

    try (ResultSet res = ps.executeQuery()) {
      while (res.next()) {
        partenaires.add(super.fillEntityWithResult(res));
      }

    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return partenaires;
  }


  @Override
  public List<PartenaireDto> listerPartenairesSelectionnables(UserDto etudiant) {
    AppUtil.checkObject(etudiant);

    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_part_for_student"));

    List<PartenaireDto> partenaires = new ArrayList<>();
    try {
      ps.setInt(1, etudiant.getId());
      try (ResultSet res = ps.executeQuery()) {
        while (res.next()) {
          partenaires.add(super.fillEntityWithResult(res));
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return partenaires;
  }


}
