package baeldung.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

/** https://www.baeldung.com/cs/semaphore */
public class LoginQueueUsingSemaphore {
  private Semaphore semaphore;

  public LoginQueueUsingSemaphore(int slotLimit) {
    semaphore = new Semaphore(slotLimit);
  }

  public static void main(String[] args) {
    int slots = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(slots);
    LoginQueueUsingSemaphore loginQueue = new LoginQueueUsingSemaphore(slots);
    IntStream.range(0, slots).forEach(value -> executorService.execute(loginQueue::tryLogin));
    executorService.shutdown();

    System.out.println("Available slots=" + loginQueue.getAvailableSlots());
    System.out.println("Next login=" + loginQueue.tryLogin());

    loginQueue.logout();

    System.out.println("Available slots=" + loginQueue.getAvailableSlots());
    System.out.println("Next login=" + loginQueue.tryLogin());
  }

  public boolean tryLogin() {
    return semaphore.tryAcquire();
  }

  public void logout() {
    semaphore.release();
  }

  public int getAvailableSlots() {
    return semaphore.availablePermits();
  }
}
