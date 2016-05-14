package domaine.bizz;

import core.AppContext;
import core.config.AppConfig;
import core.exceptions.BizzException;
import core.injecteur.InjecteurDependance;
import core.injecteur.InjecteurDependance.Injecter;
import domaine.bizz.interfaces.UserBizz;
import domaine.factory.EntiteFactory;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserTest {
  @Injecter
  private static EntiteFactory factory;
  private static InjecteurDependance injector;

  private UserBizz etud, prof;


  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    AppContext ctxt = new AppContext(AppContext.Profil.TEST, "Gerasmus");
    UserTest.injector = ctxt.initInjectorDependancySystem();
  }


  @Before
  public void setUp() throws Exception {
    UserTest.injector.injectByAnnotedField(this);

    etud = (UserBizz) UserTest.factory.getUser();
    etud.setProf(false);

    prof = (UserBizz) UserTest.factory.getUser();
    prof.setProf(true);
  }

  @Test
  public final void dummy() {
    Assert.assertTrue(true);
  }


  @Test
  public final void testGetPseudo() {
    Assert.assertEquals("MockPseudo", etud.getPseudo());
  }


  @Test(expected = BizzException.class)
  public final void testSetPseudoNull() {
    etud.setPseudo("");
  }

  @Test(expected = BizzException.class)
  public final void testSetPseudoVide() {
    etud.setPseudo("");
  }

  @Test(expected = BizzException.class)
  public final void testSetPseudoVide2() {
    etud.setPseudo(" ");
  }

  // TODO le pseudo devrai contenir des donner valide C.A.D des données comme
  // aa => ok
  // .A =>ko a => KO
  // @Test(expected = BizzException.class)
  // public final void testSetPseudoincorrect() {
  // etud.setPseudo(".?");
  // }

  @Test
  public final void testSetPseudo() {
    etud.setPseudo("test");
    Assert.assertEquals("test", etud.getPseudo());
  }


  @Test
  public final void testGetNom() {
    Assert.assertEquals("Mocky", etud.getNom());
  }


  @Test(expected = BizzException.class)
  public final void testSetNomNull() {
    etud.setNom("");
  }

  @Test(expected = BizzException.class)
  public final void testSetNomVide() {
    etud.setNom("");
  }

  @Test(expected = BizzException.class)
  public final void testSetNomVide2() {
    etud.setNom(" ");
  }

  @Test
  public final void testSetNom() {
    etud.setNom("test");
    Assert.assertEquals("test", etud.getNom());
  }

  @Test
  public final void testGetPrenom() {
    Assert.assertEquals("John", etud.getPrenom());
  }

  @Test(expected = BizzException.class)
  public final void testSetPrenomNull() {
    etud.setPrenom("");
  }

  @Test(expected = BizzException.class)
  public final void testSetPrenomVide() {
    etud.setPrenom("");
  }

  @Test(expected = BizzException.class)
  public final void testSetPrenomVide2() {
    etud.setPrenom(" ");
  }

  @Test
  public final void testSetPrenom() {
    etud.setPrenom("test");
    Assert.assertEquals("test", etud.getPrenom());
  }



  @Test
  public final void testGetMdp() {
    Assert.assertEquals("Abc#123", etud.getMdp());
  }


  @Test(expected = BizzException.class)
  public final void testSetMdpNull() {
    etud.setMdp("");
  }

  @Test(expected = BizzException.class)
  public final void testSetMdpVide() {
    etud.setMdp(" ");
  }

  @Test(expected = BizzException.class)
  public final void testSetMdpVide2() {
    etud.setMdp("");
  }

  @Test
  public final void testSetMdp() {
    etud.setMdp("test");
    Assert.assertEquals("test", etud.getMdp());
  }

  @Test
  public final void testGetDateInscription() {
    Assert.assertTrue(LocalDate.of(2016, 02, 10).isEqual(etud.getDateInscription()));
  }


  @Test(expected = BizzException.class)
  public final void testSetDateInscriptionNull() {
    etud.setDateInscription(null);
  }

  @Test
  public final void testSetDateInscription() {
    etud.setDateInscription(LocalDate.now());
    Assert.assertTrue(LocalDate.now().isEqual(etud.getDateInscription()));
  }

  @Test
  public final void testGetEstProf() {
    Assert.assertEquals(false, etud.isProf());
  }



  @Test
  public final void testSetTypeEtud() {
    etud.setProf(false);
    Assert.assertEquals(false, etud.isProf());
  }

  @Test
  public final void testSetTypeProf() {
    etud.setProf(true);
    Assert.assertEquals(true, etud.isProf());
  }


  @Test
  public final void testGetMail() {
    Assert.assertEquals("john.mocky@student.vinci.be", etud.getMail());
  }

  @Test(expected = BizzException.class)
  public final void testSetMailNull() {
    etud.setMail("");
  }

  @Test(expected = BizzException.class)
  public final void testSetMailVide() {
    etud.setMail("");
  }

  @Test(expected = BizzException.class)
  public final void testSetMailVide2() {
    etud.setMail(" ");
  }

  @Test
  public final void testSetMail() {
    etud.setMail("test@mail.dom");
    Assert.assertEquals("test@mail.dom", etud.getMail());
  }

  @Test
  public final void testEstPasProf() {
    Assert.assertFalse(etud.isProf());
  }

  @Test
  public final void testDeterminerTypeNull() {
    etud.setMail(AppConfig.getValueOf("mail_etud_user"));
    etud.determinerType();
  }

  @Test
  public final void testDeterminerTypeProf() {
    prof.setMail(AppConfig.getValueOf("mail_prof_user"));
    prof.determinerType();
  }


  @Test
  public final void testDeterminerTypeEtud() {
    etud.setMail(AppConfig.getValueOf("mail_etud_user"));
    etud.determinerType();
  }


  @Test
  public final void testGetDepartement() {
    prof.setDepartement(UserTest.factory.getDepartement());
    Assert.assertNotNull(prof.getDepartement());
  }



  @Test
  public final void testGetNumVersion() {
    Assert.assertEquals(1, etud.getNumeroVersion(), 0);
  }

  @Test(expected = BizzException.class)
  public final void testSetNumVersion() {
    etud.setNumeroVersion(0);
  }

  public final void testSetNumVersion2() {
    etud.setNumeroVersion(12);
    Assert.assertEquals(12, etud.getNumeroVersion(), 0);
  }


  public final void testIncNumVersion() {
    etud.setNumeroVersion(5);
    etud.incNumeroVersion();
    Assert.assertEquals(6, etud.getNumeroVersion(), 0);

    etud.setNumeroVersion(Integer.MAX_VALUE - 1);
    etud.incNumeroVersion();
    Assert.assertEquals(1, etud.getNumeroVersion(), 0);
  }



  @Test
  public final void testEquals() {
    Assert.assertFalse(etud.equals(null));
  }



  @Test
  public final void testEquals2() {
    Assert.assertTrue(etud.equals(UserTest.factory.getUser()));
  }

  @Test
  public final void testEquals3() {
    prof.setId(13);
    Assert.assertFalse(etud.equals(prof));
  }


  @Test
  public final void testEquals4() {
    Assert.assertFalse(etud.equals(UserTest.factory.getDepartement()));
  }

  @Test
  public final void testToString() {
    Assert.assertNotNull(etud.toString());
  }



}
