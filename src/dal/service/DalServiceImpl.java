package dal.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import org.apache.commons.dbcp2.BasicDataSource;

import core.AppContext;
import core.AppContext.Layer;
import core.config.AppConfig;
import core.exceptions.AppException;
import core.exceptions.BizzException;
import core.exceptions.FatalException;
import core.injecteur.InjecteurDependance.Injecter;
import util.AppUtil;


// Ne doit pas être en cache, pour pouvoir creer une nouvelle connexion à chaque instance.
public class DalServiceImpl implements DalService, DalBackendService {

  @Injecter
  private AppContext context;

  private static final ThreadLocal<Connection> map = new ThreadLocal<Connection>() {
    @Override
    protected Connection initialValue() {
      return DalServiceImpl.initConnection(AppConfig.getValueOf("driverClassName"),
          AppConfig.getValueOf("url"));
    }
  };



  /**
   * Initialiser une {@link Connection} en la mettant le {@link ThreadLocal}.
   *
   * @param driver - Le driver pour la connexion
   * @param url - L'url de connexion
   */
  private static Connection initConnection(String driver, String url) {
    AppUtil.checkString(driver);
    AppUtil.checkString(url);

    try {
      Class.forName(driver); // Test si j'ai le driver
      Connection con = null;

      try (BasicDataSource bds = new BasicDataSource()) {
        bds.setDriverClassName(driver);
        bds.setUrl(url);
        con = bds.getConnection();
        // con = BasicDataSourceFactory.createDataSource(AppConfig.getProps()).getConnection();
      }
      System.out.println("Init for Thread #" + Thread.currentThread().getId());
      return con;
    } catch (ClassNotFoundException exception) {
      throw new FatalException("Driver Manquant ! ", exception);
    } catch (SQLException exception) {
      throw new FatalException("Connection à  la BDD - Echouée", exception);
    } catch (Exception exception) {
      throw new FatalException("Création du DataSource pour la ConnectionPool - " + "Echouée",
          exception);
    }

  }

  @Override
  public void closeConnection() {
    try {
      DalServiceImpl.map.get().close();
    } catch (SQLException exception) {
      context.getLayerLogger(Layer.DAL).log(Level.SEVERE, "closeConnection échoué", exception);
    }
  }

  @Override
  public boolean commit() {
    try {
      DalServiceImpl.map.get().commit();
      DalServiceImpl.map.get().setAutoCommit(true);
      return true;
    } catch (SQLException exception) {
      context.getLayerLogger(Layer.DAL).log(Level.SEVERE, "Commit échoué", exception);
      throw new AppException("Commit échoué", exception);
    }
  }

  @Override
  public boolean startTransaction() {
    try {
      DalServiceImpl.map.get().setAutoCommit(false);
      return true;
    } catch (SQLException exception) {
      context.getLayerLogger(Layer.DAL).log(Level.SEVERE, "setAutoCommit échoué", exception);
      throw new BizzException("setAutoCommit échoué", exception);
    }
  }

  @Override
  public boolean rollback() {
    try {
      DalServiceImpl.map.get().rollback();
      DalServiceImpl.map.get().setAutoCommit(true);
      return true;
    } catch (SQLException exception) {
      context.getLayerLogger(Layer.DAL).log(Level.SEVERE, "Rollback échoué", exception);
      throw new BizzException("Rollback échoué", exception);
    }
  }


  /*
   * *************************************************************************************
   * ------------------------------ Prepared Statement -----------------------------------
   * *************************************************************************************
   */


  @Override
  public PreparedStatement createPrepareStatement(String query, int flag) {
    try {
      if (flag == 0) {
        return DalServiceImpl.map.get().prepareStatement(query);
      }
      return DalServiceImpl.map.get().prepareStatement(query, flag);
    } catch (SQLException exception) {
      context.getLayerLogger(Layer.DAL).log(Level.SEVERE, "Creation du PrepareStatement échoué",
          exception);
      throw new AppException("Creation du PrepareStatement échoué", exception);
    }
  }

  @Override
  public PreparedStatement createPrepareStatement(String query) {
    return createPrepareStatement(query, 0);
  }

}
