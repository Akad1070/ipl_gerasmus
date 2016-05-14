import core.AppContext;
import core.AppContext.Profil;
import core.config.AppConfig;
import core.exceptions.FatalException;
import ihm.AppServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

  /**
   * Point d'entrée principal de l'Application.
   *
   * @param args - Contient le mode pour le lancement {@code dev, test, prod}
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      new Main(Profil.PROD);
      return;
    }
    if (args[0].equalsIgnoreCase("dev")) {
      new Main(Profil.DEV);
    } else {
      new Main(Profil.TEST);
    }

  }

  /**
   * Init. la séquence de boot de l'App et lance le serveur.
   *
   * @param mode - Le mode de l'App.
   */
  Main(Profil mode) {
    this.launchServer(mode);
  }

  private void launchServer(Profil mode) {
    AppServlet mainServlet =
        new AppContext(mode, "Gerasmus").getInjector().buildDependency(AppServlet.class);

    WebAppContext ctxt = new WebAppContext();
    ctxt.setContextPath("/");
    ctxt.addServlet(new ServletHolder(mainServlet), "/api/gerasmus/v1");
    ctxt.addServlet(new ServletHolder(new DefaultServlet()), "/");
    ctxt.setWelcomeFiles(new String[] {"layout.html"});
    ctxt.setResourceBase("public");
    ctxt.setInitParameter("cache-control", "no-store,no-cache,must-revalidate");
    ctxt.setInitParameter("allowedOrigins", "*");
    ctxt.setInitParameter("allowedMethods", "GET, POST");
    Server server = new Server(AppConfig.getInt("serverPort"));
    server.setHandler(ctxt);

    try {
      server.start();
    } catch (Exception exception) {
      throw new FatalException(AppConfig.getAppValueOf("SERVER_UNSTARTED"), exception);
    }
  }
}
