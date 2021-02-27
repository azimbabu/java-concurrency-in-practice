package utils;

/** StaticUtilities */
public class LaunderThrowable {
  public static RuntimeException launderThrowable(Throwable throwable) {
    if (throwable instanceof RuntimeException) {
      return (RuntimeException) throwable;
    } else if (throwable instanceof Error) {
      throw (Error) throwable;
    } else {
      throw new IllegalStateException("Not unchecked", throwable);
    }
  }
}
