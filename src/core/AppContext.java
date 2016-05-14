package core;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import util.AppUtil;
import core.config.AppConfig;
import core.exceptions.FatalException;
import core.injecteur.InjecteurDependance;

/**
 * Retient tout les composants de l'Application sur son fonctionnement interne. Tel que le
 * {@link Profil} actuel de l'App. , le Logger et les crashListener.
 */
public class AppContext {
  private final InjecteurDependance injector;
  private final List<CrashListener> crashListeners = new ArrayList<>();
  private final Logger appLogger;
  private final Profil profileType;
  private final String appName;
  private final String version;



  /**
   * Constructeur par-default. Utilise alors le {@link Profil.PROD}.
   */
  public AppContext() {
    this(Profil.PROD, "Gerasmus");
  }

  /**
   * Initialise et configure le logger puis l'action pour {@link Thread.UncaughtExceptionHandler},
   * puis le systeme d'injection.
   *
   * @param profil - Le {@link Profil} actuel de l'App.
   */
  public AppContext(Profil profil, String appName) {
    super();
    this.addCrashListener(new CrashWriter(this));

    this.appName = appName;
    this.profileType = (profil == null ? Profil.PROD : profil);

    this.appLogger = this.initLogger();
    this.injector = this.initInjectorDependancySystem();

    this.initGlobalCatcher();

    this.version = AppConfig.getAppValueOf("version");
    String str =
        String.format("%s - %s - Launched using profile '%s' ", this.appName, this.version, profil);
    this.getAppLogger().info(str.toString());

    this.todoBeforeExit();
  }


  /**
   * Fais ca avant de fermer l'Application.
   */
  private void todoBeforeExit() {
    Runtime.getRuntime().addShutdownHook(new Thread((Runnable) () -> {
      for (Handler handler : AppContext.this.appLogger.getHandlers()) {
        handler.flush();
        handler.close();
        AppContext.this.appLogger.removeHandler(handler);
      }

    }, "Before-Shutdown-thread"));

  }



