package dal.dao;

import core.config.AppConfig;
import core.exceptions.FatalException;
import dal.dao.interfaces.ChoixMobiliteDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.ChoixMobiliteDto.Etat;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.PartenaireDto;
import domaine.dto.PaysDto;
import domaine.dto.ProgrammeDto;
import domaine.dto.TypeProgrammeDto;
import domaine.dto.UserDto;
import util.AppUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ChoixMobiliteDaoImpl extends DaoImpl<ChoixMobiliteDto> implements ChoixMobiliteDao {

  @Override
  public ChoixMobiliteDto insererMobilite(ChoixMobiliteDto mob) {
    AppUtil.checkObjects(mob, mob.getDemande(), mob.getProgramme(), mob.getTypeProgramme());
    this.ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("c_mobilite"),
        Statement.RETURN_GENERATED_KEYS);
    Integer optionnal = null;
    try {
      this.ps.setInt(1, mob.getDemande().getId());
      this.ps.setString(2, mob.getEtat().toString());
      this.ps.setInt(3, mob.getQuadri());
      this.ps.setInt(4, mob.getProgramme().getId());
      this.ps.setInt(5, mob.getTypeProgramme().getId());
      // Le partenaire peut être null.
      if (mob.getPartenaire() != null) {
        optionnal = mob.getPartenaire().getId();
      }
      this.ps.setObject(6, optionnal);
      // La localité peut aussi être null.
      if (mob.getLocalite() != null) {
        optionnal = mob.getLocalite().getId();
      }
      this.ps.setObject(7, optionnal);

      this.ps.executeUpdate();

      ResultSet keys = this.ps.getGeneratedKeys();
      if (keys.next()) {
        mob.setId(keys.getInt(1));
        mob.setNumPreference(keys.getInt(2));
      }
      return mob;
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
  }


  @Override
  public List<ChoixMobiliteDto> findMobilityByAnneeAndEtat(ChoixMobiliteDto mob) {
    AppUtil.checkObject(mob);

    String etat = (mob.getEtat() != null) ? mob.getEtat().name() : "";
    List<ChoixMobiliteDto> list = null;

    this.ps = this.dalbs.createPrepareStatement(AppConfig.getValueOf("r_mob_rechercher"));
    try {
      this.ps.setString(1, "%" + etat + "%");
      this.ps.setString(2, "%" + mob.getDemande().getAnneeAcademique() + "%");
      try (ResultSet rs = this.ps.executeQuery()) {
        list = new ArrayList<ChoixMobiliteDto>(rs.getMetaData().getColumnCount());
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
  public List<ChoixMobiliteDto> listerMobiliteDuneDemande(DemandeMobiliteDto demande) {
    AppUtil.checkObject(demande);
    List<ChoixMobiliteDto> list = null;
    this.ps = this.dalbs.createPrepareStatement(AppConfig.getValueOf("r_mob_pour_demande"));

    try {
      this.ps.setInt(1, demande.getId());
      try (ResultSet rs = this.ps.executeQuery()) {
        list = new ArrayList<ChoixMobiliteDto>(rs.getMetaData().getColumnCount());
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
  public ChoixMobiliteDto annulerChoixMobilite(ChoixMobiliteDto mobilite) {
    AppUtil.checkObjects(mobilite, mobilite.getMotifAnnulation());
    this.ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("u_mob_annuler"));
    try {
      this.ps.setInt(1, mobilite.getMotifAnnulation().getId());
      this.ps.setInt(2, mobilite.getId());
      this.ps.setInt(3, mobilite.getNumPreference());
      this.ps.setInt(4, mobilite.getNumeroVersion());
      if (this.ps.executeUpdate() == 0) {
        return null;
      }
      return mobilite;
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
  }

  @Override
  public ChoixMobiliteDto updateEtat(ChoixMobiliteDto mobilite) {
    AppUtil.checkObject(mobilite);
    this.ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("u_mob_etat"));
    try {
      this.ps.setString(1, mobilite.getEtat().toString());
      if (mobilite.getPartenaire() == null) {
        this.ps.setNull(2, Types.INTEGER);
      } else {
        this.ps.setInt(2, mobilite.getPartenaire().getId());
      }
      this.ps.setInt(3, mobilite.getId());
      this.ps.setInt(4, mobilite.getNumPreference());
      this.ps.setInt(5, mobilite.getNumeroVersion());
      if (this.ps.executeUpdate() == 0) {
        return null;
      }
      return mobilite;
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
  }

  @Override
  public ChoixMobiliteDto findProcessingMobility(DemandeMobiliteDto dem) {
    AppUtil.checkObject(dem);
    this.ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("r_mob_rchr_en_cours"));
    try {
      this.ps.setInt(1, dem.getId());
      this.ps.setInt(2, dem.getAnneeAcademique());
      try (ResultSet res = this.ps.executeQuery()) {
        if (res.next()) {
          return super.fillEntityWithResult(res);
        } else {
          return null;
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
  }


  @Override
  public List<ChoixMobiliteDto> listerMobiliteAvecDemandePayement(int annee) {
    AppUtil.checkPositive(annee);
    this.ps = super.dalbs.createPrepareStatement(AppConfig.getValueOf("r_mob_avec_payement"));
    List<ChoixMobiliteDto> mobilites = new ArrayList<ChoixMobiliteDto>();
    try {
      this.ps.setInt(1, annee);
      try (ResultSet res = this.ps.executeQuery()) {
        UserDto etudiant;
        DemandeMobiliteDto demande;
        ChoixMobiliteDto mobilite;
        DepartementDto departement;
        PaysDto pays;
        ProgrammeDto programme;
        TypeProgrammeDto type;
        PartenaireDto partenaire;
        while (res.next()) {
          etudiant = this.factory.getUser();
          mobilite = this.factory.getChoixMobilite();
          demande = this.factory.getDemandeMobilite();
          departement = this.factory.getDepartement();
          pays = this.factory.getPays();
          partenaire = this.factory.getPartenaire();
          type = this.factory.getTypeProgramme();
          programme = this.factory.getProgramme();

          departement.setId(res.getInt("departement"));

          etudiant.setNom(res.getString("nom"));
          etudiant.setPrenom(res.getString("prenom"));
          etudiant.setMail(res.getString("email"));
          etudiant.setDateInscription(res.getDate("date_inscription").toLocalDate());
          etudiant.setProf(res.getBoolean("prof"));

          etudiant.setId(res.getInt("usr.id"));
          etudiant.setNumeroVersion(res.getInt("usr.num_verison"));

          etudiant.setMdp(res.getString("mdp"));
          etudiant.setPseudo(res.getString("pseudo"));
          etudiant.setDepartement(departement);

          demande.setNumeroVersion(res.getInt("dm.num_verison"));
          demande.setId(res.getInt("dm.id"));

          demande.setDateIntroduction(res.getDate("date_introduction").toLocalDate());
          demande.setAnneeAcademique(res.getInt("annee_academique"));

          mobilite.setEtat(Etat.valueOf(res.getString("etat")));
          mobilite.setId(res.getInt("candidature"));
          mobilite.setNumPreference(res.getInt("num_preference"));
          mobilite.setAnnule(res.getBoolean("annule"));
          mobilite.setQuadri(res.getInt("quadri"));
          pays.setId(res.getInt("pays"));
          mobilite.setLocalite(pays);
          type.setId(res.getInt("type_programme"));
          programme.setId(res.getInt("programme"));
          partenaire.setId(res.getInt("partenaire"));
          mobilite.setTypeProgramme(type);
          mobilite.setPartenaire(partenaire);
          mobilite.setProgramme(programme);


          demande.setEtudiant(etudiant);
          mobilite.setDemande(demande);
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return mobilites;
  }



  @Override
  public boolean hasMobiliteConfirmed(UserDto etudiant) {
    this.ps = this.dalbs.createPrepareStatement(AppConfig.getValueOf("r_mob_conf_for_usr"));
    try {
      this.ps.setInt(1, etudiant.getId());
      this.ps.setInt(2, AppUtil.determinerAnneeAcademiqueCourante());
      try (ResultSet rs = this.ps.executeQuery()) {
        if (rs.next()) {
          return true;
        }
      }
    } catch (SQLException exception) {
      throw new FatalException(exception.getMessage());
    }
    return false;
  }

  @Override
  public boolean isLinked(PartenaireDto part) {
    this.ps = this.dalbs.createPrepareStatement(AppConfig.getValueOf("r_is_linked_wt_part"));
    try {
      this.ps.setInt(1, part.getId());
      try (ResultSet rs = this.ps.executeQuery()) {
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
