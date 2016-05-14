package dal.dao;

import core.config.AppConfig;
import core.exceptions.FatalException;
import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.interfaces.AnnulationDao;
import domaine.dto.AnnulationDto;
import domaine.dto.ChoixMobiliteDto;
import util.AppUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@NoCache
public class AnnulationDaoImpl extends DaoImpl<AnnulationDto> implements AnnulationDao {

  @Override
  public AnnulationDto insererAnnulation(AnnulationDto motif) {

    AppUtil.checkObject(motif);
    ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("c_annulation"),
        Statement.RETURN_GENERATED_KEYS);
    try {
      ps.setInt(1, motif.getCreateur().getId());
      ps.setString(2, motif.getMotif());
      ps.executeUpdate();

      ResultSet keys = ps.getGeneratedKeys();
      if (keys.next()) {
        motif.setId(keys.getInt(1));
      }
      return motif;
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
  }

  @Override
  public List<AnnulationDto> listerAnnulationPourProf() {
    List<AnnulationDto> list = null;
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_annulation"));
    try (ResultSet rs = ps.executeQuery()) {
      list = new ArrayList<AnnulationDto>(rs.getMetaData().getColumnCount());
      while (rs.next()) {
        list.add(super.fillEntityWithResult(rs));
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return list;
  }


  @Override
  public AnnulationDto findByMobilite(ChoixMobiliteDto mobi) {
    ps = dalbs.createPrepareStatement(AppConfig.getValueOf("r_mobi_annul"));
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
