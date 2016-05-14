package domaine.ucc;


import core.AppContext;
import core.exceptions.BizzException;
import core.injecteur.InjecteurDependance;
import core.injecteur.InjecteurDependance.Injecter;
import domaine.dto.UserDto;
import domaine.factory.EntiteFactory;
import domaine.ucc.interfaces.QuidamUcc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class QuidamUccTest {
  private static InjecteurDependance injector;

  @Injecter
  private static EntiteFactory factory;

  @Injecter
  private QuidamUcc ucc;

  private UserDto user;


  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    AppContext ctxt = new AppContext(AppContext.Profil.TEST, "Gerasmus");
    QuidamUccTest.injector = ctxt.initInjectorDependancySystem();
  }

  @Before
  public void setUp() throws Exception {
    QuidamUccTest.injector.injectByAnnotedField(this);
    user = QuidamUccTest.factory.getUser();
    QuidamUccTest.injector.removeFromCache(ucc.getClass());
  }


  @Test(expected = BizzException.class)
  public final void testInscrirePseudoInvalide() {
    user.setPseudo("t");
    ucc.inscrire(user);
  }


  @Test(expected = BizzException.class)
  public final void testInscrirePseudoInvalide2() {
    user.setPseudo("Ab");
    ucc.inscrire(user);
  }

  @Test(expected = BizzException.class)
  public final void testInscrirePseudoInvalide3() {
    user.setPseudo("t1");
    ucc.inscrire(user);
  }


  @Test(expected = BizzException.class)
  public final void testInscrireMdpInvalide() {
    user.setMdp("t");
    ucc.inscrire(user);
  }

  @Test(expected = BizzException.class)
  public final void testInscrireMdpInvalide2() {
    user.setMdp("xyz");
    ucc.inscrire(user);
  }


  @Test(expected = BizzException.class)
  public final void testInscrireMailInvalide() {
    user.setMail("a");
    ucc.inscrire(user);
  }

  @Test(expected = BizzException.class)
  public final void testInscrireMailInvalide2() {
    user.setMail("a@mail");
    ucc.inscrire(user);
  }


  @Test(expected = BizzException.class)
  public final void testInscrireNomInvalide() {
    user.setNom("a");
    ucc.inscrire(user);
  }

  @Test(expected = BizzException.class)
  public final void testInscrireNomInvalide2() {
    user.setNom("Ab12");
    ucc.inscrire(user);
  }

  @Test(expected = BizzException.class)
  public final void testInscrireNomInvalide3() {
    user.setNom("Test(");
    ucc.inscrire(user);
  }



  @Test(expected = BizzException.class)
  public final void testInscrirePrenomInvalide() {
    user.setPrenom("");
    ucc.inscrire(user);
  }


  @Test(expected = BizzException.class)
  public final void testInscrirePrenomInvalide2() {
    user.setPrenom("Test(");
    ucc.inscrire(user);
  }

  @Test(expected = BizzException.class)
  public final void testInscrirePrenomInvalide3() {
    user.setPrenom("Test123");
    ucc.inscrire(user);
  }


  @Test(expected = BizzException.class)
  public final void testInscrirePseudoDejaPris() {
    user.setPseudo("tmock15");
    ucc.inscrire(user);
    ucc.inscrire(user);
  }

  @Test(expected = BizzException.class)
  public final void testInscrireMailDejaPris() {
    user.setMail("test.mock@student.vinci.be");
    ucc.inscrire(user);
    ucc.inscrire(user);
  }

  @Test
  public final void testInscrireOk() {
    user.setPseudo("TMockOk");
    user.setMail("test.mock@vinci.be");
    user.setMdp("0123Xyz");
    Assert.assertEquals("Different", user, ucc.inscrire(user));
  }


  @Test(expected = BizzException.class)
  public final void testSeConnecterNull() {
    ucc.seConnecter(null);
  }


  @Test(expected = BizzException.class)
  public final void testSeConnecterPseudoInvalide() {
    user.setIdentifiant("'");
    ucc.seConnecter(user);
  }

  @Test(expected = BizzException.class)
  public final void testSeConnecterPseudoInvalide2() {
    user.setIdentifiant("Tetsuo)");
    ucc.seConnecter(user);
  }

  @Test(expected = BizzException.class)
  public final void testSeConnecterPseudoVide() {
    user.setIdentifiant("");
    ucc.seConnecter(user);
  }

  @Test(expected = BizzException.class)
  public final void testSeConnecterPseudoVide2() {
    user.setIdentifiant(" ");
    ucc.seConnecter(user);
  }


  @Test(expected = BizzException.class)
  public final void testSeConnecterMailInvalide() {
    user.setMail("'");
    ucc.seConnecter(user);
  }

  @Test(expected = BizzException.class)
  public final void testSeConnecterMailInvalide2() {
    user.setMail("t@mail.");
    ucc.seConnecter(user);
  }

  @Test(expected = BizzException.class)
  public final void testSeConnecterMailVide() {
    user.setMail("");
    ucc.seConnecter(user);
  }

  @Test(expected = BizzException.class)
  public final void testSeConnecterMailVide2() {
    user.setMail(" ");
    ucc.seConnecter(user);
  }

  @Test(expected = BizzException.class)
  public final void testSeConnecterMdpVide() {
    user.setMdp("");
    ucc.seConnecter(user);
  }

  @Test(expected = BizzException.class)
  public final void testSeConnecterMdpVide2() {
    user.setMdp(" ");
    ucc.seConnecter(user);
  }


  @Test(expected = BizzException.class)
  public final void testSeConnecterInexistant() {
    // this.user.setIdentifiant("Test1");
    // this.user.setMdp("Abc#123");
    user.setIdentifiant("");
    user.setMdp("");
    Assert.assertNull(ucc.seConnecter(user));
  }


  @Test(expected = BizzException.class)
  public final void testSeConnecterInexistant2() {
    user.setIdentifiant("Test2");
    user.setMdp("0123Xyz");
    Assert.assertNull(ucc.seConnecter(user));
  }

  @Test(expected = BizzException.class)
  public final void testSeConnecterFauxLogin() {
    user.setIdentifiant("Test31");
    user.setMail("test.mock@vinci.be");
    user.setMdp("Abc#123");

    Assert.assertEquals("Different", user, ucc.seConnecter(user));
  }

  @Test(expected = BizzException.class)
  public final void testSeConnecterFauxMail() {
    user.setMail("faux.test@mock.dom");
    user.setMdp("Abc#123");

    Assert.assertEquals("Different", user, ucc.seConnecter(user));
  }

  @Test(expected = BizzException.class)
  public final void testSeConnecterFauxMdp() {
    user.setIdentifiant("Test1");
    user.setMdp("Abc#1234");

    Assert.assertNull(ucc.seConnecter(user));
  }


//  @Test
//  public final void testSeConnecterPseudoOk() {
//    user.setIdentifiant("MockPseudo");
//    user.setMdp("Abc#123");
//
//    Assert.assertNotNull(ucc.seConnecter(user));
//    // Assert.assertNotEquals("Different", user, ucc.seConnecter(user));
//  }

  @Test
  public final void testSeConnecterMailOk() {
    user.setIdentifiant("john.mocky@student.vinci.be");
    user.setMdp("Abc#123");

    Assert.assertNotNull(ucc.seConnecter(user));
    // Assert.assertNotEquals("Different", user, ucc.seConnecter(user));
  }



}
