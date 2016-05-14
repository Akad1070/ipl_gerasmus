package domaine.ucc;

import core.config.AppConfig;
import core.exceptions.BizzException;
import core.injecteur.InjecteurDependance.Injecter;
import dal.dao.core.DaoFactoryImpl;
import domaine.bizz.interfaces.UserBizz;
import domaine.dto.UserDto;
import domaine.ucc.interfaces.QuidamUcc;
import util.AppUtil;

import java.time.LocalDate;

public class QuidamUccImpl implements QuidamUcc {

  @Injecter
  private DaoFactoryImpl daoFactory;

  private UserBizz userBizz;


  @Override
  public UserDto inscrire(UserDto user) {
    AppUtil.checkObjects(user, user.getDepartement());
    userBizz = (UserBizz) user;
    userBizz.checkBeforeInscription();

    userBizz.setDateInscription(LocalDate.now());

    // Permet de déterminer si l'utilisateur est un proffesseur ou un étudiant en fonction de son
    // mail.
    userBizz.determinerType();

    if (daoFactory.getUserDao().getUserByPseudo(userBizz) != null) {
      throw new BizzException(AppConfig.getAppValueOf("PSEUDO_DEJA_PRIS"));
    }

    if (daoFactory.getUserDao().getUserByMail(userBizz) != null) {
      throw new BizzException(AppConfig.getAppValueOf("EMAIL_DEJA_PRIS"));
    }

    userBizz
        .setDepartement(daoFactory.getDepartementDao().findById(userBizz.getDepartement().getId()));

    userBizz.crypterMdp();

    return daoFactory.getUserDao().inscrire(userBizz);

  }


  @Override
  public UserDto seConnecter(UserDto user) {
    AppUtil.checkObject(user);
    userBizz = (UserBizz) user;
    userBizz.verifierIdentifiants();

    String mdpEntre = userBizz.getMdp();
    // L'utilisateur se connecte-t-il par mail ?
    if (userBizz.verifierMail()) {
      userBizz.setMail(userBizz.getIdentifiant());
      userBizz = (UserBizz) daoFactory.getUserDao().getUserByMail(userBizz);
    } else {
      // Non alors il a entré son pseudo.
      userBizz.setPseudo(userBizz.getIdentifiant());
      userBizz = (UserBizz) daoFactory.getUserDao().getUserByPseudo(userBizz);
    }
    // Cet user n'existe pas ou son mdp est éroné.
    if (userBizz == null || !userBizz.verifierMdp(mdpEntre)) {
      throw new BizzException(AppConfig.getAppValueOf("USER_INCONNU"));
    }

    userBizz.setDepartement(daoFactory.getDepartementDao().findByUser(userBizz));

    return userBizz;
  }

}
