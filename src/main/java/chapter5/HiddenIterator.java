package chapter5;

import annotations.GuardedBy;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * HiddenIterator
 *
 * <p>Iteration hidden within string concatenation
 */
public class HiddenIterator {
  @GuardedBy("this")
  private final Set<Integer> set = new HashSet<>();

  public synchronized void add(Integer i) {
    set.add(i);
  }

  public synchronized void remove(Integer i) {
    set.remove(i);
  }

  public void addTenThings() {
    Random random = new Random();
    for (int i = 0; i < 10; i++) {
      add(random.nextInt());
    }
    System.out.println("DEBUG: added ten elements to " + set);
  }
}
