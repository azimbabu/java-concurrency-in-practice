package chapter14;

import annotations.ThreadSafe;

/**
 * BoundedBuffer
 *
 * <p>Bounded buffer using condition queues
 */
@ThreadSafe
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {
  // CONDITION PREDICATE: not-full (!isFull())
  // CONDITION PREDICATE: not-empty (!isEmpty())

  private static final int DEFAULT_CAPACITY = 100;

  public BoundedBuffer() {
    this(DEFAULT_CAPACITY);
  }

  public BoundedBuffer(int capacity) {
    super(capacity);
  }

  // BLOCKS-UNTIL: not-full
  public synchronized void put(V v) throws InterruptedException {
    while (isFull()) {
      wait();
    }
    doPut(v);
    notifyAll();
  }

  // BLOCKS-UNTIL: not-empty
  public synchronized V take() throws InterruptedException {
    while (isEmpty()) {
      wait();
    }
    V v = doTake();
    notifyAll();
    return v;
  }
}
