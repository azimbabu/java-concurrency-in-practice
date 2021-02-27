package chapter11;

import annotations.ThreadSafe;

/**
 * StripedMap
 *
 * <p>Hash-based map using lock striping
 */
@ThreadSafe
public class StripedMap {
  private static final int NUM_LOCKS = 16;
  private final Node[] buckets;
  private final Object[] locks;

  public StripedMap(int numBuckets) {
    buckets = new Node[numBuckets];
    locks = new Object[NUM_LOCKS];
    for (int i = 0; i < NUM_LOCKS; i++) {
      locks[i] = new Object();
    }
  }

  public Object get(Object key) {
    int hash = hash(key);
    synchronized (locks[hash % NUM_LOCKS]) {
      for (Node current = buckets[hash]; current != null; current = current.next) {
        if (current.key.equals(key)) {
          return current.value;
        }
      }
    }
    return null;
  }

  private int hash(Object key) {
    return Math.abs(key.hashCode() % buckets.length);
  }

  public void clear() {
    for (int i = 0; i < buckets.length; i++) {
      synchronized (locks[i % NUM_LOCKS]) {
        buckets[i] = null;
      }
    }
  }

  private static class Node {
    Node next;
    Object key;
    Object value;
  }
}
