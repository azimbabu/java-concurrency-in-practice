package chapter12;

import java.util.concurrent.atomic.AtomicInteger;

/** XorShift */
public class XorShift {
  static final AtomicInteger sequence = new AtomicInteger(8862213);
  int x = -1831433054;

  public XorShift(int seed) {
    this.x = x;
  }

  public XorShift() {
    this((int) System.nanoTime() + sequence.getAndAdd(129));
  }

  public int next() {
    x ^= x << 6;
    x ^= x >>> 21;
    x ^= (x << 7);
    return x;
  }
}
