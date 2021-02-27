package baeldung.semaphore;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://www.baeldung.com/cs/semaphore
 */
public class ProducerConsumer {

    private static final AtomicInteger counter = new AtomicInteger();
    public static final int MAX = 100;

    static class Producer implements Runnable {
        private Deque<Integer> buffer;
        private Semaphore semaphore;

        public Producer(Deque<Integer> buffer, Semaphore semaphore) {
            this.buffer = buffer;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            while (true) {
                int value = ThreadLocalRandom.current().nextInt();
                buffer.offer(value);
                System.out.println("Thread=" + Thread.currentThread().getName() + ", produced=" + value + ", count=" + counter.get());
                semaphore.release();
                if (counter.get() >= MAX) {
                    break;
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private Deque<Integer> buffer;
        private Semaphore semaphore;

        public Consumer(Deque<Integer> buffer, Semaphore semaphore) {
            this.buffer = buffer;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    semaphore.acquire();
                    System.out.println("Thread=" + Thread.currentThread().getName() + ", consumed=" + buffer.poll() + ", count=" + counter.get());
                    if (counter.incrementAndGet() >= MAX) {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

  public static void main(String[] args) {
      ExecutorService executorService = Executors.newCachedThreadPool();
      ArrayDeque<Integer> queue = new ArrayDeque<>();
      Semaphore semaphore = new Semaphore(0);
      executorService.execute(new Producer(queue, semaphore));
      executorService.execute(new Consumer(queue, semaphore));
      executorService.shutdown();
  }
}
