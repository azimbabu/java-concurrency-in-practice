package chapter7;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * InterruptBorrowedThread
 *
 * <p>
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TimedRun1 {
  private static final ScheduledExecutorService cancelExecutor =
      Executors.newScheduledThreadPool(1);

  public static void timedRun(Runnable runnable, long timeout, TimeUnit unit) {
    final Thread taskThread = Thread.currentThread();
    cancelExecutor.schedule(
        () -> {
          taskThread.interrupt();
        },
        timeout,
        unit);
    runnable.run();
  }
}
