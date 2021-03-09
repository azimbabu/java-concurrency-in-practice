package chapter14;

import annotations.GuardedBy;
import annotations.ThreadSafe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ConditionBoundedBuffer
 *
 * <p>Bounded buffer using explicit condition variables
 */
@ThreadSafe
public class ConditionBoundedBuffer<T> {
  private static final int BUFFER_SIZE = 100;
  protected final Lock lock = new ReentrantLock();
  // CONDITION PREDICATE: notFull (count < items.length)
  private final Condition notFull = lock.newCondition();
  // CONDITION PREDICATE: notEmpty (count > 0)
  private final Condition notEmpty = lock.newCondition();

  @GuardedBy("lock")
  private final T[] items = (T[]) new Object[BUFFER_SIZE];

  @GuardedBy("lock")
  private int head, tail, count;

  // BLOCKS-UNTIL: notFull
  public void put(T t) throws InterruptedException {
    lock.lock();
    try {
      while (count == items.length) {
        notFull.await();
      }

      items[tail] = t;
      if (++tail == items.length) {
        tail = 0;
      }

      ++count;
      notEmpty.signal();
    } finally {
      lock.unlock();
    }
  }

  // BLOCKS-UNTIL: notEmpty
  public T take() throws InterruptedException {
    lock.lock();
    try {
      while (count == 0) {
        notEmpty.await();
      }

      T t = items[head];
      items[head] = null;
      if (++head == items.length) {
        head = 0;
      }

      --count;
      notFull.signal();
      return t;
    } finally {
      lock.unlock();
    }
  }
}
