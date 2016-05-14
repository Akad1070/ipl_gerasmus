package dal.dao.mocks;


import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.UserDao;
import domaine.dto.DemandeMobiliteDto;
import domaine.dto.PartenaireDto;
import domaine.dto.UserDto;
import util.AppUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@NoCache
public class MockUserDaoImpl extends DaoImpl<UserDto> implements UserDao {

  private final Map<String, UserDto> users = new ConcurrentHashMap<String, UserDto>();

  @Override
  public UserDto inscrire(UserDto user) {

    if (!users.containsKey(user.getPseudo())) {
      users.put(user.getPseudo().toLowerCase(), user);
    }
    return user;
  }

  @Override
  public UserDto getUserByPseudo(UserDto user) {
    return users.get(user.getPseudo().toLowerCase());
  }

  @Override
  public UserDto getUserByMail(UserDto user) {
    return users.values().stream().filter(u -> user.getMail().equalsIgnoreCase(u.getMail()))
        .findFirst().orElse(null);
  }

  @Override
  public List<UserDto> findByName(UserDto user) {
    List<UserDto> liste = users.values().stream().filter(u -> {
      boolean found = u.getNom().equalsIgnoreCase(user.getNom());
      return (found || u.getPrenom().equalsIgnoreCase(user.getPrenom()));
    })
      .collect(Collectors.toList());

    if (liste.size() == 0) {
      return null;
    }
    return liste;
  }

  @Override
  public UserDto findById(int id) {
    return users.values().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
  }

  @Override
  public List<UserDto> findAll() {
    return users.entrySet().stream().map((usr) -> usr.getValue()).collect(Collectors.toList());
  }

  @Override
  public UserDto findByPartenaire(PartenaireDto pat) {
    AppUtil.checkObjects(pat, pat.getCreateur());
    return findById(pat.getCreateur().getId());
  }

  @Override
  public UserDto findByDemandeMobilite(DemandeMobiliteDto candidature) {
    return users.values().stream().filter((usr) -> candidature.getEtudiant().getId() == usr.getId())
        .collect(Collectors.toList()).get(0);
  }

}
