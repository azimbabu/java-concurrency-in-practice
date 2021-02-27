package chapter4;

import annotations.Immutable;

/**
 * Point
 *
 * <p>Immutable Point class used by DelegatingVehicleTracker
 */
@Immutable
public class Point {
  public final int x;
  public final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
