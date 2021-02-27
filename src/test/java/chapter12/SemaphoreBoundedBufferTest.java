package chapter12;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SemaphoreBoundedBufferTest {

  private static final int LOCKUP_DETECT_TIMEOUT = 1000;
  private static final int CAPACITY = 10000;
  private static final int THRESHOLD = 10000;

  @Test
  void testIsEmptyWhenConstructed() {
    SemaphoreBoundedBuffer<Integer> boundedBuffer = new SemaphoreBoundedBuffer<>(10);
    assertTrue(boundedBuffer.isEmpty());
    assertFalse(boundedBuffer.isFull());
  }

  @Test
  void testIsFullAfterPuts() throws InterruptedException {
    SemaphoreBoundedBuffer<Integer> boundedBuffer = new SemaphoreBoundedBuffer<>(10);
    for (int i = 0; i < 10; i++) {
      boundedBuffer.put(i);
    }
    assertFalse(boundedBuffer.isEmpty());
    assertTrue(boundedBuffer.isFull());
  }

  @Test
  void testTakeBlocksWhenEmpty() {
    final SemaphoreBoundedBuffer<Integer> boundedBuffer = new SemaphoreBoundedBuffer<>(10);
    Thread taker =
        new Thread(
            () -> {
              try {
                int unused = boundedBuffer.take();
                fail();
              } catch (InterruptedException success) {
              }
            });

    try {
      taker.start();
      Thread.sleep(LOCKUP_DETECT_TIMEOUT);
      taker.interrupt();
      taker.join(LOCKUP_DETECT_TIMEOUT);
      assertFalse(taker.isAlive());
    } catch (Exception unexpected) {
      fail();
    }
  }

  void testLeak() throws InterruptedException {
    SemaphoreBoundedBuffer<Big> boundedBuffer = new SemaphoreBoundedBuffer<>(CAPACITY);
    int heapSize1 = snapshotHeap();

    for (int i = 0; i < CAPACITY; i++) {
      boundedBuffer.put(new Big());
    }

    for (int i = 0; i < CAPACITY; i++) {
      boundedBuffer.take();
    }

    int heapSize2 = snapshotHeap();
    assertTrue(Math.abs(heapSize1 - heapSize2) < THRESHOLD);
  }

  private int snapshotHeap() {
    /* Snapshot heap and return heap size */
    return 0;
  }

  class Big {
    double[] data = new double[100000];
  }
}
