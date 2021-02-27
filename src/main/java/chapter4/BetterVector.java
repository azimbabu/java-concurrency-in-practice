package chapter4;

import annotations.ThreadSafe;

import java.util.Vector;

/**
 * BetterVector
 *
 * <p>Extending Vector to have a put-if-absent method
 */
@ThreadSafe
public class BetterVector<E> extends Vector<E> {
  // When extending a serializable class, you should redefine serialVersionUID
  private static final long serialVersionUID = 2649688117562432789L;

  public synchronized boolean putIfAbsent(E e) {
    boolean absent = !contains(e);
    if (absent) {
      add(e);
    }
    return absent;
  }
}
