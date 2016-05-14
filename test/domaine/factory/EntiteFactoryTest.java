package domaine.factory;

import core.AppContext;
import core.exceptions.BizzException;
import core.exceptions.FatalException;
import core.injecteur.InjecteurDependance;
import core.injecteur.InjecteurDependance.Injecter;
import domaine.bizz.Departement;
import domaine.bizz.Partenaire;
import domaine.bizz.User;
import domaine.dto.DepartementDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EntiteFactoryTest {
  @Injecter
  private static EntiteFactory factory;
  private static InjecteurDependance injector;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    AppContext ctxt = new AppContext(AppContext.Profil.TEST, "Gerasmus");
    EntiteFactoryTest.injector = ctxt.initInjectorDependancySystem();
  }

  @Before
  public void setUp() throws Exception {
    EntiteFactoryTest.injector.injectByAnnotedField(this);
  }


  @Test(expected = BizzException.class)
  public final void testGetEntityNull() {
    factory.getEntite(null);
  }

  @Test(expected = FatalException.class)
  public final void testGetEntityUnknown() {
    factory.getEntite(Object.class);
  }

  @Test
  public final void testGetEntity() {
    Assert.assertSame(User.class, factory.getEntite(UserDto.class).getClass());
  }

  @Test
  public final void testGetUser() {
    Assert.assertNotNull(EntiteFactoryTest.factory.getUser());
    Assert.assertTrue(EntiteFactoryTest.factory.getUser() instanceof User);
    Assert.assertSame(User.class, EntiteFactoryTest.factory.getUser().getClass());
    UserDto dto = EntiteFactoryTest.factory.getUser();
    Assert.assertEquals(dto, EntiteFactoryTest.factory.getUser());
  }


  @Test
  public final void testGetDepartement() {
    Assert.assertNotNull(EntiteFactoryTest.factory.getDepartement());
    Assert.assertTrue(EntiteFactoryTest.factory.getDepartement() instanceof Departement);
    Assert.assertSame(Departement.class, EntiteFactoryTest.factory.getDepartement().getClass());
    DepartementDto dto = EntiteFactoryTest.factory.getDepartement();
    Assert.assertEquals(dto, EntiteFactoryTest.factory.getDepartement());
  }


  @Test
  public final void testGetMobilite() {
    // TODO
    // Assert.assertNotNull(EntiteFactoryTest.factory.getChoixMobilite());
    // Assert.assertTrue(EntiteFactoryTest.factory.getChoixMobilite() instanceof ChoixMobilite);
    // Assert.assertSame(ChoixMobilite.class,
    // EntiteFactoryTest.factory.getChoixMobilite().getClass());
    // ChoixMobiliteDto dto = EntiteFactoryTest.factory.getChoixMobilite();
    // Assert.assertEquals(dto, EntiteFactoryTest.factory.getChoixMobilite());
  }


  @Test
  public final void testGetPartenaireDto() {
    Assert.assertNotNull(EntiteFactoryTest.factory.getPartenaire());
    Assert.assertTrue(EntiteFactoryTest.factory.getPartenaire() instanceof Partenaire);
    Assert.assertSame(Partenaire.class, EntiteFactoryTest.factory.getPartenaire().getClass());
    PartenaireDto dto = EntiteFactoryTest.factory.getPartenaire();
    Assert.assertEquals(dto, EntiteFactoryTest.factory.getPartenaire());
  }

}
