package chapter7;

import utils.LaunderThrowable;

import java.util.concurrent.*;

/**
 * TimedRun
 *
 * <p>Cancelling a task using Future
 */
public class TimedRun {
  private static final ExecutorService taskExecutor = Executors.newCachedThreadPool();

  public static void timedRun(Runnable runnable, long timeout, TimeUnit unit)
      throws InterruptedException {
    Future<?> future = taskExecutor.submit(runnable);
    try {
      future.get(timeout, unit);
    } catch (ExecutionException e) {
      // exception thrown in task; rethrow
      throw LaunderThrowable.launderThrowable(e.getCause());
    } catch (TimeoutException e) {
      // task will be cancelled below
    } finally {
      // Harmless if task already completed
      future.cancel(true); // interrupt if running
    }
  }
}
