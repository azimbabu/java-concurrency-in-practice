package chapter4;

import annotations.NotThreadSafe;

/**
 * MutablePoint
 *
 * <p>Mutable Point class similar to java.awt.Point
 */
@NotThreadSafe
public class MutablePoint {
  public int x;
  public int y;

  public MutablePoint() {
    x = 0;
    y = 0;
  }

  public MutablePoint(MutablePoint point) {
    this.x = point.x;
    this.y = point.y;
  }
}
