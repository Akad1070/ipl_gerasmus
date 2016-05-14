package dal.dao.mocks;

import core.injecteur.InjecteurDependance.NoCache;
import dal.dao.DaoImpl;
import dal.dao.interfaces.UserInfoDao;
import domaine.dto.ChoixMobiliteDto;
import domaine.dto.UserDto;
import domaine.dto.UserInfoDto;
import util.AppUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@NoCache
public class MockUserInfoDaoImpl extends DaoImpl<UserInfoDto> implements UserInfoDao {
  private final Map<Integer, UserInfoDto> dbUserInfos =
      new ConcurrentHashMap<Integer, UserInfoDto>();


  @Override
  public UserInfoDto findById(int id) {
    AppUtil.checkPositive(id);
    return dbUserInfos.get(id);
  }



  @Override
  public UserInfoDto insererInfo(UserInfoDto userInfo) {
    AppUtil.checkObject(userInfo);
    dbUserInfos.put(userInfo.getId(), userInfo);
    return userInfo;
  }


  @Override
  public UserInfoDto rechercherInfo(UserDto user) {
    AppUtil.checkObject(user);
    return dbUserInfos.get(user.getId());
  }


  @Override
  public UserInfoDto update(UserInfoDto userInfo) {
    AppUtil.checkObject(userInfo);
    List<UserInfoDto> liste =
        dbUserInfos.values().stream()
            .filter(info -> info.getId() == userInfo.getId()
                && info.getNumeroVersion() == userInfo.getNumeroVersion())
        .collect(Collectors.toList());
    if (liste.size() == 0) {
      return null;
    }
    dbUserInfos.put(liste.get(0).getId(), userInfo);
    return liste.get(0);
  }


  @Override
  public boolean compteBancaireNotNull(ChoixMobiliteDto mobilite) {
    String compteBancaire =
        dbUserInfos.get(mobilite.getDemande().getEtudiant().getId()).getCompteBancaire();
    if (compteBancaire != null && !compteBancaire.equals("")) {
      return true;
    }
    return false;
  }


}
