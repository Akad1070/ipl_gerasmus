package domaine.ucc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import core.AppContext;
import core.config.AppConfig;
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
import domaine.dto.PaysDto;
import domaine.dto.ProgrammeDto;
import domaine.dto.TypeProgrammeDto;
import domaine.dto.UserDto;
import domaine.dto.UserInfoDto;
import domaine.factory.EntiteFactory;
import domaine.ucc.interfaces.EtudiantUcc;
import domaine.ucc.interfaces.ProfesseurUcc;
import domaine.ucc.interfaces.QuidamUcc;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EtudiantUccTest {

  private static AppContext ct;

  @Injecter
  private EntiteFactory factory;

  @Injecter
  private EtudiantUcc etudUcc;
  @Injecter
  private QuidamUcc quidUcc;
  @Injecter
  private ProfesseurUcc profUcc;

  private DemandeMobiliteDto demandeMob;
  private ChoixMobiliteDto choixMob;
  private UserDto user;
  private PartenaireDto partenaire;
  private UserInfoDto userInfos;
  private DepartementDto departement;
  private AnnulationDto annulation;
  private DocumentDto document;

  @BeforeClass
  public static void setUpBeforeClass() {
    ct = new AppContext(AppContext.Profil.TEST, "Gerasmus");

  }

  @Before
  public void setUp() {
    ct.getInjector().removeFromCache(DaoFactoryImpl.class);
    ct.getInjector().removeFromCache(EtudiantUccImpl.class);
    ct.getInjector().removeFromCache(ProfesseurUccImpl.class);
    ct.getInjector().removeFromCache(QuidamUccImpl.class);
    ct.getInjector().injectByAnnotedField(this);
    demandeMob = factory.getDemandeMobilite();
    choixMob = factory.getChoixMobilite();
    user = factory.getUser();
    partenaire = factory.getPartenaire();
    userInfos = factory.getUserInfo();
    departement = factory.getDepartement();
    annulation = factory.getAnnulation();
    document = factory.getDocument();

  }

  @Test(expected = BizzException.class)
  public void testChangerEtatParametres() {

    etudUcc.changerEtat(null);
  }

  @Test(expected = BizzException.class)
  public void testChangerEtatCompteManquant() {

    quidUcc.inscrire(user);
    choixMob.setEtat(Etat.CONFIRME);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);

    userInfos.setCompteBancaire(null);
    etudUcc.encoderInformationsPersonnelles(userInfos);

    choixMob.setEtat(Etat.DEMANDE_PAYEMENT);
    etudUcc.changerEtat(choixMob);
  }

  @Test(expected = BizzException.class)
  public void testChangerEtatMobiliteObsolete() {


    quidUcc.inscrire(user);
    choixMob.setEtat(Etat.CONFIRME);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);

    etudUcc.encoderInformationsPersonnelles(userInfos);

    choixMob.setEtat(Etat.DEMANDE_PAYEMENT);

    ChoixMobiliteDto choixMob2 = factory.getChoixMobilite();

    choixMob2.setEtat(Etat.DEMANDE_PAYEMENT_SOLDE);

    choixMob2.setNumeroVersion(1);
    choixMob.setNumeroVersion(choixMob2.getNumeroVersion());

    etudUcc.changerEtat(choixMob2);
    etudUcc.changerEtat(choixMob);

  }

  @Test
  public void testChangerEtatValide() {


    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    etudUcc.confirmerPartenaire(choixMob);
    userInfos.setUtilisateur(user);
    etudUcc.encoderInformationsPersonnelles(userInfos);

    ChoixMobiliteDto choixMob2 = factory.getChoixMobilite();
    choixMob2.setEtat(Etat.A_PAYER);

    etudUcc.changerEtat(choixMob2);

    List<ChoixMobiliteDto> liste = etudUcc.recupererMobilitesPourDemande(demandeMob);

    assertEquals("L'etat de la mobilité doit correspondre.", choixMob.getEtat(),
        liste.get(0).getEtat());

  }

  @Test(expected = Exception.class)
  public void testDeclarerChoixParametres() {
    etudUcc.declarerChoix(null);

  }

  @Test(expected = BizzException.class)
  public void testDeclarerChoixDemandeExisteDeja() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    choixMob.setId(2);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);

  }

  @Test(expected = BizzException.class)
  public void testDeclarerChoixDemandeUtilisateurInexistant() {

    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);

  }

  @Test(expected = BizzException.class)
  public void testDeclarerChoixDemandeMobiliteNull() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList((ChoixMobiliteDto) null));
    etudUcc.declarerChoix(demandeMob);
  }

  @Test
  public void testDeclarerChoixDemandeInvalide() {

    quidUcc.inscrire(user);
    DemandeMobiliteDto demande = factory.getDemandeMobilite();

    try {
      demande.setChoixMobilites(Arrays.asList(choixMob));
      demande.setAnneeAcademique(0);
      etudUcc.declarerChoix(demande);
      fail("L'année académique doit être supérieur à zéro.");
    } catch (Exception e) {
      assertTrue(true);

    }
    try {
      demande = factory.getDemandeMobilite();
      demande.setChoixMobilites(Arrays.asList(choixMob));
      demande.setEtudiant(null);
      etudUcc.declarerChoix(demande);
      fail("Il doit y avoir un créateur pour la demande de mobilité.");
    } catch (Exception e) {
      assertTrue(true);

    }
  }

  @Test
  public void testDeclarerChoixDemandeValide() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);

    List<ChoixMobiliteDto> liste = etudUcc.recupererMobilitesPourDemande(demandeMob);

    assertEquals("L'etat de la mobilité doit correspondre.", choixMob.getEtat(),
        liste.get(0).getEtat());
    assertEquals("La demande doit correspondre.", choixMob.getDemande().getId(),
        liste.get(0).getDemande().getId());
  }

  @Test
  public void testAjouterPartenaireParametres() {
    try {
      etudUcc.ajouterPartenaire(null, null);
      fail("L'utilisateur et le partenaire ne peut être nulle.");
    } catch (BizzException e) {
      assertTrue(true);
    }

    try {
      etudUcc.ajouterPartenaire(null, partenaire);
      fail("L'utilisateur ne peut être nulle.");
    } catch (BizzException e) {
      assertTrue(true);
    }

    try {
      etudUcc.ajouterPartenaire(user, null);
      fail("Le partenaire ne peut être nulle.");
    } catch (BizzException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testAjouterPartenaireChampManquant() {

    quidUcc.inscrire(user);
    try {
      partenaire.setNomLegal(null);
      etudUcc.ajouterPartenaire(user, partenaire);
      fail("Le nom légale du partenaire ne doit pas être nulle.");
    } catch (BizzException e) {
      assertTrue(true);
    }

    try {
      partenaire.setNomAffaire(null);
      etudUcc.ajouterPartenaire(user, partenaire);
      fail("Le nom d'affaire du partenaire ne doit pas être nulle.");
    } catch (BizzException e) {
      assertTrue(true);
    }

    try {
      partenaire.setPays(null);
      etudUcc.ajouterPartenaire(user, partenaire);
      fail("Le nom légale du partenaire ne doit pas être nulle.");
    } catch (BizzException e) {
      assertTrue(true);
    }

    try {
      partenaire.setVille(null);
      etudUcc.ajouterPartenaire(user, partenaire);
      fail("La ville du partenaire ne doit pas être nulle.");
    } catch (BizzException e) {
      assertTrue(true);
    }

    try {
      partenaire.setMail(null);
      etudUcc.ajouterPartenaire(user, partenaire);
      fail("L'adresse mail du partenaire ne doit pas être nulle.");
    } catch (BizzException e) {
      assertTrue(true);
    }

  }

  @Test(expected = BizzException.class)
  public void testAjouterPartenaireUtilisateurInexistant() {

    etudUcc.ajouterPartenaire(user, partenaire);
  }

  @Test
  public void testAjouterPartenaireUtilisateurCorrespondant() {

    quidUcc.inscrire(user);
    partenaire.setIplDepartements(new ArrayList<DepartementDto>(Arrays.asList(departement)));
    PartenaireDto part = etudUcc.ajouterPartenaire(user, partenaire);
    assertEquals("L'utilisateur ne correspondant pas au créateur du partenaire.", user.getId(),
        part.getCreateur().getId());

  }

  @Test
  public void testAjouterPartenaireChampOptionelle() {

    UserDto userDto = factory.getUser();
    quidUcc.inscrire(userDto);

    PartenaireDto part1 = factory.getPartenaire();
    part1.setCodePostal(null);
    part1.setIplDepartements(new ArrayList<DepartementDto>(Arrays.asList(departement)));
    try {
      etudUcc.ajouterPartenaire(userDto, part1);
      assertTrue(true);
    } catch (Exception e) {
      fail("Le code postal du partenaire est un champ optionelle.");
    }

    PartenaireDto part2 = factory.getPartenaire();
    part2.setNomcomplet(null);
    part2.setIplDepartements(new ArrayList<DepartementDto>(Arrays.asList(departement)));
    try {
      etudUcc.ajouterPartenaire(userDto, part2);
      assertTrue(true);
    } catch (Exception e) {
      fail("Le nom complet du partenaire est un champ optionelle.");
    }

    PartenaireDto part3 = factory.getPartenaire();
    part3.setNbEmploye(0);
    part3.setIplDepartements(new ArrayList<DepartementDto>(Arrays.asList(departement)));
    try {
      etudUcc.ajouterPartenaire(userDto, part3);
      assertTrue(true);
    } catch (Exception e) {
      fail("Le nombre d'employé du partenaire est un champ optionelle.");
    }

  }

  @Test
  public void testAjouterPartenaireUtilisateurProfNonCorrespondant() {

    quidUcc.inscrire(user);
    user.setProf(true);
    partenaire.setIplDepartements(new ArrayList<DepartementDto>(Arrays.asList(departement)));
    PartenaireDto part = etudUcc.ajouterPartenaire(user, partenaire);
    assertNull("Le partenaire ne doit pas avoir d'utilisateur-créateur.", part.getCreateur());

  }

  @Test(expected = BizzException.class)
  public void testAnnulerChoixMobiliterParametres() {

    etudUcc.annulerChoixMobilite(null);

  }

  @Test(expected = BizzException.class)
  public void testAnnulerChoixMobiliterUtilisateurInexistant() {

    choixMob.setMotifAnnulation(annulation);
    etudUcc.annulerChoixMobilite(choixMob);

  }

  @Test(expected = BizzException.class)
  public void testAnnulerChoixMobiliterDemandeInexistante() {

    quidUcc.inscrire(user);
    choixMob.setMotifAnnulation(annulation);
    etudUcc.annulerChoixMobilite(choixMob);

  }

  @Test(expected = BizzException.class)
  public void testAnnulerChoixMobiliterUtilisateurPasCreateur() {

    UserDto user2 = factory.getUser();
    user2.setId(2);
    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    quidUcc.inscrire(user2);
    annulation.setCreateur(user2);
    choixMob.setMotifAnnulation(annulation);
    etudUcc.annulerChoixMobilite(choixMob);
  }

  @Test
  public void testAnnulerChoixMobiliterAnnulationChampVide() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);

    AnnulationDto annulation2 = factory.getAnnulation();

    try {
      annulation2.setCreateur(null);
      choixMob.setMotifAnnulation(annulation2);
      etudUcc.annulerChoixMobilite(choixMob);
      fail("Il doit y avoir un créateur pour l'annulation.");
    } catch (Exception e) {
      assertTrue(true);

    }

    try {
      annulation2 = factory.getAnnulation();
      user.setId(0);
      annulation2.setCreateur(user);
      choixMob.setMotifAnnulation(annulation2);
      etudUcc.annulerChoixMobilite(choixMob);
      fail("Il doit y avoir un créateur pour l'annulation.");
    } catch (Exception e) {
      assertTrue(true);

    }
  }

  @Test(expected = BizzException.class)
  public void testAnnulerChoixMobiliterObsolete() {

    AnnulationDto annulation2 = factory.getAnnulation();
    annulation2.setId(2);
    choixMob.setNumeroVersion(1);
    ChoixMobiliteDto choixMob2 = factory.getChoixMobilite();
    choixMob2.setNumeroVersion(choixMob.getNumeroVersion());

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    choixMob.setMotifAnnulation(annulation);
    etudUcc.annulerChoixMobilite(choixMob);
    choixMob2.setMotifAnnulation(annulation2);
    etudUcc.annulerChoixMobilite(choixMob2);
  }

  @Test
  public void testAnnulerChoixMobiliterValide() {


    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    choixMob.setMotifAnnulation(annulation);
    etudUcc.annulerChoixMobilite(choixMob);

    assertEquals("L'annulation de mobilité ne correspond pas.", annulation.getId(),
        etudUcc.recupererMobilitesPourDemande(demandeMob).get(0).getMotifAnnulation().getId());

  }

  @Test(expected = BizzException.class)
  public void testConfirmerPartenaireParametres() {

    etudUcc.confirmerPartenaire(null);
  }

  @Test(expected = BizzException.class)
  public void testConfirmerPartenaireMobiliteExistant() {

    ChoixMobiliteDto choixMob2 = factory.getChoixMobilite();
    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob, choixMob2));
    etudUcc.declarerChoix(demandeMob);

    etudUcc.confirmerPartenaire(choixMob);
    etudUcc.confirmerPartenaire(choixMob2);
  }

  @Test(expected = BizzException.class)
  public void testConfirmerPartenaireMobiliteEtaIncorrecte() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    choixMob.setEtat(Etat.A_PAYER_SOLDE);
    etudUcc.confirmerPartenaire(choixMob);
  }

  @Test
  public void testConfirmerPartenaireExistantValide() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    etudUcc.confirmerPartenaire(choixMob);

    assertEquals("L'état de la mobilité ne correspond pas.", choixMob.getEtat(),
        etudUcc.recupererMobilitesPourDemande(demandeMob).get(0).getEtat());

  }

  @Test
  public void testConfirmerPartenaireNonExistantValide() {

    quidUcc.inscrire(user);
    partenaire.setId(2);
    choixMob.setPartenaire(partenaire);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    etudUcc.confirmerPartenaire(choixMob);

    assertEquals("L'état de la mobilité ne correspond pas.", choixMob.getEtat(),
        etudUcc.recupererMobilitesPourDemande(demandeMob).get(0).getEtat());

    assertEquals("Le partenaire de la mobilité ne correspond pas.", partenaire.getId(),
        etudUcc.recupererPartenaireParId(partenaire).getId());

  }

  @Test(expected = BizzException.class)
  public void testListerMobilitesPourEtudParametres() {

    etudUcc.listerMobilitesPourEtud(null);
  }

  @Test
  public void testListerMobilitesPourEtudValide() {

    quidUcc.inscrire(user);
    DemandeMobiliteDto demandeMob2 = factory.getDemandeMobilite();
    ChoixMobiliteDto choixMob2 = factory.getChoixMobilite();
    choixMob2.setId(2);
    demandeMob2.setId(2);
    demandeMob2.setAnneeAcademique(2016);
    demandeMob2.setChoixMobilites(Arrays.asList(choixMob2));
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    etudUcc.declarerChoix(demandeMob2);
    List<DemandeMobiliteDto> liste = etudUcc.listerMobilitesPourEtud(user);

    assertEquals("La demande de mobilité ne correspond pas.", demandeMob.getId(),
        liste.get(0).getId());
    assertEquals("La demande de mobilité ne correspond pas.", demandeMob2.getId(),
        liste.get(1).getId());
    assertEquals("Le nombre de demande de mobilité ne correspond pas.", 2, liste.size());
  }

  @Test(expected = BizzException.class)
  public void testListerPartenairesPourEtudiant() {

    etudUcc.listerPartenairesPourEtudiant(null);
  }

  @Test
  public void testListerPartenairesPourEtudiantValide() {

    quidUcc.inscrire(user);
    etudUcc.ajouterPartenaire(user, partenaire);
    List<PartenaireDto> liste = etudUcc.listerPartenairesPourEtudiant(user);

    assertEquals("Le partenaire ne correspond pas.", 1, liste.get(0).getId());
    assertEquals("Le nombre de partenaire est incorrecte.", 1, liste.size());
  }


  @Test(expected = BizzException.class)
  public void testEncoderInformationsPersonnellesParametres() {

    etudUcc.encoderInformationsPersonnelles(null);

  }

  @Test(expected = BizzException.class)
  public void testEncoderInformationsPersonnellesMobiliteNonConfirme() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    etudUcc.encoderInformationsPersonnelles(userInfos);

  }

  @Test
  public void testEncoderInformationsPersonnellesChampInvalide() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);

    UserInfoDto userInfo2 = factory.getUserInfo();

    try {
      userInfo2.setAdresse(null);
      etudUcc.encoderInformationsPersonnelles(userInfo2);
      fail("Il doit y avoir une adresse pour les informations personnelles de l'utilisateur.");
    } catch (Exception e) {
      assertTrue(true);
    }

    try {
      userInfo2 = factory.getUserInfo();
      userInfo2.setBanque(null);
      etudUcc.encoderInformationsPersonnelles(userInfo2);
      fail("Il doit y avoir une banque pour les informations personnelles de l'utilisateur.");
    } catch (Exception e) {
      assertTrue(true);
    }

    try {
      userInfo2 = factory.getUserInfo();
      userInfo2.setBic(null);
      etudUcc.encoderInformationsPersonnelles(userInfo2);
      fail("Il doit y avoir un bic pour les informations personnelles de l'utilisateur.");
    } catch (Exception e) {
      assertTrue(true);
    }

    try {
      userInfo2 = factory.getUserInfo();
      userInfo2.setCompteBancaire(null);
      etudUcc.encoderInformationsPersonnelles(userInfo2);
      fail(
          "Il doit y avoir un compte bancaire pour les informations personnelles de l'utilisateur.");
    } catch (Exception e) {
      assertTrue(true);
    }


    try {
      userInfo2 = factory.getUserInfo();
      userInfo2.setDateNais(null);
      etudUcc.encoderInformationsPersonnelles(userInfo2);
      fail(
          "Il doit y avoir une date de naissance pour les informations personnelles de l'utilisateur.");
    } catch (Exception e) {
      assertTrue(true);
    }

    try {
      userInfo2 = factory.getUserInfo();
      userInfo2.setEmail(null);
      etudUcc.encoderInformationsPersonnelles(userInfo2);
      fail("Il doit y avoir un mail valide pour les informations personnelles de l'utilisateur.");
    } catch (Exception e) {
      assertTrue(true);
    }

    try {
      userInfo2 = factory.getUserInfo();
      userInfo2.setNationalite(null);
      etudUcc.encoderInformationsPersonnelles(userInfo2);
      fail("Il doit y avoir une nationalité pour les informations personnelles de l'utilisateur.");
    } catch (Exception e) {
      assertTrue(true);
    }

    try {
      userInfo2 = factory.getUserInfo();
      userInfo2.setTel(null);
      etudUcc.encoderInformationsPersonnelles(userInfo2);
      fail(
          "Il doit y avoir un numéro de téléphone pour les informations personnelles de l'utilisateur.");
    } catch (Exception e) {
      assertTrue(true);
    }
    try {
      userInfo2 = factory.getUserInfo();
      userInfo2.setTitulaire(null);
      etudUcc.encoderInformationsPersonnelles(userInfo2);
      fail("Il doit y avoir un titulaire pour les informations personnelles de l'utilisateur.");
    } catch (Exception e) {
      assertTrue(true);
    }

  }

  @Test
  public void testEncoderInformationsPersonnellesValideInsert() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    etudUcc.confirmerPartenaire(choixMob);
    userInfos.setUtilisateur(user);
    etudUcc.encoderInformationsPersonnelles(userInfos);
    assertEquals("Infos utilisateurs ne correspondent pas.", userInfos.getId(),
        etudUcc.recupererUserInfoParUId(user).getId());
  }

  @Test
  public void testEncoderInformationsPersonnellesValideUpdate() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    etudUcc.confirmerPartenaire(choixMob);

    userInfos.setNumeroVersion(1);
    userInfos.setUtilisateur(user);
    etudUcc.encoderInformationsPersonnelles(userInfos);

    UserInfoDto userInfos2 = factory.getUserInfo();
    userInfos2.setNumeroVersion(userInfos.getNumeroVersion());
    userInfos2.setUtilisateur(userInfos.getUtilisateur());
    userInfos2.setAdresse("Nulle part");

    etudUcc.encoderInformationsPersonnelles(userInfos2);

    assertEquals("Infos utilisateurs ne correspondent pas.", userInfos2.getId(),
        etudUcc.recupererUserInfoParUId(user).getId());

    assertEquals("L'adresse de l'utilisateur ne correspond pas.", userInfos2.getAdresse(),
        etudUcc.recupererUserInfoParUId(user).getAdresse());

  }

  @Test(expected = BizzException.class)
  public void testEncoderInformationsPersonnellesDonneesObsoletes() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    etudUcc.confirmerPartenaire(choixMob);

    userInfos.setNumeroVersion(1);
    etudUcc.encoderInformationsPersonnelles(userInfos);

    UserInfoDto userInfos2 = factory.getUserInfo();
    userInfos2.setNumeroVersion(userInfos.getNumeroVersion());

    userInfos2.setAdresse("Nulle part");

    etudUcc.encoderInformationsPersonnelles(userInfos2);

  }

  @Test(expected = BizzException.class)
  public void testRechercherMobiliteParametres() {

    etudUcc.rechercherMobilite(null);
  }

  @Test
  public void testRechercherMobiliteResultat1() {

    quidUcc.inscrire(user);
    ChoixMobiliteDto choixMob2 = factory.getChoixMobilite();
    choixMob2.setId(2);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob, choixMob2));
    etudUcc.declarerChoix(demandeMob);

    List<ChoixMobiliteDto> liste = etudUcc.rechercherMobilite(choixMob);

    assertEquals("Les choix de mobilités ne correspondent pas.", choixMob.getId(),
        liste.get(0).getId());
    assertEquals("Les choix de mobilités ne correspondent pas.", choixMob2.getId(),
        liste.get(1).getId());
    assertEquals("Nombre de choix de mobilités trouvés incorrectes", 2, liste.size());

  }

  @Test
  public void testRechercherMobiliteResultat2() {

    quidUcc.inscrire(user);
    ChoixMobiliteDto choixMob2 = factory.getChoixMobilite();
    choixMob2.setId(2);

    ChoixMobiliteDto choixMob3 = factory.getChoixMobilite();
    choixMob3.setId(3);

    demandeMob.setChoixMobilites(Arrays.asList(choixMob, choixMob2, choixMob3));
    etudUcc.declarerChoix(demandeMob);

    choixMob3.setEtat(Etat.A_PAYER);

    List<ChoixMobiliteDto> liste = etudUcc.rechercherMobilite(choixMob3);

    assertEquals("Les choix de mobilités ne correspondent pas.", choixMob3.getId(),
        liste.get(0).getId());
    assertEquals("Nombre de choix de mobilités trouvés incorrectes", 1, liste.size());

  }

  @Test
  public void testRechercherMobiliteResultat3() {

    quidUcc.inscrire(user);
    ChoixMobiliteDto choixMob2 = factory.getChoixMobilite();
    choixMob2.setId(2);

    ChoixMobiliteDto choixMob3 = factory.getChoixMobilite();
    choixMob3.setId(3);

    demandeMob.setChoixMobilites(Arrays.asList(choixMob, choixMob2));
    etudUcc.declarerChoix(demandeMob);

    choixMob3.setEtat(Etat.A_PAYER);

    List<ChoixMobiliteDto> liste = etudUcc.rechercherMobilite(choixMob3);

    assertNull("Le nombre de mobilités trouvées doit être nulle.", liste);

  }

  @Test(expected = BizzException.class)
  public void testRecupererMobilitesPourDemandeParametres() {
    etudUcc.recupererMobilitesPourDemande(null);

  }

  @Test
  public void testRecupererMobilitesPourDemande() {

    quidUcc.inscrire(user);
    ChoixMobiliteDto choixMob2 = factory.getChoixMobilite();
    choixMob2.setId(2);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob, choixMob2));
    etudUcc.declarerChoix(demandeMob);
    List<ChoixMobiliteDto> liste = etudUcc.recupererMobilitesPourDemande(demandeMob);

    assertEquals("Le choix de mobilité ne correspond pas.", choixMob.getId(), liste.get(0).getId());
    assertEquals("Le choix de mobilité ne correspond pas.", choixMob2.getId(),
        liste.get(1).getId());
    assertEquals("Le nombre de mobilités ne correspond pas.", 2, liste.size());

  }

  @Test(expected = BizzException.class)
  public void testRechercherUserParametres() {
    etudUcc.rechercherUser(null);

  }

  @Test
  public void testRechercherUserCorrespondant1() {

    user.setNom("Connue");
    user.setPrenom("Non connue");
    UserDto user2 = factory.getUser();
    user2.setId(2);
    user2.setNom("Connue");
    user2.setPrenom("Non connue");
    user2.setMail("dqs.dqs@student.vinci.be");
    user2.setPseudo("test2");

    UserDto user3 = factory.getUser();
    user3.setId(3);
    user3.setNom("Inconnue");
    user3.setMail("dqs.ddee@student.vinci.be");
    user3.setPseudo("test3");

    quidUcc.inscrire(user);
    quidUcc.inscrire(user2);
    quidUcc.inscrire(user3);

    List<UserDto> liste = etudUcc.rechercherUser(user);

    assertEquals("Le nom des utilisateurs ne correspondent pas.", user.getNom(),
        liste.get(0).getNom());
    assertEquals("Le prenom des utilisateurs ne correspondent pas.", user.getPrenom(),
        liste.get(0).getPrenom());
    assertEquals("Le nom des utilisateurs ne correspondent pas.", user.getNom(),
        liste.get(1).getNom());
    assertEquals("Le prenom des utilisateurs ne correspondent pas.", user.getPrenom(),
        liste.get(1).getPrenom());
    assertEquals("Nombre d'utilisateurs trouvés incorrectes", 2, liste.size());
  }

  @Test
  public void testRechercherUserCorrespondant2() {

    user.setNom("Connue");
    user.setPrenom("dqsdq");
    UserDto user2 = factory.getUser();
    user2.setId(2);
    user2.setNom("Inconnue");
    user2.setPrenom("Non connue");
    user2.setMail("dqs.dqs@student.vinci.be");
    user2.setPseudo("test2");

    UserDto user3 = factory.getUser();
    user3.setId(3);
    user3.setNom("Connue");
    user3.setPrenom("fgdfgd");
    user3.setMail("dqs.ddee@student.vinci.be");
    user3.setPseudo("test3");

    quidUcc.inscrire(user);
    quidUcc.inscrire(user2);
    quidUcc.inscrire(user3);

    List<UserDto> liste = etudUcc.rechercherUser(user);

    assertEquals("Le nom des utilisateurs ne correspondent pas.", user.getNom(),
        liste.get(1).getNom());
    assertEquals("Nombre d'utilisateurs trouvés incorrectes", 2, liste.size());
  }

  @Test
  public void testRechercherUserCorrespondant3() {

    user.setNom("dqsdqs");
    user.setPrenom("Connue");
    UserDto user2 = factory.getUser();
    user2.setId(2);
    user2.setNom("Connue");
    user2.setPrenom("Non connue");
    user2.setMail("dqs.dqs@student.vinci.be");
    user2.setPseudo("test2");

    UserDto user3 = factory.getUser();
    user3.setId(3);
    user3.setNom("Connue");
    user3.setPrenom("dsqdq");
    user3.setMail("dqs.ddee@student.vinci.be");
    user3.setPseudo("test3");

    quidUcc.inscrire(user);
    quidUcc.inscrire(user2);
    quidUcc.inscrire(user3);

    List<UserDto> liste = etudUcc.rechercherUser(user);

    assertEquals("Le prenom des utilisateurs ne correspondent pas.", user.getPrenom(),
        liste.get(0).getPrenom());
    assertEquals("Nombre d'utilisateurs trouvés incorrectes", 1, liste.size());
  }

  @Test
  public void testRechercherUserResultatVide() {

    user.setNom("Connue");
    user.setPrenom("dsqdqsd");
    UserDto user2 = factory.getUser();
    user2.setId(2);
    user2.setNom("dsqdza");
    user2.setPrenom("Non connue");
    user2.setMail("dqs.dqs@student.vinci.be");
    user2.setPseudo("test2");

    UserDto user3 = factory.getUser();
    user3.setId(3);
    user3.setNom("dsqlkdzoa");
    user3.setPrenom("dqsdqsd");
    user3.setMail("dqs.ddee@student.vinci.be");
    user3.setPseudo("test3");

    quidUcc.inscrire(user2);
    quidUcc.inscrire(user3);

    List<UserDto> liste = etudUcc.rechercherUser(user);

    assertNull("Nombre d'utilisateurs trouvés doit être null", liste);
  }

  @Test(expected = BizzException.class)
  public void testRecupererUserParIdParametres() {
    etudUcc.recupererUserParId(null);
  }

  @Test
  public void testRecupererUserParIdCorrespondant() {
    UserDto user2 = factory.getUser();
    user2.setId(2);
    user2.setPseudo("dsqdqs");
    user2.setMail("dsq.dqsd@student.vinci.be");
    quidUcc.inscrire(user);
    quidUcc.inscrire(user2);

    assertEquals("Ces utilisateurs doivent correspondre.", user.getId(),
        etudUcc.recupererUserParId(user).getId());
    assertEquals("Ces utilisateurs doivent correspondre.", user2.getId(),
        etudUcc.recupererUserParId(user2).getId());
    assertNotEquals("Ces utilisateurs ne doivent pas corresponddre.", user.getId(),
        etudUcc.recupererUserParId(user2).getId());

  }

  @Test(expected = BizzException.class)
  public void testRecupererUserParIdInvalide() {

    etudUcc.recupererUserParId(user).getId();

  }

  @Test(expected = BizzException.class)
  public void testRecupererUserInfoParUIdParametres1() {

    etudUcc.recupererUserInfoParUId(null);
  }

  @Test
  public void testRecupererUserInfoParUIdInfosExiste() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    etudUcc.confirmerPartenaire(choixMob);

    userInfos.setUtilisateur(user);
    etudUcc.encoderInformationsPersonnelles(userInfos);
    assertEquals("Infos utilisateurs non correspondant.", userInfos.getId(),
        etudUcc.recupererUserInfoParUId(user).getId());

  }

  @Test
  public void testRecupererUserInfoParUIdInfosExistePas() {

    assertNull("Infos utilisateurs non existant.", etudUcc.recupererUserInfoParUId(user));

  }

  @Test
  public void testRecupererUserInfoParUId_ModifInfosCorrespondant() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);
    etudUcc.confirmerPartenaire(choixMob);
    userInfos.setUtilisateur(user);
    etudUcc.encoderInformationsPersonnelles(userInfos);
    userInfos.setAdresse("qsdqs dsqdq");
    etudUcc.encoderInformationsPersonnelles(userInfos);

    assertEquals("Infos utilisateurs non correspondant.", userInfos.getAdresse(),
        etudUcc.recupererUserInfoParUId(user).getAdresse());

  }

  @Test(expected = BizzException.class)
  public void testRecupererPartenaireParIdParametres() {

    etudUcc.recupererPartenaireParId(null);
  }

  @Test
  public void testRecupererPartenaireParIdCorrespondant() {

    partenaire.setIplDepartements(Arrays.asList(departement));
    quidUcc.inscrire(user);
    etudUcc.ajouterPartenaire(user, partenaire);
    assertEquals("Les partenaires doivent correspondre.", partenaire.getId(),
        etudUcc.recupererPartenaireParId(partenaire).getId());
  }

  @Test(expected = BizzException.class)
  public void testRechercherPartenaireParametres() {

    etudUcc.rechercherPartenaire(null);
  }

  @Test
  public void testRechercherPartenaireCorrespondant() {

    partenaire.setIplDepartements(new ArrayList<DepartementDto>(Arrays.asList(departement)));
    quidUcc.inscrire(user);
    etudUcc.ajouterPartenaire(user, partenaire);
    partenaire.setNomLegal("Entreprise");

    PartenaireDto part1 = factory.getPartenaire();
    part1.setIplDepartements(new ArrayList<DepartementDto>(Arrays.asList(departement)));

    UserDto user2 = factory.getUser();
    user2.setId(2);
    user2.setMail("dqs.dqs@student.vinci.be");
    user2.setPseudo("test2");

    quidUcc.inscrire(user2);
    etudUcc.ajouterPartenaire(user2, part1);


    PartenaireDto part2 = factory.getPartenaire();
    part2.setIplDepartements(new ArrayList<DepartementDto>(Arrays.asList(departement)));
    part2.setNomLegal("Entreprise");
    UserDto user3 = factory.getUser();
    user3.setId(3);
    user3.setMail("zezr.dqs@student.vinci.be");
    user3.setPseudo("test3");
    quidUcc.inscrire(user3);

    etudUcc.ajouterPartenaire(user3, part2);

    List<PartenaireDto> liste = etudUcc.rechercherPartenaire(partenaire);

    assertEquals("Les partenaires recherchés doivent correspondre.", partenaire.getNomLegal(),
        liste.get(2).getNomLegal());

  }

  @Test
  public void testRechercherPartenaireResultatVide() {

    partenaire.setIplDepartements(new ArrayList<DepartementDto>(Arrays.asList(departement)));
    quidUcc.inscrire(user);
    partenaire.setNomLegal("Entreprise");
    partenaire.setVille("Nulle part");
    PaysDto pays = factory.getPays();
    pays.setNom("Nulle part");
    partenaire.setPays(pays);

    PartenaireDto part1 = factory.getPartenaire();
    part1.setIplDepartements(new ArrayList<DepartementDto>(Arrays.asList(departement)));

    UserDto user2 = factory.getUser();
    user2.setId(2);
    user2.setMail("dqs.dqs@student.vinci.be");
    user2.setPseudo("test2");

    quidUcc.inscrire(user2);
    etudUcc.ajouterPartenaire(user2, part1);


    List<PartenaireDto> liste = etudUcc.rechercherPartenaire(partenaire);

    assertNull("Il ne devrait y avoir aucun partenaire de trouvé.", liste);
  }

  @Test
  public void testListerDepartementsCorrespondant() {

    List<DepartementDto> liste = etudUcc.listerDepartements();

    assertEquals("Les départements devraient être identique.", AppConfig.getValueOf("code_dep"),
        liste.get(0).getCode());
    assertEquals("Les départements devraient être identique.", AppConfig.getValueOf("code2_dep"),
        liste.get(1).getCode());
    assertEquals("Les départements devraient être identique.", AppConfig.getValueOf("code3_dep"),
        liste.get(2).getCode());
    assertEquals("Les départements devraient être identique.", AppConfig.getValueOf("code4_dep"),
        liste.get(3).getCode());
    assertEquals("Il devrait y avoir 4 departement enregistrés.", 4, liste.size());

  }

  @Test
  public void testListerPays() {

    List<PaysDto> liste = etudUcc.listerPays();

    assertEquals("Les pays devraient être identique.", AppConfig.getValueOf("code3_pays"),
        liste.get(0).getCode());
    assertEquals("Les pays devraient être identique.", AppConfig.getValueOf("code_pays"),
        liste.get(1).getCode());
    assertEquals("Les pays devraient être identique.", AppConfig.getValueOf("code2_pays"),
        liste.get(2).getCode());
    assertEquals("Les pays devraient être identique.", AppConfig.getValueOf("code4_pays"),
        liste.get(3).getCode());
    assertEquals("Il devrait y avoir 4 pays enregistrés.", 4, liste.size());
  }

  @Test
  public void testListerProgrammes() {

    List<ProgrammeDto> liste = etudUcc.listerProgrammes();

    assertEquals("Les programmes devraient être identique.", "HTTP", liste.get(0).getNom());
    assertEquals("Les programmes devraient être identique.", "FTP", liste.get(1).getNom());
    assertEquals("Les programmes devraient être identique.", "SMTP", liste.get(2).getNom());
    assertEquals("Les programmes devraient être identique.", "TELENET", liste.get(3).getNom());
    assertEquals("Il devrait y avoir 4 programmes enregistrés.", 4, liste.size());
  }

  @Test
  public void testListerTypesProgramme() {

    List<TypeProgrammeDto> liste = etudUcc.listerTypesProgramme();

    assertEquals("Les types de programme devraient être identique.",
        AppConfig.getValueOf("nom_type_prog"), liste.get(0).getNom());
    assertEquals("Les types de programme devraient être identique.",
        AppConfig.getValueOf("nom2_type_prog"), liste.get(1).getNom());
    assertEquals("Les types de programme devraient être identique.",
        AppConfig.getValueOf("nom3_type_prog"), liste.get(2).getNom());
    assertEquals("Les types de programme devraient être identique.",
        AppConfig.getValueOf("nom4_type_prog"), liste.get(3).getNom());
    assertEquals("Il devrait y avoir 4 type de programme enregistrés.", 4, liste.size());
  }

  @Test
  public void testListerAnneesAcademiqueIntroduites() {

    quidUcc.inscrire(user);
    demandeMob.setChoixMobilites(Arrays.asList(choixMob));
    etudUcc.declarerChoix(demandeMob);

    DemandeMobiliteDto dem = factory.getDemandeMobilite();
    dem.setId(2);
    UserDto user2 = factory.getUser();
    user2.setId(2);
    user2.setMail("dqs.dqs@student.vinci.be");
    user2.setPseudo("test2");
    quidUcc.inscrire(user2);
    dem.setEtudiant(user2);
    dem.setChoixMobilites(Arrays.asList(choixMob));
    dem.setAnneeAcademique(2016);
    etudUcc.declarerChoix(dem);

    List<Integer> liste = etudUcc.listerAnneesAcademiqueDejaIntroduites();

    assertEquals("L'année académique devrait être identique.", (Integer) 2015, liste.get(0));
    assertEquals("L'année académique devrait être identique.", (Integer) 2016, liste.get(1));
    assertEquals("Il devrait y avoir 2 demande de mobilité enregistré.", 2, liste.size());
  }

  @Test
  public void testListerAnneesAcademiqueIntroduitesResultatVide() {
    List<Integer> liste = etudUcc.listerAnneesAcademiqueDejaIntroduites();

    assertNull("Il ne devrait pas y avoir de demande de mobilité introduite.", liste);
  }


  @Test(expected = BizzException.class)
  public void testListerEtatDocumentsParametres() {

    etudUcc.listerEtatDocuments(null);
  }

  @Test
  public void testListerEtatDocuments() {

    DocumentDto document2 = factory.getDocument();
    document2.setId(2);
    Map<DocumentDto, Boolean> liste = etudUcc.listerEtatDocuments(choixMob);
    assertTrue("Ce document devrait être rempli.", liste.get(document));
    assertFalse("Ce document ne devrait pas être rempli.", liste.get(document2));

  }

  @Test(expected = BizzException.class)
  public void testListerEtatLogicielsEncodesParametres() {

    etudUcc.listerEtatLogicielsEncodes(null);
  }

  @Test
  public void testListerEtatLogicielsEncodes() {

    LogicielDto logiciel1 = factory.getLogiciel();
    LogicielDto logiciel2 = factory.getLogiciel();
    logiciel2.setId(2);

    choixMob.setLogiciels(Arrays.asList(logiciel1));
    profUcc.gererEncodageLogiciels(choixMob);
    Map<LogicielDto, Boolean> liste = etudUcc.listerEtatLogicielsEncodes(choixMob);
    assertTrue("Il devrait y avoir un logiciel encodé pour cette mobilité.", liste.get(logiciel1));
    assertFalse("Il ne devrait pas y avoir de logiciel encodé pour cette mobilité.",
        liste.get(logiciel2));
  }
}
