package dal.dao.core;

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

public interface DaoFactory {

  DemandeMobiliteDao getDemandeMobiliteDao();

  AnnulationDao getAnnulationDao();

  ChoixMobiliteDao getChoixMobiliteDao();

  PartenaireDao getPartenaireDao();

  DepartementDao getDepartementDao();

  UserDao getUserDao();

  UserInfoDao getUserInfoDao();

  PaysDao getPaysDao();

  ProgrammeDao getProgrammeDao();

  TypeProgrammeDao getTypeProgrammeDao();

  DocumentDao getDocumentDao();

  LogicielDao getLogicielDao();
}
