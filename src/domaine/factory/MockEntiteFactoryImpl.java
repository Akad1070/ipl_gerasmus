package domaine.factory;

import core.config.AppConfig;
import domaine.bizz.UserInfo;
import domaine.dto.AnnulationDto;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.ChoixMobiliteDto.Etat;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.DocumentDto;
import domaine.dto.LogicielDto;
import domaine.dto.PartenaireDto;
import domaine.dto.PartenaireDto.TypeOrganisation;
import domaine.dto.PaysDto;
import domaine.dto.ProgrammeDto;
import domaine.dto.TypeProgrammeDto;
import domaine.dto.UserDto;

import java.time.LocalDate;
import java.util.Arrays;


/**
 * Usine à mock.
 *
 * @author Akad
 */
public class MockEntiteFactoryImpl extends EntiteFactoryImpl implements EntiteFactory {


  @Override
  public UserDto getUser() {
    UserDto dto = super.getUser();
    dto.setId(AppConfig.getInt("id_user"));
    dto.setPseudo(AppConfig.getValueOf("pseudo_user"));
    dto.setMdp(AppConfig.getValueOf("mdp_clair_user"));
    dto.setNom(AppConfig.getValueOf("nom_user"));
    dto.setPrenom(AppConfig.getValueOf("prenom_user"));
    dto.setProf(false);
    dto.setMail(AppConfig.getValueOf("mail_etud_user"));
    dto.setDepartement(getDepartement());
    dto.setDateInscription(LocalDate.of(2016, 02, 10));
    return dto;
  }


  @Override
  public UserInfo getUserInfo() {
    UserInfo dto = super.getUserInfo();
    dto.setAdresse(AppConfig.getValueOf("adresse_info"));
    dto.setBanque(AppConfig.getValueOf("bank_info"));
    dto.setBic(AppConfig.getValueOf("code_bic_info"));
    dto.setCivilite(AppConfig.getValueOf("civilite_info"));
    dto.setCompteBancaire(AppConfig.getValueOf("compte_bank_info"));
    dto.setDateNais(LocalDate.parse(AppConfig.getValueOf("date_nais_info")));
    dto.setEmail(AppConfig.getValueOf("mail_info"));
    dto.setId(AppConfig.getInt("id_user"));
    dto.setNationalite(AppConfig.getValueOf("nation_info"));
    dto.setNombreAnneeReussie(AppConfig.getInt("nb_ans_reussis_info"));
    dto.setSexe(AppConfig.getValueOf("sexe_info"));
    dto.setTel(AppConfig.getValueOf("tel_info"));
    dto.setTitulaire(AppConfig.getValueOf("compte_titu_info"));
    return dto;
  }



  @Override
  public DepartementDto getDepartement() {
    DepartementDto dto = super.getDepartement();
    dto.setId(AppConfig.getInt("id_dep"));
    dto.setCode(AppConfig.getValueOf("code_dep"));
    dto.setNom(AppConfig.getValueOf("nom_dep"));
    return dto;
  }


  @Override
  public ChoixMobiliteDto getChoixMobilite() {
    ChoixMobiliteDto dto = super.getChoixMobilite();
    dto.setDemande(getDemandeMobilite());
    dto.setId(AppConfig.getInt("id_demande"));
    dto.setNumPreference(1);
    dto.setQuadri(AppConfig.getInt("quadri_choix"));
    dto.setEtat(Etat.valueOf(AppConfig.getValueOf("etat_choix")));
    dto.setPartenaire(getPartenaire());
    dto.setProgramme(getProgramme());
    dto.setTypeProgramme(getTypeProgramme());
    return dto;
  }



  @Override
  public DemandeMobiliteDto getDemandeMobilite() {
    DemandeMobiliteDto dto = super.getDemandeMobilite();
    dto.setId(AppConfig.getInt("id_demande"));
    dto.setAnneeAcademique(AppConfig.getInt("annee_acad_demande"));
    dto.setDateIntroduction(LocalDate.parse(AppConfig.getValueOf("date_intro_demande")));
    dto.setEtudiant(getUser());
    return dto;
  }



  @Override
  public ProgrammeDto getProgramme() {
    ProgrammeDto dto = super.getProgramme();
    dto.setNom(AppConfig.getValueOf("nom_prog"));
    return dto;
  }


  @Override
  public TypeProgrammeDto getTypeProgramme() {
    TypeProgrammeDto dto = super.getTypeProgramme();
    dto.setNom(AppConfig.getValueOf("nom_type_prog"));
    return dto;
  }

  @Override
  public PartenaireDto getPartenaire() {
    PartenaireDto pat = super.getPartenaire();
    pat.setId(AppConfig.getInt("id_pat"));
    pat.setNomAffaire(AppConfig.getValueOf("nom_affaire_pat"));
    pat.setNomLegal(AppConfig.getValueOf("nom_legal_pat"));
    // pat.setCreateur(getUser());
    pat.setMail(AppConfig.getValueOf("mail_pat"));
    pat.setTelephone(AppConfig.getValueOf("tel_pat"));
    pat.setAdresse(AppConfig.getValueOf("adresse_pat"));
    pat.setCodePostal(AppConfig.getValueOf("cp_pat"));
    pat.setVille(AppConfig.getValueOf("ville_pat"));
    pat.setRegion(AppConfig.getValueOf("region_pat"));
    pat.setPays(getPays());
    pat.setSiteWeb(AppConfig.getValueOf("site_pat"));
    pat.setNbEmploye(AppConfig.getInt("nb_employee_pat"));
    pat.setTypeOrganisation(TypeOrganisation.valueOf(AppConfig.getValueOf("type_orga_pat")));
    pat.setIplDepartements(Arrays.asList(getDepartement()));
    return pat;
  }



  @Override
  public PaysDto getPays() {
    PaysDto dto = super.getPays();
    dto.setId(AppConfig.getInt("id_pays"));
    dto.setCode(AppConfig.getValueOf("code_pays"));
    dto.setNom(AppConfig.getValueOf("nom_pays"));
    return dto;
  }


  @Override
  public AnnulationDto getAnnulation() {
    AnnulationDto dto = super.getAnnulation();
    dto.setId(AppConfig.getInt("id_annul"));
    dto.setMotif(AppConfig.getValueOf("motif_annul"));
    dto.setCreateur(getUser());
    return dto;
  }


  @Override
  public DocumentDto getDocument() {
    // TODO Auto-generated method stub
    DocumentDto dto = super.getDocument();
    dto.setId(AppConfig.getInt("id_doc"));
    dto.setGenre(AppConfig.getValueOf("genre_doc"));
    dto.setMobilite(getChoixMobilite());
    dto.setNom("nom_doc");
    dto.setProgramme(getProgramme());
    dto.setTypeProgramme(getTypeProgramme());
    return dto;
  }



  @Override
  public LogicielDto getLogiciel() {
    LogicielDto dto = super.getLogiciel();
    dto.setId(AppConfig.getInt("id_logi"));
    dto.setNom(AppConfig.getValueOf("nom_logi"));
    return dto;
  }



}
