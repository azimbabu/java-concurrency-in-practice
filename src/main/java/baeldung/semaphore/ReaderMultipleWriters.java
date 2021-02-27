package baeldung.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/** https://www.baeldung.com/cs/semaphore */
public class ReaderMultipleWriters {

  private static final AtomicInteger counter = new AtomicInteger();
  private static final int MAX = 100;

  public static void main(String[] args) {
    Semaphore write = new Semaphore(1);
    Semaphore mutex = new Semaphore(1);
    AtomicInteger data = new AtomicInteger();
    AtomicInteger readCount = new AtomicInteger();

    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.execute(new Writer(write, data));
    executorService.execute(new Reader(write, mutex, data, readCount));
    executorService.shutdown();
  }

  static class Writer implements Runnable {
    private Semaphore write;
    private AtomicInteger data;

    public Writer(Semaphore write, AtomicInteger data) {
      this.write = write;
      this.data = data;
    }

    @Override
    public void run() {
      try {
        while (true) {
          write.acquire();
          int value = ThreadLocalRandom.current().nextInt();
          data.set(value);
          System.out.println(
              "Thread="
                  + Thread.currentThread().getName()
                  + ", written="
                  + value
                  + ", count="
                  + counter.get());

          write.release();

          if (counter.incrementAndGet() >= MAX) {
            break;
          }
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  static class Reader implements Runnable {
    private Semaphore write;
    private Semaphore mutex;
    private AtomicInteger data;
    private AtomicInteger readCount;

    public Reader(Semaphore write, Semaphore mutex, AtomicInteger data, AtomicInteger readCount) {
      this.write = write;
      this.mutex = mutex;
      this.data = data;
      this.readCount = readCount;
    }

    @Override
    public void run() {
      try {
        while (true) {
          mutex.acquire();
          int count = readCount.incrementAndGet();
          if (count == 1) {
            write.acquire();
          }
          mutex.release();

          int value = data.get();
          System.out.println(
              "Thread="
                  + Thread.currentThread().getName()
                  + ", read="
                  + value
                  + ", count="
                  + counter.get());

          mutex.acquire();
          count = readCount.decrementAndGet();
          if (count == 0) {
            write.release();
          }
          mutex.release();

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
