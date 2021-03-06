package chapter15;

import annotations.Immutable;
import annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CasNumberRange
 *
 * <p>Preserving multivariable invariants using CAS
 */
@ThreadSafe
public class CasNUmberRange {
  private final AtomicReference<IntPair> values = new AtomicReference<>(new IntPair(0, 0));

  public int getLower() {
    return values.get().lower;
  }

  public void setLower(int i) {
    while (true) {
      IntPair oldValues = values.get();
      if (i > oldValues.upper) {
        throw new IllegalArgumentException("Can't set lower to " + i + " > upper");
      }
      IntPair newValues = new IntPair(i, oldValues.upper);
      if (values.compareAndSet(oldValues, newValues)) {
        return;
      }
    }
  }

  public int getUpper() {
    return values.get().upper;
  }

  public void setUpper(int i) {
    while (true) {
      IntPair oldValues = values.get();
      if (i < oldValues.lower) {
        throw new IllegalArgumentException("Can't set upper to " + i + " < lower");
      }
      IntPair newValues = new IntPair(oldValues.lower, i);
      if (values.compareAndSet(oldValues, newValues)) {
        return;
      }
    }
  }

  @Immutable
  private static class IntPair {
    final int lower; // Invariant: lower <= upper
    final int upper;

    public IntPair(int lower, int upper) {
      this.lower = lower;
      this.upper = upper;
    }
  }
}
