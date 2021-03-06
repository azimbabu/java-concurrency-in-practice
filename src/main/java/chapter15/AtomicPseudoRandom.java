package chapter15;

import annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicPseudoRandom
 *
 * <p>Random number generator using AtomicInteger
 */
@ThreadSafe
public class AtomicPseudoRandom extends PseudoRandom {
  private AtomicInteger seed;

  public AtomicPseudoRandom(int seed) {
    this.seed = new AtomicInteger(seed);
  }

  public int nextInt(int n) {
    while (true) {
      int s = seed.get();
      int nextSeed = calculateNext(s);
      if (seed.compareAndSet(s, nextSeed)) {
        int remainder = s % n;
        return remainder > 0 ? remainder : remainder + n;
      }
    }
  }
}