  /**
   * Definis l'action à réaliser lors d'un {@link UncaughtExceptionHandler}.
   */
  private void initGlobalCatcher() {
    Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
      this.appLogger.log(Level.SEVERE, "Fatal exception", exception);

      for (CrashListener listener : this.crashListeners) {
        listener.onCrash(exception);
      }

      for (Handler handler : this.appLogger.getHandlers()) {
        handler.close();
      }

      System.exit(1);
    });
  }

  /**
   * Configure le système d'injection. Peut être aussi utilisé pour les test unitaires comme décrit
   * en-dessous. <br>
   *
   * <pre>
   * <code>
   *    {@literal @}BeforeClass
   *    public static void setUpBeforeClass() throws Exception {
   *        AppContext ctxt = new AppContext(AppContext.Profil.TEST, "Gerasmus", "1.1.0");
   *        injector = AppBooter.initInjectorDependancySystem(ctxt);
   *    }
   * 
   *    {@literal @}Before
   *    public setUp() throws Exception{
   *        injector.injectByAnnotedField(this);
   *        .....
   *    }
   * </code>
   * </pre>
   *
   * @return {@link InjecteurDependance} contenant deja son instance et celle de {@link AppContext}.
   * @throws FatalException Une erreur est survenue durant le process. Check les .log
   */
  public InjecteurDependance initInjectorDependancySystem() {
    // Loads the config file or die on tryin !
    try {
      AppConfig.loadProperties(this.getProfil());
    } catch (IOException exception) {
      throw new FatalException("Could not load " + this.getProfil().getPropUrl(), exception);
    }
    return new InjecteurDependance(this);
  }


  /**
   * @return {@link InjecteurDependance} DI.
   */
  public InjecteurDependance getInjector() {
    return this.injector;
  }



  /**
   * Init. le logger principal pour l'App.
   */
  private Logger initLogger() {
    Logger appLogger = Logger.getLogger(this.appName);

    try {
      Formatter logFormatter = new Formatter() {
        @Override
        public String format(LogRecord record) {
          StringBuilder str = new StringBuilder();

          str.append('[').append(LocalDateTime.now().format(AppUtil.dateFormatter));
          str.append("][").append(record.getLoggerName()).append("][");
          str.append(record.getLevel().getLocalizedName()).append("] ");
          str.append(record.getMessage()).append("\r\n");

          Throwable throwable = record.getThrown();
          if (throwable != null) {
            StringWriter stringWriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringWriter));
            str.append(stringWriter).append("\r\n");
          }

          return str.toString();
        }
      };

      Handler logFile = new FileHandler(AppConfig.RES_FOLDER + "app.log");
      logFile.setFormatter(logFormatter);
      logFile.setLevel(Level.ALL);

      Handler logConsole = new ConsoleHandler() {
        @Override
        public void publish(LogRecord record) {
          try {
            String message = this.getFormatter().format(record);
            if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
              System.err.print(message);
            } else {
              System.out.print(message);
            }
          } catch (Exception exception) {
            throw new FatalException("Logger error", exception);
          }
        }
      };
      logConsole.setFormatter(logFormatter);

      appLogger.addHandler(logConsole);
      appLogger.addHandler(logFile);

      appLogger.setParent(Logger.getGlobal());
      appLogger.setUseParentHandlers(false);
    } catch (IOException exception) {
      throw new FatalException("Could not setup the application logger", exception);
    }

    return appLogger;
  }

  /**
   * Cree un loger pour une couche de l'Application.
   *
   * @param appLayer - la {@link Layer} en à logger.
   * @return le Logger pour cette couche de l'Application.
   */
  public Logger getLayerLogger(Layer appLayer) {
    Logger layerLogger = Logger.getLogger(this.appName + "-" + appLayer);
    layerLogger.setParent(this.appLogger);

    return layerLogger;
  }

  /**
   * Renvoie le logger de l'Application.
   */
  public Logger getAppLogger() {
    return this.appLogger;
  }


  /**
   * Envoie une instance la classe en params selon la correspondance dans le .properties
   *
   * @param cls - La class à instancier
   * @return L'instance
   */
  @SuppressWarnings("unchecked")
  public <I> I getInstanceOf(Class<? extends I> cls) {
    try {
      String pathForImpl = AppConfig.getValueOf(cls.getSimpleName());
      return (I) Class.forName(pathForImpl).newInstance();
    } catch (Exception excep) {
      throw new FatalException("Could not fetch '" + cls.getSimpleName() + "' implementation in "
          + AppConfig.getProfil(), excep);
    }
  }



  /**
   * @return Le {@link Profil} actuel de l'Application.
   */
  public Profil getProfil() {
    return this.profileType;
  }

  /**
   * @return la version actuel de l'Application.
   */
  public String getVersion() {
    return this.version;
  }

  /**
   * @return le nom de l'Application.
   */
  public String getAppName() {
    return this.appName;
  }

  /**
   * Ajoute un listener pour enregistrer le crash de l'Application.
   *
   * @param listener - Un {@link CrashListener} contenant que faire lors d'un Crash
   * @return TRUE si ajout effectué.
   */
  public boolean addCrashListener(CrashListener listener) {
    return this.crashListeners.add(listener);
  }

  /**
   * Enleve le crashListener de la liste.
   *
   * @param listener - Le listener en question
   * @return TRUE si suppresion effectué.
   */
  public boolean removeCrashListener(CrashListener listener) {
    return this.crashListeners.remove(listener);
  }


  /**
   * Le Profil dans lequel lancé l'Application.
   * <ul>
   * <li>PROD : l'environnement de Production</li>
   * <li>DEV : l'environnement de Developpement</li>
   * <li>TEST : l'environnement pour les Test</li>
   * </ul>
   */
  public enum Profil {
    DEV("dev.properties"), PROD("prod.properties"), TEST("dev.properties");

    private String propUrl;

    Profil(String url) {
      this.propUrl = url;
    }

    public String getPropUrl() {
      return this.propUrl;
    }

    @Override
    public String toString() {
      return "Mode " + this.name() + " : " + this.propUrl;
    }
  }

  /**
   * Les principales couches de l'Application.
   */
  public enum Layer {
    IHM, INJECTEUR, DAL, DALSQL, DAO, UCC;
  }


}
