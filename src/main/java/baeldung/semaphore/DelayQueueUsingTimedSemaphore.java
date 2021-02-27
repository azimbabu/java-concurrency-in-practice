package baeldung.semaphore;

import org.apache.commons.lang3.concurrent.TimedSemaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/** https://www.baeldung.com/cs/semaphore */
public class DelayQueueUsingTimedSemaphore {

  private TimedSemaphore semaphore;

  public DelayQueueUsingTimedSemaphore(long timeAmount, TimeUnit timeUnit, int slotLimit) {
    semaphore = new TimedSemaphore(timeAmount, timeUnit, slotLimit);
  }

  public static void main(String[] args) throws InterruptedException {
    int slots = 50;
    ExecutorService executorService = Executors.newFixedThreadPool(slots);
    DelayQueueUsingTimedSemaphore delayQueue =
        new DelayQueueUsingTimedSemaphore(1, TimeUnit.SECONDS, slots);

    IntStream.range(0, slots).forEach(value -> executorService.execute(delayQueue::tryAdd));
    executorService.shutdown();

    System.out.println("Available slots=" + delayQueue.getAvailableSlots());
    System.out.println("Next add" + delayQueue.tryAdd());

    Thread.sleep(1000);

    System.out.println("Available slots=" + delayQueue.getAvailableSlots());
    System.out.println("Next add=" + delayQueue.tryAdd());
    delayQueue.semaphore.shutdown();
  }

  public boolean tryAdd() {
    return semaphore.tryAcquire();
  }

  public int getAvailableSlots() {
    return semaphore.getAvailablePermits();
  }
}
