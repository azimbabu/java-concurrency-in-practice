package chapter5;

import java.util.concurrent.CountDownLatch;

/**
 * TestHarness
 *
 * <p>Using CountDownLatch for starting and stopping threads in timing tests
 */
public class TestHarness {
  public long timeTasks(int numThreads, final Runnable task) throws InterruptedException {
    final CountDownLatch startGate = new CountDownLatch(1);
    final CountDownLatch endGate = new CountDownLatch(numThreads);

    for (int i = 0; i < numThreads; i++) {
      Thread thread =
          new Thread() {
            @Override
            public void run() {
              try {
                startGate.await();
                try {
                  task.run();
                } finally {
                  endGate.countDown();
                }
              } catch (InterruptedException ignored) {

              }
            }
          };
      thread.start();
    }

    long start = System.nanoTime();
    startGate.countDown();
    endGate.await();
    long end = System.nanoTime();
    return end - start;
  }
}
