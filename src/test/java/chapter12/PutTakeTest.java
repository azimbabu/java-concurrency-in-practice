package chapter12;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PutTakeTest {

  protected static final ExecutorService executorService = Executors.newCachedThreadPool();
  protected final int numTrials, numPairs;
  protected final AtomicInteger putSum = new AtomicInteger(0);
  protected final AtomicInteger takeSum = new AtomicInteger(0);
  private final SemaphoreBoundedBuffer<Integer> boundedBuffer;
  protected CyclicBarrier barrier;

  public PutTakeTest(int capacity, int numPairs, int numTrials) {
    this.boundedBuffer = new SemaphoreBoundedBuffer<>(capacity);
    this.numTrials = numTrials;
    this.numPairs = numPairs;
    this.barrier = new CyclicBarrier(numPairs * 2 + 1);
  }

  static int xOrShift(int x) {
    x ^= (x << 6);
    x ^= (x >>> 21);
    x ^= (x << 7);
    return x;
  }

  public static void main(String[] args) throws Exception {
    new PutTakeTest(10, 10, 100000).test(); // sample parameters
    executorService.shutdown();
  }

  void test() {
    try {
      for (int i = 0; i < numPairs; i++) {
        executorService.execute(new Producer());
        executorService.execute(new Consumer());
      }
      barrier.await(); // wait for all threads to be ready
      barrier.await(); // wait for all threads to finish
      assertEquals(putSum.get(), takeSum.get());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  class Producer implements Runnable {
    @Override
    public void run() {
      try {
        int seed = (this.hashCode() ^ (int) System.nanoTime());
        int sum = 0;
        barrier.await();
        for (int i = 0; i < numTrials; i++) {
          boundedBuffer.put(seed);
          sum += seed;
          seed = xOrShift(seed);
        }
        putSum.getAndAdd(sum);
        barrier.await();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  class Consumer implements Runnable {
    @Override
    public void run() {
      try {
        barrier.await();
        int sum = 0;
        for (int i = 0; i < numTrials; i++) {
          sum += boundedBuffer.take();
        }
        takeSum.getAndAdd(sum);
        barrier.await();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
