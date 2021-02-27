package chapter14;

import annotations.ThreadSafe;

/**
 * SleepyBoundedBuffer
 *
 * <p>Bounded buffer using crude blocking
 */
@ThreadSafe
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

  private static final int DEFAULT_CAPACITY = 100;
  private static final int SLEEP_GRANULARITY = 60;

  public SleepyBoundedBuffer() {
    this(DEFAULT_CAPACITY);
  }

  public SleepyBoundedBuffer(int capacity) {
    super(capacity);
  }

  public void put(V v) throws InterruptedException {
    while (true) {
      synchronized (this) {
        if (!isFull()) {
          doPut(v);
          return;
        }
      }
      Thread.sleep(SLEEP_GRANULARITY);
    }
  }

  public V take() throws InterruptedException {
    while (true) {
      synchronized (this) {
        if (!isEmpty()) {
          return doTake();
        }
      }
      Thread.sleep(SLEEP_GRANULARITY);
    }
  }
}
