package chapter12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TestThreadPool {

  private final TestingThreadFactory threadFactory = new TestingThreadFactory();

  @Test
  void testPoolExpansion() throws InterruptedException {
    int maxSize = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(maxSize, threadFactory);

    for (int i = 0; i < 10 * maxSize; i++) {
      executorService.execute(
          () -> {
            try {
              Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
          });
    }

    for (int i = 0; i < 20 && threadFactory.numCreated.get() < maxSize; i++) {
      Thread.sleep(100);
    }

    Assertions.assertEquals(threadFactory.numCreated.get(), maxSize);
    executorService.shutdownNow();
  }

  class TestingThreadFactory implements ThreadFactory {
    private final AtomicInteger numCreated = new AtomicInteger();
    private final ThreadFactory factory = Executors.defaultThreadFactory();

    @Override
    public Thread newThread(Runnable r) {
      numCreated.incrementAndGet();
      return factory.newThread(r);
    }
  }
}
