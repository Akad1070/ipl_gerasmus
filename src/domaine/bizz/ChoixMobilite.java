package domaine.bizz;

import dal.dao.core.DbEntity;
import dal.dao.core.DbEntityColumn;
import dal.dao.core.DbEntityFk;
import domaine.bizz.interfaces.ChoixMobiliteBizz;
import domaine.dto.AnnulationDto;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DocumentDto;
import domaine.dto.LogicielDto;
import domaine.dto.PartenaireDto;
import domaine.dto.PaysDto;
import domaine.dto.ProgrammeDto;
import domaine.dto.TypeProgrammeDto;
import util.AppUtil;

import java.util.List;
import java.util.Map;


@DbEntity(table = "choix_mobilites")
public class ChoixMobilite extends BaseEntiteImpl implements ChoixMobiliteBizz {

  // Vu que la PK est concaténé, l'id qui est dans la BaseEntity sert de fk candidature.

  @DbEntityColumn("num_preference")
  private Integer preference = 1;

  @DbEntityColumn("etat")
  private Etat etat;

  @DbEntityColumn("quadri")
  private Integer quadri;

  @DbEntityColumn("annule")
  private boolean annule;


  @DbEntityColumn("pays")
  private Integer fkPays;

  @DbEntityFk
  private PaysDto localite;


  @DbEntityColumn("programme")
  private Integer fkProgramme;
  @DbEntityFk
  private ProgrammeDto programme;


  @DbEntityColumn("type_programme")
  private int fkTypeProgramme;
  @DbEntityFk
  private TypeProgrammeDto typeProgramme;


  @DbEntityColumn("partenaire")
  private Integer fkPartenaire;
  @DbEntityFk
  private PartenaireDto partenaire;


  @DbEntityColumn("motif_annulation")
  private Integer fkMotifAnnulation;
  @DbEntityFk
  private AnnulationDto motif;


  @DbEntityFk
  private DemandeMobiliteDto demande;

  @DbEntityFk
  private List<DocumentDto> documentsSignes;

  @DbEntityFk
  private Map<DocumentDto, Boolean> etatsDocumentsSignes;

  @DbEntityFk
  private List<LogicielDto> logiciels;

  @DbEntityFk
  private Map<LogicielDto, Boolean> etatsLogicielsEncodes;



  @Override
  public Etat getEtat() {
    return etat;
  }

  @Override
  public void setEtat(Etat etat) {
    this.etat = etat;
  }

  @Override
  public PaysDto getLocalite() {
    return localite;
  }

  @Override
  public void setLocalite(PaysDto localite) {
    AppUtil.checkObject(localite, "La localite ne peut pas être nulle");
    this.localite = localite;
  }

  @Override
  public Integer getQuadri() {
    return quadri;
  }

  @Override
  public void setQuadri(int quadri) {
    AppUtil.checkPositive(quadri);
    this.quadri = quadri;
  }

  @Override
  public boolean isAnnule() {
    return annule;
  }

  @Override
  public void setAnnule(boolean annule) {
    this.annule = annule;
  }


  @Override
  public PartenaireDto getPartenaire() {
    return partenaire;
  }

  @Override
  public void setPartenaire(PartenaireDto part) {
    AppUtil.checkObject(part, "Le partenaire de cette moblite ne peut pas être null");
    partenaire = part;
  }

  @Override
  public AnnulationDto getMotifAnnulation() {
    return motif;
  }

  @Override
  public void setMotifAnnulation(AnnulationDto motifAnnul) {
    AppUtil.checkObject(motifAnnul);
    motif = motifAnnul;
    fkMotifAnnulation = motifAnnul.getId();
  }

  @Override
  public DemandeMobiliteDto getDemande() {
    return demande;
  }


  @Override
  public void setDemande(DemandeMobiliteDto dem) {
    AppUtil.checkObject(dem, "La demande de mobilite pour ce choix ne peut pas être null.");
    demande = dem;
    id = dem.getId();
  }


  @Override
  public ProgrammeDto getProgramme() {
    return programme;
  }

  @Override
  public void setProgramme(ProgrammeDto programme) {
    AppUtil.checkObject(programme, "Le programme ne peut pas etre null");
    this.programme = programme;
  }

  @Override
  public TypeProgrammeDto getTypeProgramme() {
    return typeProgramme;
  }

  @Override
  public void setTypeProgramme(TypeProgrammeDto typeProgramme) {
    AppUtil.checkObject(typeProgramme, "Le programme ne peut pas etre null");
    this.typeProgramme = typeProgramme;
  }


  @Override
  public Integer getNumPreference() {
    return preference;
  }

  @Override
  public void setNumPreference(Integer preference) {
    this.preference = preference;
  }

  @Override
  public Integer getNumCandidature() {
    return id;
  }

  @Override
  public void setNumCandidature(int num) {
    AppUtil.checkPositive(num, "Le numéro de candidature ne peut pas etre null");
    id = num;
  }


  @Override
  public List<DocumentDto> getDocumentsSignes() {
    return documentsSignes;
  }

  @Override
  public void setDocumentsSignes(List<DocumentDto> documentsSignes) {
    AppUtil.checkObject(documentsSignes);
    this.documentsSignes = documentsSignes;
  }

  @Override
  public List<LogicielDto> getLogiciels() {
    return logiciels;
  }

  @Override
  public void setLogiciels(List<LogicielDto> logiciels) {
    AppUtil.checkObject(logiciels);
    this.logiciels = logiciels;
  }

  @Override
  public void setEtatsDocumentsSignes(Map<DocumentDto, Boolean> etatsDocs) {
    AppUtil.checkObject(etatsDocs);
    etatsDocumentsSignes = etatsDocs;
  }

  @Override
  public Map<DocumentDto, Boolean> getEtatsDocumentsSignes() {
    return etatsDocumentsSignes;
  }

  @Override
  public void setEtatsLogicielsEncodes(Map<LogicielDto, Boolean> etatsLogis) {
    AppUtil.checkObject(etatsLogis);
    etatsLogicielsEncodes = etatsLogis;
  }

  @Override
  public Map<LogicielDto, Boolean> getEtatsLogicielsEncodes() {
    return etatsLogicielsEncodes;
  }

  // TODO A verifier si conforme et valide
  @Override
  public boolean isConfirme() {
    return !annule && etat != Etat.INTRO;
  }


}
