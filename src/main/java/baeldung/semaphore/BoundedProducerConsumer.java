package baeldung.semaphore;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/** https://www.baeldung.com/cs/semaphore */
public class BoundedProducerConsumer {

  private static final AtomicInteger counter = new AtomicInteger();
  private static final int MAX = 100;

  public static void main(String[] args) {
    Semaphore full = new Semaphore(0);
    Semaphore empty = new Semaphore(10);
    Semaphore mutex = new Semaphore(1);
    Deque<Integer> buffer = new ArrayDeque<>();

    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.execute(new BoundedProducerConsumer.Producer(buffer, full, empty, mutex));
    executorService.execute(new BoundedProducerConsumer.Consumer(buffer, full, empty, mutex));

    executorService.shutdown();
  }

  static class Producer implements Runnable {
    private Deque<Integer> buffer;
    private Semaphore full;
    private Semaphore empty;
    private Semaphore mutex;

    public Producer(Deque<Integer> buffer, Semaphore full, Semaphore empty, Semaphore mutex) {
      this.buffer = buffer;
      this.full = full;
      this.empty = empty;
      this.mutex = mutex;
    }

    @Override
    public void run() {
      try {
        while (true) {
          int value = ThreadLocalRandom.current().nextInt();

          empty.acquire();
          mutex.acquire();

          buffer.offer(value);
          System.out.println(
              "Thread="
                  + Thread.currentThread().getName()
                  + ", produced="
                  + value
                  + ", count="
                  + counter.get());

          mutex.release();
          full.release();

          if (counter.incrementAndGet() >= MAX) {
            break;
          }
        }

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  static class Consumer implements Runnable {
    private Deque<Integer> buffer;
    private Semaphore full;
    private Semaphore empty;
    private Semaphore mutex;

    public Consumer(Deque<Integer> buffer, Semaphore full, Semaphore empty, Semaphore mutex) {
      this.buffer = buffer;
      this.full = full;
      this.empty = empty;
      this.mutex = mutex;
    }

    @Override
    public void run() {
      try {
        while (true) {
          full.acquire();
          mutex.acquire();

          int value = buffer.poll();
          System.out.println(
              "Thread="
                  + Thread.currentThread().getName()
                  + ", consumed="
                  + value
                  + ", count="
                  + counter.get());

          mutex.release();
          empty.release();

          if (counter.incrementAndGet() >= MAX) {
            break;
          }
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
