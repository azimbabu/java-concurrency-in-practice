package chapter16;

import annotations.NotThreadSafe;

/**
 * UnsafeLazyInitialization
 *
 * <p>Unsafe lazy initialization
 */
@NotThreadSafe
public class UnsafeLazyInitialization {
  private static Resource resource;

  public static Resource getInstance() {
    if (resource == null) {
      resource = new Resource(); // unsafe publication
    }
    return resource;
  }

  static class Resource {}
}
