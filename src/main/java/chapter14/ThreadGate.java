package chapter14;

import annotations.GuardedBy;
import annotations.ThreadSafe;

/**
 * ThreadGate
 *
 * <p>Recloseable gate using wait and notifyAll
 */
@ThreadSafe
public class ThreadGate {
  // CONDITION-PREDICATE: opened-since(n) (isOpen || generation > n)
  @GuardedBy("this")
  private boolean isOpen;

  @GuardedBy("this")
  private int generation;

  public synchronized void open() {
    ++generation;
    isOpen = true;
    notifyAll();
  }

  public synchronized void close() {
    isOpen = false;
  }

  // BLOCKS-UNTIL: opened-since(n) (isOpen || generation > n)
  public synchronized void await() throws InterruptedException {
    int arrivalGeneration = generation;
    while (!isOpen && arrivalGeneration == generation) {
      wait();
    }
  }
}
