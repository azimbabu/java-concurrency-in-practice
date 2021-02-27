package chapter7;

import annotations.GuardedBy;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PrimeGenerator implements Runnable {
  private static ExecutorService executor = Executors.newCachedThreadPool();

  @GuardedBy("this")
  private final List<BigInteger> primes = new ArrayList<>();

  private volatile boolean cancelled;

  static List<BigInteger> sSecondOfPrimes() throws InterruptedException {
    PrimeGenerator generator = new PrimeGenerator();
    executor.execute(generator);
    try {
      TimeUnit.SECONDS.sleep(1);
    } finally {
      generator.cancel();
    }
    return generator.get();
  }

  public static void main(String[] args) throws InterruptedException {
    System.out.println(sSecondOfPrimes());
    executor.shutdown();
  }

  @Override
  public void run() {
    BigInteger prime = BigInteger.ONE;
    while (!cancelled) {
      prime = prime.nextProbablePrime();
      synchronized (this) {
        primes.add(prime);
      }
    }
  }

  public void cancel() {
    cancelled = true;
  }

  public synchronized List<BigInteger> get() {
    return new ArrayList<>(primes);
  }
}
