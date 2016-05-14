package core;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;



public class CrashWriter implements CrashListener {
  private static final DateTimeFormatter FILENAME_FORMATTER = DateTimeFormatter
      .ofPattern("yyyy-MM-dd_HH-mm-ss");

  private static Path REPORTS_FOLDER = FileSystems.getDefault().getPath("crash-reports");

  private final AppContext appContext;
  private final Logger logger;

  /**
   * Init. le CrashWriter.
   *
   * @param appContext - Le contexte actuel.
   */
  public CrashWriter(AppContext appContext) {
    this.appContext = appContext;
    this.logger = this.appContext.getAppLogger();
  }



  @Override
  public void onCrash(Throwable crashSource) {
    String reportFile = this.writeCrashReport(crashSource);

    this.logger.severe("A fatal exception has occurred");
    if (reportFile != null) {
      this.logger.severe("The details have been saved to " + reportFile);
    } else {
      this.logger.severe("The details could not be saved, the crash-reports folder "
          + CrashWriter.REPORTS_FOLDER.toAbsolutePath().toString() + " cannot be created");
    }
  }


  /**
   * Logs an exception to a file.
   *
   * @param exception The exception to log.
   * @return The name of the file the exception has been written to.
   */
  private String writeCrashReport(Throwable exception) {
    if (!CrashWriter.REPORTS_FOLDER.toFile().exists()
        && !CrashWriter.REPORTS_FOLDER.toFile().mkdirs()) {
      return null;
    }

    LocalDateTime crashTime = LocalDateTime.now();

    String logFileName = crashTime.format(CrashWriter.FILENAME_FORMATTER) + ".log";

    Path pathToCrashFile = Paths.get(CrashWriter.REPORTS_FOLDER.toString(), logFileName);

    try (Writer writer = Files.newBufferedWriter(pathToCrashFile, StandardCharsets.UTF_8)) {
      if (!pathToCrashFile.toFile().createNewFile()) {
        this.logger.severe("Could not create logfile " + pathToCrashFile);
        return null;
      }

      writer.write("/*\\ Gerasmus /*\\ \n");
      writer.write("Date : " + crashTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\n\n");

      writer.write("Exception Message : " + exception.getMessage() + "\n");
      writer.write("Exception StackTrace : \n");
      exception.printStackTrace(new PrintWriter(writer));

    } catch (IOException except) {
      this.logger.log(Level.SEVERE, "Could not create crash report", except);
      return null;
    }

    return pathToCrashFile.toAbsolutePath().toString();
  }
}
