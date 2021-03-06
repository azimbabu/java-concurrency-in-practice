package chapter16;

import annotations.NotThreadSafe;

/**
 * DoubleCheckedLocking
 *
 * <p>Double-checked-locking antipattern
 */
@NotThreadSafe
public class DoubleCheckedLocking {
  private static Resource resource;

  public static Resource getInstance() {
    if (resource == null) {
      synchronized (DoubleCheckedLocking.class) {
        if (resource == null) {
          resource = new Resource();
        }
      }
    }
    return resource;
  }

  static class Resource {}
}
