package chapter16;

import annotations.ThreadSafe;

/**
 * ResourceFactory
 *
 * <p>Lazy initialization holder class idiom
 */
@ThreadSafe
public class ResourceFactory {
  public static Resource getInstance() {
    return ResourceHolder.resource;
  }

  private static class ResourceHolder {
    private static Resource resource = new Resource();
  }

  static class Resource {}
}
