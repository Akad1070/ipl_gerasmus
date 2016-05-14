package dal.dao.mocks;

import core.config.AppConfig;
import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.PaysDao;
import domaine.dto.PartenaireDto;
import domaine.dto.PaysDto;
import domaine.dto.ProgrammeDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoCache
public class MockPaysDaoImpl extends DaoImpl<PaysDto> implements PaysDao {

  private final List<PaysDto> dbPays =
      Collections.synchronizedList(new ArrayList<PaysDto>());


  private void insertIfEmpty() {
    if (!dbPays.isEmpty()) {
      return;
    }

    dbPays.add(factory.getPays());

    for (int i = 2; i <= 4; ++i) {
      PaysDto pays = factory.getPays();
      pays.setId(AppConfig.getInt("id" + i + "_pays"));
      pays.setCode(AppConfig.getValueOf("code" + i + "_pays"));
      pays.setNom(AppConfig.getValueOf("nom" + i + "_pays"));
      ProgrammeDto prog = factory.getProgramme();
      prog.setId(i);
      pays.setProgramme(prog);
      dbPays.add(pays);
    }



  }

  @Override
  public PaysDto findByCode(PaysDto pays) {
    insertIfEmpty();
    return dbPays.stream().filter(p -> pays.getCode().contains(p.getCode())).findFirst()
        .orElse(null);
  }

  @Override
  public PaysDto findByNom(PaysDto pays) {
    insertIfEmpty();
    return dbPays.stream().filter(p -> pays.getNom().contains(p.getNom())).findFirst().orElse(null);
  }

  @Override
  public List<PaysDto> findByProgramme(ProgrammeDto prog) {
    insertIfEmpty();
    return dbPays.stream().filter(p -> prog.getId() == p.getProgramme().getId())
        .collect(Collectors.toList());
  }



  @Override
  public PaysDto findById(int id) {
    insertIfEmpty();
    return dbPays.stream().filter(p -> id == p.getId()).findFirst().orElse(null);
  }

  @Override
  public List<PaysDto> findAll() {
    insertIfEmpty();
    return Collections.unmodifiableList(dbPays);
  }

  @Override
  public PaysDto findByPartenaire(PartenaireDto pat) {
    insertIfEmpty();
    return dbPays.stream().filter(p -> pat.getPays().getId() == p.getId()).findFirst().orElse(null);
  }



}
