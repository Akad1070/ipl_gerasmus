package domaine.bizz;

import core.AppContext;
import core.config.AppConfig;
import core.injecteur.InjecteurDependance;
import core.injecteur.InjecteurDependance.Injecter;
import domaine.dto.AnnulationDto;
import domaine.factory.EntiteFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AnnulationTest {

  @Injecter
  private static EntiteFactory factory;
  private static InjecteurDependance injector;

  // Si Y'as des methode de bizz dans la classe d'entite, choisir le ***Bizz à la place.
  private AnnulationDto annulation, annulationCree;



  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    AppContext ctxt = new AppContext(AppContext.Profil.TEST, "Gerasmus");
    injector = ctxt.initInjectorDependancySystem();
  }

  @Before
  public void setUp() throws Exception {
    injector.injectByAnnotedField(this);
    annulation = factory.getAnnulation();
    annulationCree = factory.getAnnulation();
    annulationCree.setCreateur(factory.getUser());
  }

  @Test
  public void testGetMotif() {
    Assert.assertNotNull(annulation.getMotif());
    Assert.assertEquals(AppConfig.getValueOf("motif_annul"), annulation.getMotif());
  }


  @Test
  public void testGetCreateur() {
    Assert.assertNotNull(annulation.getCreateur());
    Assert.assertNotNull(annulationCree.getCreateur());
    Assert.assertEquals(AnnulationTest.factory.getUser(), annulationCree.getCreateur());
  }

  @Test
  public void testSetMotif() {
    annulation.setMotif("Test");
    Assert.assertEquals("Test", annulation.getMotif());
  }

  @Test
  public void testSetCreateur() {
    AnnulationDto annul2 = AnnulationTest.factory.getAnnulation();
    annul2.setCreateur(AnnulationTest.factory.getUser());


    Assert.assertNotNull(annul2.getCreateur());
    Assert.assertEquals(annulationCree.getCreateur(), annul2.getCreateur());
  }

}
