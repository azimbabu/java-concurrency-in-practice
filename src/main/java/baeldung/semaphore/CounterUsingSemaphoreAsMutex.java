package baeldung.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

/** https://www.baeldung.com/cs/semaphore */
public class CounterUsingSemaphoreAsMutex {

  private Semaphore semaphore;
  private int count;

  public CounterUsingSemaphoreAsMutex() {
    semaphore = new Semaphore(1);
    count = 0;
  }

  public static void main(String[] args) throws InterruptedException {
    int count = 5;
    ExecutorService executorService = Executors.newFixedThreadPool(count);
    CounterUsingSemaphoreAsMutex counter = new CounterUsingSemaphoreAsMutex();
    IntStream.range(0, 2 * count)
        .forEach(
            value ->
                executorService.execute(
                    () -> {
                      try {
                        counter.increase();
                      } catch (InterruptedException e) {
                        e.printStackTrace();
                      }
                    }));

    System.out.println("hasQueuedThreads=" + counter.hasQueuedThreads());
    System.out.println("count=" + counter.getCount());
    Thread.sleep(count * 1000);
    System.out.println("hasQueuedThreads=" + counter.hasQueuedThreads());
    System.out.println("count=" + counter.getCount());
    executorService.shutdown();
  }

  public void increase() throws InterruptedException {
    semaphore.acquire();
    this.count++;
    Thread.sleep(1000);
    semaphore.release();
    System.out.println("this.count=" + count);
  }

  public int getCount() {
    return count;
  }

  public boolean hasQueuedThreads() {
    return semaphore.hasQueuedThreads();
  }
}
