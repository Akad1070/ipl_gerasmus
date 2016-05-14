package dal.dao.mocks;

import core.config.AppConfig;
import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.ProgrammeDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.ProgrammeDto;
import util.AppUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoCache
public class MockProgrammeDaoImpl extends DaoImpl<ProgrammeDto> implements ProgrammeDao {

  private final List<ProgrammeDto> programmes =
      Collections.synchronizedList(new ArrayList<ProgrammeDto>());


  private void insertIfEmpty() {
    if (!programmes.isEmpty()) {
      return;
    }

    ProgrammeDto progra = factory.getProgramme();
    progra.setId(1);
    programmes.add(progra);

    progra = factory.getProgramme();
    progra.setId(2);
    progra.setNom(AppConfig.getValueOf("nom2_prog"));
    programmes.add(progra);

    progra = factory.getProgramme();
    progra.setId(2);
    progra.setNom(AppConfig.getValueOf("nom3_prog"));
    programmes.add(progra);

    progra = factory.getProgramme();
    progra.setId(2);
    progra.setNom(AppConfig.getValueOf("nom4_prog"));
    programmes.add(progra);


  }



  @Override
  public ProgrammeDto findById(int id) {
    insertIfEmpty();
    return programmes.stream().filter(prog -> prog.getId() == id).findFirst().orElse(null);
  }

  @Override
  public List<ProgrammeDto> findAll() {
    insertIfEmpty();
    return Collections.unmodifiableList(programmes);
  }


  @Override
  public ProgrammeDto findByName(ProgrammeDto programme) {
    AppUtil.checkObject(programme);
    insertIfEmpty();
    return programmes.stream().filter(p -> p.getNom().contains(programme.getNom())).findFirst()
        .orElse(null);
  }

  @Override
  public ProgrammeDto findByMobilite(ChoixMobiliteDto mobi) {
    return factory.getProgramme();
  }

}
