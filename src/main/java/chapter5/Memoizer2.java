package chapter5;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Memoizer2<A, V> implements Computable<A, V> {
  private final Map<A, V> cache = new ConcurrentHashMap<>();
  private final Computable<A, V> computable;

  public Memoizer2(Computable<A, V> computable) {
    this.computable = computable;
  }

  @Override
  public V compute(A arg) throws InterruptedException {
    V result = cache.get(arg);
    if (result == null) {
      result = computable.compute(arg);
      cache.put(arg, result);
    }
    return result;
  }
}
