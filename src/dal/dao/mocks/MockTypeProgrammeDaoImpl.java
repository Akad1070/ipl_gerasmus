package dal.dao.mocks;

import core.config.AppConfig;
import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.TypeProgrammeDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.TypeProgrammeDto;
import util.AppUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoCache
public class MockTypeProgrammeDaoImpl extends DaoImpl<TypeProgrammeDto>
    implements TypeProgrammeDao {

  private final List<TypeProgrammeDto> typesProgramme =
      Collections.synchronizedList(new ArrayList<TypeProgrammeDto>());


  private void insertIfEmpty() {
    if (!typesProgramme.isEmpty()) {
      return;
    }
    TypeProgrammeDto progra = factory.getTypeProgramme();
    progra.setId(1);
    typesProgramme.add(progra);

    progra = factory.getTypeProgramme();
    progra.setId(2);
    progra.setNom(AppConfig.getValueOf("nom2_type_prog"));
    typesProgramme.add(progra);

    progra = factory.getTypeProgramme();
    progra.setId(2);
    progra.setNom(AppConfig.getValueOf("nom3_type_prog"));
    typesProgramme.add(progra);

    progra = factory.getTypeProgramme();
    progra.setId(2);
    progra.setNom(AppConfig.getValueOf("nom4_type_prog"));
    typesProgramme.add(progra);


  }

  @Override
  public TypeProgrammeDto findById(int id) {
    insertIfEmpty();
    return typesProgramme.stream().filter(prog -> prog.getId() == id).findFirst().orElse(null);
  }

  @Override
  public List<TypeProgrammeDto> findAll() {
    insertIfEmpty();
    return Collections.unmodifiableList(typesProgramme);
  }


  @Override
  public TypeProgrammeDto findByName(TypeProgrammeDto programme) {
    AppUtil.checkObject(programme);
    insertIfEmpty();
    return typesProgramme.stream().filter(p -> p.getNom().contains(programme.getNom())).findFirst()
        .orElse(null);
  }


  @Override
  public TypeProgrammeDto findByMobilite(ChoixMobiliteDto mobi) {
    return factory.getTypeProgramme();
  }

}
