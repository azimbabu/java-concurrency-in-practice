package chapter7;

import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class TrackingExecutor extends AbstractExecutorService {
  private final ExecutorService executor;
  private final Set<Runnable> tasksCancelledAtShutdown =
      Collections.synchronizedSet(new HashSet<>());

  public TrackingExecutor(ExecutorService executor) {
    this.executor = executor;
  }

  @Override
  public void shutdown() {
    executor.shutdown();
  }

  @Override
  public List<Runnable> shutdownNow() {
    return executor.shutdownNow();
  }

  @Override
  public boolean isShutdown() {
    return executor.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return executor.isTerminated();
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    return executor.awaitTermination(timeout, unit);
  }

  @Override
  public void execute(Runnable runnable) {
    executor.execute(
        () -> {
          try {
            runnable.run();
          } finally {
            if (isShutdown() && Thread.currentThread().isInterrupted()) {
              tasksCancelledAtShutdown.add(runnable);
            }
          }
        });
  }

  public List<Runnable> getCancelledTasks() {
    if (!executor.isTerminated()) {
      throw new IllegalStateException(/*...*/ );
    }
    return new ArrayList<>(tasksCancelledAtShutdown);
  }
}
