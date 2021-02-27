package common;

import annotations.GuardedBy;
import annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;

/**
 * ValueLatch
 *
 * <p>Result-bearing latch used by ConcurrentPuzzleSolver
 */
@ThreadSafe
public class ValueLatch<T> {
  private final CountDownLatch done = new CountDownLatch(1);
  @GuardedBy("this")
  private T value = null;

  public boolean isSet() {
    return (done.getCount() == 0);
  }

  public T getValue() throws InterruptedException {
    done.await();
    synchronized (this) {
      return value;
    }
  }

  public synchronized void setValue(T newValue) {
    if (!isSet()) {
      value = newValue;
      done.countDown();
    }
  }
}
