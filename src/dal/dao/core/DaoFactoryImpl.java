package dal.dao.core;

import core.injecteur.InjecteurDependance.Injecter;
import dal.dao.interfaces.AnnulationDao;
import dal.dao.interfaces.ChoixMobiliteDao;
import dal.dao.interfaces.DemandeMobiliteDao;
import dal.dao.interfaces.DepartementDao;
import dal.dao.interfaces.DocumentDao;
import dal.dao.interfaces.LogicielDao;
import dal.dao.interfaces.PartenaireDao;
import dal.dao.interfaces.PaysDao;
import dal.dao.interfaces.ProgrammeDao;
import dal.dao.interfaces.TypeProgrammeDao;
import dal.dao.interfaces.UserDao;
import dal.dao.interfaces.UserInfoDao;

public class DaoFactoryImpl implements DaoFactory {
  @Injecter
  private LogicielDao logicielDao;

  @Injecter
  private DemandeMobiliteDao demandeMobiliteDao;

  @Injecter
  private AnnulationDao annulationDao;

  @Injecter
  private ChoixMobiliteDao choixMobiliteDao;

  @Injecter
  private PartenaireDao partenaireDao;

  @Injecter
  private DepartementDao departementDao;

  @Injecter
  private UserDao userDao;

  @Injecter
  private UserInfoDao userInfoDao;

  @Injecter
  private PaysDao paysDao;

  @Injecter
  private ProgrammeDao programmeDao;

  @Injecter
  private TypeProgrammeDao typeProgrammeDao;

  @Injecter
  private DocumentDao documentDao;



  @Override
  public final DemandeMobiliteDao getDemandeMobiliteDao() {
    return demandeMobiliteDao;
  }

  @Override
  public final AnnulationDao getAnnulationDao() {
    return annulationDao;
  }

  @Override
  public final ChoixMobiliteDao getChoixMobiliteDao() {
    return choixMobiliteDao;
  }

  @Override
  public final PartenaireDao getPartenaireDao() {
    return partenaireDao;
  }

  @Override
  public final DepartementDao getDepartementDao() {
    return departementDao;
  }

  @Override
  public final UserDao getUserDao() {
    return userDao;
  }

  @Override
  public final UserInfoDao getUserInfoDao() {
    return userInfoDao;
  }

  @Override
  public final PaysDao getPaysDao() {
    return paysDao;
  }

  @Override
  public final ProgrammeDao getProgrammeDao() {
    return programmeDao;
  }

  @Override
  public final TypeProgrammeDao getTypeProgrammeDao() {
    return typeProgrammeDao;
  }

  @Override
  public final DocumentDao getDocumentDao() {
    return documentDao;
  }

  @Override
  public LogicielDao getLogicielDao() {
    return logicielDao;
  }

}
