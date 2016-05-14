package domaine.ucc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import core.AppContext;
import core.exceptions.BizzException;
import core.injecteur.InjecteurDependance.Injecter;
import dal.dao.core.DaoFactoryImpl;
import domaine.dto.AnnulationDto;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.ChoixMobiliteDto.Etat;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.DepartementDto;
import domaine.dto.DocumentDto;
import domaine.dto.LogicielDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;
import domaine.dto.UserInfoDto;
import domaine.factory.EntiteFactory;
import domaine.ucc.interfaces.ProfesseurUcc;
import domaine.ucc.interfaces.QuidamUcc;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProfesseurUccTest {

  private static AppContext ct;

  @Injecter
  private EntiteFactory factory;

  @Injecter
  private ProfesseurUcc profUcc;
  @Injecter
  private QuidamUcc quidUcc;

  private DemandeMobiliteDto demandeMob;
  private ChoixMobiliteDto choixMob;
  private UserDto user;
  private PartenaireDto partenaire;
  private UserInfoDto userInfos;
  private DepartementDto departement;
  private AnnulationDto annulation;
  private DocumentDto document;
  private LogicielDto logiciel;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    ct = new AppContext(AppContext.Profil.TEST, "Gerasmus");
  }

  @Before
  public void setUp() throws Exception {
    ct.getInjector().removeFromCache(DaoFactoryImpl.class);
    ct.getInjector().removeFromCache(ProfesseurUccImpl.class);
    ct.getInjector().removeFromCache(QuidamUccImpl.class);
    ct.getInjector().injectByAnnotedField(this);
    this.demandeMob = this.factory.getDemandeMobilite();
    this.choixMob = this.factory.getChoixMobilite();
    this.user = this.factory.getUser();
    this.partenaire = this.factory.getPartenaire();
    this.userInfos = this.factory.getUserInfo();
    this.departement = this.factory.getDepartement();
    this.annulation = this.factory.getAnnulation();
    this.document = this.factory.getDocument();
    this.logiciel = this.factory.getLogiciel();

  }

  @Test(expected = BizzException.class)
  public void testEncoderSignatureDocumentsParametres() {

    this.profUcc.encoderSignatureDocuments(null);
  }

  @Test
  public void testEncoderSignatureDocumentsAjouterDocument() {

    this.quidUcc.inscrire(this.user);
    this.demandeMob.setChoixMobilites(Arrays.asList(this.choixMob));
    this.profUcc.declarerChoix(this.demandeMob);
    this.profUcc.confirmerPartenaire(this.choixMob);

    DocumentDto document2 = this.factory.getDocument();
    document2.setId(2);
    this.choixMob.setDocumentsSignes(Arrays.asList(document2));
    this.profUcc.encoderSignatureDocuments(this.choixMob);

    Map<DocumentDto, Boolean> map = this.profUcc.listerEtatDocuments(this.choixMob);

    assertEquals("L'état de la mobilité doit correspondre.", Etat.PREPA, this.choixMob.getEtat());
    assertTrue("Le document devait être signé.", map.get(this.document));
    assertTrue("Le document devait être signé.", map.get(document2));
  }

  @Test
  public void testEncoderSignatureDocumentsAjouterDocumentRollback() {

    this.quidUcc.inscrire(this.user);
    this.demandeMob.setChoixMobilites(Arrays.asList(this.choixMob));
    this.profUcc.declarerChoix(this.demandeMob);
    this.profUcc.confirmerPartenaire(this.choixMob);

    DocumentDto document2 = this.factory.getDocument();
    document2.setId(2);
    this.document.setId(0);
    this.choixMob.setDocumentsSignes(Arrays.asList(this.document, document2));
    this.profUcc.encoderSignatureDocuments(this.choixMob);

    Map<DocumentDto, Boolean> map = this.profUcc.listerEtatDocuments(this.choixMob);

    assertFalse("Le document ne devrait pas être signé.", map.get(document2));
  }

  @Test
  public void testEncoderSignatureDocumentsAjouterDocumentAPayerSolde() {

    this.quidUcc.inscrire(this.user);
    this.demandeMob.setChoixMobilites(Arrays.asList(this.choixMob));
    this.profUcc.declarerChoix(this.demandeMob);
    this.profUcc.confirmerPartenaire(this.choixMob);

    DocumentDto document2 = this.factory.getDocument();
    document2.setId(2);
    DocumentDto document3 = this.factory.getDocument();
    document3.setId(3);
    DocumentDto document4 = this.factory.getDocument();
    document4.setId(4);
    this.choixMob.setDocumentsSignes(Arrays.asList(document2, document3, document4));
    this.profUcc.encoderSignatureDocuments(this.choixMob);

    Map<DocumentDto, Boolean> map = this.profUcc.listerEtatDocuments(this.choixMob);

    assertEquals("L'état de la mobilité doit correspondre.", Etat.A_PAYER_SOLDE,
        this.choixMob.getEtat());
    assertTrue("Le document devait être signé.", map.get(this.document));
    assertTrue("Le document devait être signé.", map.get(document2));
    assertTrue("Le document devait être signé.", map.get(document3));
  }

  @Test
  public void testEncoderSignatureDocumentsAjouterDocumentAPayer() {

    this.quidUcc.inscrire(this.user);
    this.demandeMob.setChoixMobilites(Arrays.asList(this.choixMob));
    this.profUcc.declarerChoix(this.demandeMob);
    this.profUcc.confirmerPartenaire(this.choixMob);

    DocumentDto document2 = this.factory.getDocument();
    document2.setId(2);
    DocumentDto document4 = this.factory.getDocument();
    document4.setId(4);
    this.choixMob.setDocumentsSignes(Arrays.asList(document2, document4));
    this.profUcc.encoderSignatureDocuments(this.choixMob);

    Map<DocumentDto, Boolean> map = this.profUcc.listerEtatDocuments(this.choixMob);

    assertEquals("L'état de la mobilité doit correspondre.", Etat.A_PAYER, this.choixMob.getEtat());
    assertTrue("Le document devait être signé.", map.get(this.document));
    assertTrue("Le document devait être signé.", map.get(document2));
  }

  @Test
  public void testEncoderSignatureDocumentsAjouterDocumentDemandePaiement() {

    this.quidUcc.inscrire(this.user);
    this.demandeMob.setChoixMobilites(Arrays.asList(this.choixMob));
    this.profUcc.declarerChoix(this.demandeMob);
    this.profUcc.confirmerPartenaire(this.choixMob);
    this.userInfos.setUtilisateur(this.user);
    this.profUcc.encoderInformationsPersonnelles(this.userInfos);

    DocumentDto document2 = this.factory.getDocument();
    document2.setId(2);
    this.choixMob.setDocumentsSignes(Arrays.asList(document2));
    this.choixMob.setEtat(Etat.DEMANDE_PAYEMENT);
    this.profUcc.encoderSignatureDocuments(this.choixMob);

    Map<DocumentDto, Boolean> map = this.profUcc.listerEtatDocuments(this.choixMob);

    assertEquals("L'état de la mobilité doit correspondre.", Etat.DEMANDE_PAYEMENT,
        this.choixMob.getEtat());
    assertTrue("Le document devait être signé.", map.get(this.document));
    assertTrue("Le document devait être signé.", map.get(document2));
  }

  @Test(expected = BizzException.class)
  public void testListerDemandeMobilitesParametres() {

    this.profUcc.listerDemandeMobilites(null);
  }

  @Test
  public void testListerDemandeMobilites() {

    UserDto user2 = this.factory.getUser();
    DepartementDto dep = this.factory.getDepartement();
    DemandeMobiliteDto demandeMob2 = this.factory.getDemandeMobilite();
    dep.setId(2);
    user2.setDepartement(dep);
    demandeMob2.setEtudiant(user2);

    user2.setId(2);
    user2.setNom("dsqdza");
    user2.setPrenom("Non connue");
    user2.setMail("dqs.dqs@student.vinci.be");
    user2.setPseudo("test2");

    this.quidUcc.inscrire(this.user);
    this.quidUcc.inscrire(user2);

    this.demandeMob.setChoixMobilites(Arrays.asList(this.choixMob));
    this.profUcc.declarerChoix(this.demandeMob);

    ChoixMobiliteDto choixMob2 = this.factory.getChoixMobilite();
    choixMob2.setId(2);
    demandeMob2.setChoixMobilites(Arrays.asList(choixMob2));
    this.profUcc.declarerChoix(demandeMob2);

    List<DemandeMobiliteDto> liste =
        this.profUcc.listerDemandeMobilites(Arrays.asList(this.departement, dep));

    assertEquals("La demande doit correspondre.", this.demandeMob, liste.get(0));
    assertEquals("La demande doit correspondre.", demandeMob2, liste.get(1));
    assertEquals("Le nombre de demande est incorrecte.", 2, liste.size());
  }



  @Test(expected = BizzException.class)
  public void testGererEncodageLogicielsParametres() {

    profUcc.gererEncodageLogiciels(null);
  }

  @Test
  public void testGererEncodageLogicielsConfirmer() {

    this.choixMob.setLogiciels(Arrays.asList(this.logiciel));
    this.profUcc.gererEncodageLogiciels(this.choixMob);

    Map<LogicielDto, Boolean> map = this.profUcc.listerEtatLogicielsEncodes(this.choixMob);

    assertTrue("Le logiciel doit être encodé pour la mobilité.", map.get(this.logiciel));
  }

  @Test
  public void testGererEncodageLogicielsAnnuler() {

    this.choixMob.setLogiciels(Arrays.asList(this.logiciel));
    this.profUcc.gererEncodageLogiciels(this.choixMob);

    this.profUcc.gererEncodageLogiciels(this.choixMob);

    Map<LogicielDto, Boolean> map = this.profUcc.listerEtatLogicielsEncodes(this.choixMob);

    assertFalse("Le logiciel ne doit pas être encodé pour la mobilité.", map.get(this.logiciel));
  }


  @Test(expected = BizzException.class)
  public void testListerDemandeDePayementParametres() {

    this.profUcc.listerDemandeDePayement(0);
  }

  @Test
  public void testListerDemandeDePayement() {

    UserDto user2 = this.factory.getUser();
    user2.setId(2);
    user2.setNom("dsqdza");
    user2.setPrenom("Non connue");
    user2.setMail("dqs.dqs@student.vinci.be");
    user2.setPseudo("test2");

    DemandeMobiliteDto demandeMob2 = this.factory.getDemandeMobilite();
    demandeMob2.setEtudiant(user2);
    demandeMob2.setId(2);

    ChoixMobiliteDto choixMob2 = this.factory.getChoixMobilite();
    choixMob2.setId(2);
    choixMob2.setNumCandidature(demandeMob2.getId());
    this.choixMob.setNumCandidature(this.demandeMob.getId());

    this.quidUcc.inscrire(this.user);
    this.quidUcc.inscrire(user2);
    this.demandeMob.setChoixMobilites(Arrays.asList(this.choixMob));
    demandeMob2.setChoixMobilites(Arrays.asList(choixMob2));
    this.profUcc.declarerChoix(this.demandeMob);
    this.profUcc.confirmerPartenaire(this.choixMob);

    this.profUcc.declarerChoix(demandeMob2);
    this.profUcc.confirmerPartenaire(choixMob2);

    this.choixMob.setEtat(Etat.DEMANDE_PAYEMENT);
    choixMob2.setEtat(Etat.DEMANDE_PAYEMENT_SOLDE);

    Map<ChoixMobiliteDto, Integer> map = this.profUcc.listerDemandeDePayement(2016);

    assertEquals("L'etat de la mobilité est incorrecte.", (Integer) 1, map.get(this.choixMob));
    assertEquals("L'etat de la mobilité est incorrecte.", (Integer) 2, map.get(choixMob2));
    assertEquals("Le nombre de mobilité est incorrecte.", 2, map.size());

  }

  @Test
  public void testListerMotifsAnnulations() {

    this.quidUcc.inscrire(this.user);
    this.demandeMob.setChoixMobilites(Arrays.asList(this.choixMob));
    this.profUcc.declarerChoix(this.demandeMob);

    this.user.setProf(true);
    this.annulation.setCreateur(this.user);

    this.choixMob.setMotifAnnulation(this.annulation);
    this.profUcc.annulerChoixMobilite(this.choixMob);

    List<AnnulationDto> liste = this.profUcc.listerMotifsAnnulations();

    assertEquals("L'annulation de mobilité doit correspondre.", this.annulation, liste.get(0));
    assertEquals("Le nombre d'annulation de mobilité est incorrecte.", 1, liste.size());
  }

  @Test(expected = BizzException.class)
  public void testChangerVisibiliterPartenaireParametres() {

    this.profUcc.changerVisibiliterPartenaire(null);
  }

  @Test(expected = BizzException.class)
  public void testChangerVisibiliterPartenaireDejaLier() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    profUcc.declarerChoix(demandeMob);
    profUcc.ajouterPartenaire(user, partenaire);

    profUcc.changerVisibiliterPartenaire(partenaire);
  }

  @Test(expected = BizzException.class)
  public void testChangerVisibiliterPartenaireObsoletes() {

    this.quidUcc.inscrire(this.user);
    this.demandeMob.setChoixMobilites(Arrays.asList(this.choixMob));
    this.choixMob.setPartenaire(null);
    this.profUcc.declarerChoix(this.demandeMob);
    this.profUcc.ajouterPartenaire(this.user, this.partenaire);

    PartenaireDto partenaire2 = this.factory.getPartenaire();
    this.partenaire.setNumeroVersion(1);
    partenaire2.setNumeroVersion(this.partenaire.getNumeroVersion());

    this.profUcc.changerVisibiliterPartenaire(partenaire2);
    this.profUcc.changerVisibiliterPartenaire(this.partenaire);
  }

  @Test
  public void testChangerVisibiliterPartenaireValide() {

    this.quidUcc.inscrire(this.user);
    this.demandeMob.setChoixMobilites(Arrays.asList(this.choixMob));
    this.partenaire.setVisible(false);
    this.profUcc.declarerChoix(this.demandeMob);
    this.profUcc.ajouterPartenaire(this.user, this.partenaire);

    this.profUcc.changerVisibiliterPartenaire(this.partenaire);

    assertTrue("Le partenaire doit être visible.", this.partenaire.isVisible());
  }

  @Test
  public void testListerPartenairesSelectionnablesPourTous() {

    this.quidUcc.inscrire(this.user);
    this.user.setProf(true);
    this.demandeMob.setChoixMobilites(Arrays.asList(this.choixMob));
    this.profUcc.declarerChoix(this.demandeMob);
    this.profUcc.ajouterPartenaire(this.user, this.partenaire);

    List<PartenaireDto> liste = this.profUcc.listerPartenairesSelectionnablesPourTous();

    assertEquals("Le partenaire doit être invisible.", this.partenaire, liste.get(0));
    assertEquals("Le partenaire doit être invisible.", 1, liste.size());


  }

}
