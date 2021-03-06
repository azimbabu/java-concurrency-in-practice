package chapter9;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

/**
 * SwingUtilities
 *
 * <p>Implementing SwingUtilities using an Executor
 */
public class SwingUtilities {
  private static final ExecutorService exec =
      Executors.newSingleThreadExecutor(new SwingThreadFactory());
  private static volatile Thread swingThread;

  public static boolean isEventDispatchThread() {
    return Thread.currentThread() == swingThread;
  }

  public static void invokeLater(Runnable task) {
    exec.execute(task);
  }

  public static void invokeAndWait(Runnable task)
      throws InvocationTargetException, InterruptedException {
    Future<?> future = exec.submit(task);
    try {
      future.get();
    } catch (ExecutionException e) {
      throw new InvocationTargetException(e);
    }
  }

  private static class SwingThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
      swingThread = new Thread(r);
      return swingThread;
    }
  }
}
