package chapter7;

import utils.LaunderThrowable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TimedRun2
 *
 * <p>Interrupting a task in a dedicated thread
 */
public class TimedRun2 {
  private static final ScheduledExecutorService cancelExecutor =
      Executors.newScheduledThreadPool(1);

  public static void timedRun(Runnable runnable, long timeout, TimeUnit unit)
      throws InterruptedException {
    class RethrowableTask implements Runnable {
      private volatile Throwable throwable;

      @Override
      public void run() {
        try {
          runnable.run();
        } catch (Throwable throwable) {
          this.throwable = throwable;
        }
      }

      void rethrow() {
        if (throwable != null) {
          LaunderThrowable.launderThrowable(throwable);
        }
      }
    }

    RethrowableTask task = new RethrowableTask();
    final Thread taskThread = new Thread(task);
    taskThread.start();
    cancelExecutor.schedule(
        () -> {
          taskThread.interrupt();
        },
        timeout,
        unit);
    taskThread.join(unit.toMillis(timeout));
    task.rethrow();
  }
}
