package chapter6;

import java.util.concurrent.Executor;

/**
 * ThreadPerTaskExecutor
 *
 * <p>Executor that starts a new thread for each task
 */
public class ThreadPerTaskExecutor implements Executor {
  @Override
  public void execute(Runnable command) {
    new Thread(command).start();
  }
}
