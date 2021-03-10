package chapter16;

import annotations.ThreadSafe;

/**
 * EagerInitialization
 *
 * <p>Eager initialization
 */
@ThreadSafe
public class EagerInitialization {
  private static Resource resource = new Resource();

  public Resource getInstance() {
    return resource;
  }

  static class Resource {}
}
