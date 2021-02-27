package chapter7;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * CheckForMail
 *
 * <p>Using a private \Executor whose lifetime is bounded by a method call
 */
public class CheckForMail {

  public boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit)
      throws InterruptedException {
    ExecutorService executor = Executors.newCachedThreadPool();
    final AtomicBoolean hasNewMail = new AtomicBoolean(false);
    try {
      for (final String host : hosts) {
        executor.execute(
            () -> {
              if (checkMail(host)) {
                hasNewMail.set(true);
              }
            });
      }
    } finally {
      executor.shutdown();
      executor.awaitTermination(timeout, unit);
    }
    return hasNewMail.get();
  }

  private boolean checkMail(String host) {
    // Check for mail
    return false;
  }
}
