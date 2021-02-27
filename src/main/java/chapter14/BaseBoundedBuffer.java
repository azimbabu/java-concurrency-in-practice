package chapter14;

import annotations.GuardedBy;
import annotations.ThreadSafe;

/**
 * BaseBoundedBuffer
 *
 * <p>Base class for bounded buffer implementations
 */
@ThreadSafe
public abstract class BaseBoundedBuffer<V> {
  @GuardedBy("this")
  private final V[] buffer;

  @GuardedBy("this")
  private int tail;

  @GuardedBy("this")
  private int head;

  @GuardedBy("this")
  private int count;

  protected BaseBoundedBuffer(int capacity) {
    buffer = (V[]) new Object[capacity];
  }

  public final synchronized boolean isFull() {
    return count == buffer.length;
  }

  public final synchronized boolean isEmpty() {
    return count == 0;
  }

  protected final synchronized void doPut(V v) {
    buffer[tail] = v;
    if (++tail == buffer.length) {
      tail = 0;
    }
    ++count;
  }

  protected final synchronized V doTake() {
    V v = buffer[head];
    buffer[head] = null;
    if (++head == buffer.length) {
      head = 0;
    }
    --count;
    return v;
  }
}
