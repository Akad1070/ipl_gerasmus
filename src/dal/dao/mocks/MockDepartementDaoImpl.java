package dal.dao.mocks;

import core.config.AppConfig;
import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.DepartementDao;
import domaine.dto.DepartementDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoCache
public class MockDepartementDaoImpl extends DaoImpl<DepartementDto> implements DepartementDao {

  private final List<DepartementDto> dbDepartements =
      Collections.synchronizedList(new ArrayList<DepartementDto>());


  private void insertIfEmpty() {
    if (!dbDepartements.isEmpty()) {
      return;
    }

    dbDepartements.add(factory.getDepartement());

    for (int i = 2; i <= 4; ++i) {
      DepartementDto dto = factory.getDepartement();
      dto.setId(AppConfig.getInt("id" + i + "_dep"));
      dto.setCode(AppConfig.getValueOf("code" + i + "_dep"));
      dto.setNom(AppConfig.getValueOf("nom" + i + "_dep"));
      dbDepartements.add(dto);
    }


  }


  @Override
  public DepartementDto findById(int id) {
    insertIfEmpty();
    return dbDepartements.parallelStream().filter(departement -> departement.getId() == id)
        .findFirst().orElse(null);
  }

  @Override
  public DepartementDto findByCode(DepartementDto dep) {
    insertIfEmpty();
    return dbDepartements.stream()
        .filter(departement -> departement.getCode().equals(dep.getCode())).findFirst()
        .orElse(factory.getDepartement());
  }

  @Override
  public List<DepartementDto> findAll() {
    insertIfEmpty();
    return Collections.unmodifiableList(dbDepartements);
  }

  @Override
  public DepartementDto findByUser(UserDto user) {
    insertIfEmpty();
    return dbDepartements.stream()
        .filter(departement -> departement.getId() == (user.getDepartement().getId())).findFirst()
        .orElse(factory.getDepartement());
  }


  @Override
  public List<DepartementDto> findByPartenaire(PartenaireDto pat) {
    insertIfEmpty();
    return dbDepartements.stream().filter(d -> pat.getIplDepartements().contains(d))
        .collect(Collectors.toList());
  }

}
