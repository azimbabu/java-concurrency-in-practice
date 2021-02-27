package chapter13;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TimedLocking
 *
 * <p>Locking with a time budget
 */
public class TimedLocking {
  private Lock lock = new ReentrantLock();

  public boolean trySendOnSharedLine(String message, long timeout, TimeUnit timeUnit)
      throws InterruptedException {
    long nanosToLock = timeUnit.toNanos(timeout) - estimatedNanosToSend(message);
    if (!lock.tryLock(nanosToLock, TimeUnit.NANOSECONDS)) {
      return false;
    }

    try {
      return sendOnSharedLine(message);
    } finally {
      lock.unlock();
    }
  }

  private boolean sendOnSharedLine(String message) {
    // send something
    return true;
  }

  private long estimatedNanosToSend(String message) {
    return message.length();
  }
}
