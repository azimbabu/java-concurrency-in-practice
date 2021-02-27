package chapter5;

import annotations.GuardedBy;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

interface Computable<A, V> {
  V compute(A arg) throws InterruptedException;
}

/**
 * Memoizer1
 *
 * <p>Initial cache attempt using HashMap and synchronization
 */
public class Memoizer1<A, V> implements Computable<A, V> {
  @GuardedBy("this")
  private final Map<A, V> cache = new HashMap<>();

  private final Computable<A, V> computable;

  public Memoizer1(Computable<A, V> computable) {
    this.computable = computable;
  }

  @Override
  public synchronized V compute(A arg) throws InterruptedException {
    V result = cache.get(arg);
    if (result == null) {
      result = computable.compute(arg);
      cache.put(arg, result);
    }
    return result;
  }
}

class ExpensiveFunction implements Computable<String, BigInteger> {

  @Override
  public BigInteger compute(String arg) throws InterruptedException {
    // after deep thought
    return new BigInteger(arg);
  }
}
