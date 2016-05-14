package domaine.bizz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import core.AppContext;
import core.exceptions.BizzException;
import core.injecteur.InjecteurDependance;
import core.injecteur.InjecteurDependance.Injecter;
import domaine.bizz.interfaces.DocumentBizz;
import domaine.dto.DocumentDto.Genre;
import domaine.factory.EntiteFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DocumentTest {

  @Injecter
  private static EntiteFactory factory;
  private static InjecteurDependance injector;

  // Si Y'as des methode de bizz dans la classe d'entite, choisir le ***Bizz à la place.
  private DocumentBizz doc;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    AppContext ctxt = new AppContext(AppContext.Profil.TEST, "Gerasmus");
    injector = ctxt.initInjectorDependancySystem();
  }

  @Before
  public void setUp() throws Exception {
    injector.injectByAnnotedField(this);
    doc = (DocumentBizz) factory.getDocument();
    doc.setGenre(Genre.DEPART);
    doc.setNom("MockDoc");
    doc.setProgramme(Programme.class.newInstance());
    doc.setTypeProgramme(TypeProgramme.class.newInstance());
    doc.setMobilite(ChoixMobilite.class.newInstance());
  }

  @Test
  public void testGetGenre() {
    assertNotNull(doc.getGenre());
    assertEquals(Genre.DEPART, doc.getGenre());
  }

  @Test
  public void testGetNom() {
    assertNotNull(doc.getNom());
    assertEquals("MockDoc", doc.getNom());
  }

  @Test
  public void testGetProgramme() {
    assertNotNull(doc.getProgramme());
    try {
      assertEquals(Programme.class.newInstance(), doc.getProgramme());
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetTypeProgramme() {
    assertNotNull(doc.getTypeProgramme());
    try {
      assertEquals(TypeProgramme.class.newInstance(), doc.getTypeProgramme());
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSetGenreGenre() {
    doc.setGenre(Genre.RETOUR);
    assertEquals(Genre.RETOUR, doc.getGenre());
  }

  @Test
  public void testSetGenreString() {
    doc.setGenre("DEPART");
    assertEquals(Genre.DEPART, doc.getGenre());
  }

  @Test(expected = BizzException.class)
  public void testSetGenreStringNull() {
    doc.setGenre("");
  }

  @Test
  public void testSetNom() {
    doc.setNom("Mock");
    assertEquals("Mock", doc.getNom());
  }

  @Test(expected = BizzException.class)
  public void testSetNomNull() {
    doc.setNom("");
  }

  @Test(expected = BizzException.class)
  public void testSetProgramme() {
    doc.setProgramme(null);
  }

  @Test(expected = BizzException.class)
  public void testSetTypeProgramme() {
    doc.setTypeProgramme(null);
  }

  @Test
  public void testGetMobilite() {
    assertNotNull(doc.getMobilite());
    try {
      assertEquals(ChoixMobilite.class.newInstance(), doc.getMobilite());
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Test(expected = BizzException.class)
  public void testSetMobilite() {
    doc.setMobilite(null);
  }

}
