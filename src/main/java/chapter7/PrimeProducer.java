package chapter7;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * PrimeProducer
 *
 * <p>Using interruption for cancellation
 */
public class PrimeProducer extends Thread {
  private final BlockingQueue<BigInteger> queue;

  public PrimeProducer(BlockingQueue<BigInteger> queue) {
    this.queue = queue;
  }

  public static void main(String[] args) throws InterruptedException {
    BlockingQueue<BigInteger> queue = new LinkedBlockingQueue<>();
    PrimeProducer producer = new PrimeProducer(queue);
    producer.start();
    try {
      TimeUnit.SECONDS.sleep(1);
    } finally {
      producer.cancel();
    }

    System.out.println(queue);
  }

  @Override
  public void run() {
    try {
      BigInteger p = BigInteger.ONE;
      while (!Thread.currentThread().isInterrupted()) {
        queue.put(p = p.nextProbablePrime());
      }
    } catch (InterruptedException consumed) {
      /* Allow thread to exit */
    }
  }

  public void cancel() {
    interrupt();
  }
}
