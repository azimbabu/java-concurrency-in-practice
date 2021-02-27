package chapter9;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * GuiExecutor
 *
 * <p>Executor built atop SwingUtilities
 */
public class GuiExecutor extends AbstractExecutorService {
  // Singletons have a private constructor and a public factory
  private static final GuiExecutor instance = new GuiExecutor();

  private GuiExecutor() {}

  public static GuiExecutor instance() {
    return instance;
  }

  @Override
  public void shutdown() {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Runnable> shutdownNow() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isShutdown() {
    return false;
  }

  @Override
  public boolean isTerminated() {
    return false;
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void execute(Runnable runnable) {
    if (SwingUtilities.isEventDispatchThread()) {
      runnable.run();
    } else {
      SwingUtilities.invokeLater(runnable);
    }
  }
}
