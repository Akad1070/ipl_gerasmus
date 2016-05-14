package domaine.bizz;

import dal.dao.core.DbEntity;
import dal.dao.core.DbEntityColumn;
import dal.dao.core.DbEntityFk;
import domaine.bizz.interfaces.DocumentBizz;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.ProgrammeDto;
import domaine.dto.TypeProgrammeDto;
import util.AppUtil;

@DbEntity(schema = "gerasmus", table = "documents")
public class Document extends BaseEntiteImpl implements DocumentBizz {

  private Genre genre;

  private String nom;

  @DbEntityColumn("programme")
  private Integer programme;

  @DbEntityFk
  private ProgrammeDto prog;

  @DbEntityColumn("type_programme")
  private Integer typeProgramme;

  @DbEntityFk
  private TypeProgrammeDto typeProg;

  @DbEntityFk
  private ChoixMobiliteDto mobilite;

  @Override
  public Genre getGenre() {
    return this.genre;
  }

  @Override
  public String getNom() {
    return this.nom;
  }

  @Override
  public ProgrammeDto getProgramme() {
    return this.prog;
  }

  @Override
  public TypeProgrammeDto getTypeProgramme() {
    return this.typeProg;
  }

  @Override
  public void setGenre(Genre genre) {
    AppUtil.checkObject(genre, "Il faut définir un genre.");
    this.genre = genre;
  }

  @Override
  public void setGenre(String genre) {
    AppUtil.checkString(genre, "Il faut définir un genre.");
    this.genre = Genre.valueOf(genre);
  }

  @Override
  public void setNom(String nom) {
    AppUtil.checkString(nom, "Il faut définir un nom.");
    this.nom = nom;
  }


  @Override
  public void setProgramme(ProgrammeDto programme) {
    AppUtil.checkObject(programme, "Le programme ne doit pas être null");
    this.prog = programme;
  }


  @Override
  public void setTypeProgramme(TypeProgrammeDto typeProgramme) {
    AppUtil.checkObject(typeProgramme, "Le programme ne doit pas être null");
    this.typeProg = typeProgramme;
  }

  @Override
  public ChoixMobiliteDto getMobilite() {
    return this.mobilite;
  }

  @Override
  public void setMobilite(ChoixMobiliteDto mobilite) {
    AppUtil.checkObject(mobilite, "La mobilité ne doit pas être null");
    this.mobilite = mobilite;
  }

}
