package chapter16;

import annotations.ThreadSafe;

/**
 * SafeLazyInitialization
 *
 * <p>Thread-safe lazy initialization
 */
@ThreadSafe
public class SafeLazyInitialization {
  private static Resource resource;

  public synchronized Resource getInstance() {
    if (resource == null) {
      resource = new Resource();
    }
    return resource;
  }

  static class Resource {}
}
