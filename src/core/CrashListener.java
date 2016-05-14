package core;

public interface CrashListener {
  /**
   * Method called when an exception was not caught, causing the application to crash.
   */
  void onCrash(Throwable fatalException);
}
