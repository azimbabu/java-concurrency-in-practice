package chapter14;

import annotations.ThreadSafe;

/**
 * GrumpyBoundedBuffer
 *
 * <p>Bounded buffer that balks when preconditions are not met
 */
@ThreadSafe
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

  private static final int DEFAULT_CAPACITY = 100;

  public GrumpyBoundedBuffer() {
    this(DEFAULT_CAPACITY);
  }

  public GrumpyBoundedBuffer(int capacity) {
    super(capacity);
  }

  public synchronized void put(V v) {
    if (isFull()) {
      throw new BufferFullException();
    }
    doPut(v);
  }

  public synchronized V take() {
    if (isEmpty()) {
      throw new BufferEmptyException();
    }
    return doTake();
  }
}

class BufferFullException extends RuntimeException {}

class BufferEmptyException extends RuntimeException {}

class ExampleUsage {
  private static final int SLEEP_GRANULARITY = 50;
  private GrumpyBoundedBuffer<String> buffer;

  void useBuffer() throws InterruptedException {
    while (true) {
      try {
        String item = buffer.take();
        // use item
        System.out.println(item);
        break;
      } catch (BufferEmptyException ex) {
        Thread.sleep(SLEEP_GRANULARITY);
      }
    }
  }
}
