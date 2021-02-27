package chapter3;

import annotations.Immutable;

import java.util.HashSet;
import java.util.Set;

/**
 * ThreeStooges
 *
 * <p>Immutable class built out of mutable underlying objects, demonstration of candidate for lock
 * elision
 */
@Immutable
public class ThreeStooges {
  private final Set<String> stooges = new HashSet<>();

  public ThreeStooges() {
    stooges.add("Moe");
    stooges.add("Larry");
    stooges.add("Curly");
  }

  public boolean isStooge(String name) {
    return stooges.contains(name);
  }
}
