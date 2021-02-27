package chapter4;

import annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ListHelder
 *
 * <p>Example of thread-safe implementation of put-if-absent helper methods for List
 */
@ThreadSafe
public class GoodListHelper<E> {
  public List<E> list = Collections.synchronizedList(new ArrayList<>());

  public boolean putIfAbsent(E e) {
    synchronized (list) {
      boolean absent = !list.contains(e);
      if (absent) {
        list.add(e);
      }
      return absent;
    }
  }
}
