package chapter4;

import annotations.GuardedBy;
import annotations.ThreadSafe;

/** SafePoint */
@ThreadSafe
public class SafePoint {
  @GuardedBy("this")
  private int x;

  @GuardedBy("this")
  private int y;

  public SafePoint(SafePoint point) {
    this(point.get());
  }

  private SafePoint(int[] coordinates) {
    this(coordinates[0], coordinates[1]);
  }

  public SafePoint(int x, int y) {
    set(x, y);
  }

  public synchronized void set(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public synchronized int[] get() {
    return new int[] {x, y};
  }
}
