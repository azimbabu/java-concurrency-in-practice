package chapter15;

import annotations.ThreadSafe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLockPseudoRandom
 *
 * <p>Random number generator using ReentrantLock
 */
@ThreadSafe
public class ReentrantLockPseudoRandom extends PseudoRandom {
  private final Lock lock = new ReentrantLock();
  private int seed;

  public ReentrantLockPseudoRandom(int seed) {
    this.seed = seed;
  }

  public int nextInt(int n) {
    lock.lock();
    try {
      int s = seed;
      seed = calculateNext(s);
      int remainder = s % n;
      return remainder > 0 ? remainder : remainder + n;
    } finally {
      lock.unlock();
    }
  }
}
