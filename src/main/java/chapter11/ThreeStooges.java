package chapter11;

import annotations.Immutable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * ThreeStooges
 *
 * <p>Immutable class built out of mutable underlying objects, demonstration of candidate for lock
 * elision
 */
@Immutable
public final class ThreeStooges {
  private final Set<String> stooges = new HashSet<>();

  public ThreeStooges() {
    stooges.add("Moe");
    stooges.add("Larry");
    stooges.add("Curly");
  }

  public boolean isStooge(String name) {
    return stooges.contains(name);
  }

  public String getStoogeNames() {
    List<String> stooges = new Vector<>();
    stooges.add("Moe");
    stooges.add("Larry");
    stooges.add("Curly");
    return stooges.toString();
  }
}
