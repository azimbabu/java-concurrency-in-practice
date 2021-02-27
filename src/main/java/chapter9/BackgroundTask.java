package chapter9;

import java.util.concurrent.*;

/**
 * BackgroundTask
 *
 * <p>Background task class supporting cancellation, completion notification, and progress
 * notification
 */
public abstract class BackgroundTask<V> implements Runnable, Future<V> {
  private final FutureTask<V> computation = new Computation();

  protected void setProgress(final int current, final int max) {
    GuiExecutor.instance().execute(() -> onProgress(current, max));
  }

  protected void onProgress(int current, int max) {}

  // Called in the event thread
  protected void onCompletion(V value, Throwable thrown, boolean cancelled) {}

  // Called in the background thread
  protected abstract V compute();

  @Override
  public void run() {}

  // Other Future methods just forwarded to computation
  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return computation.cancel(mayInterruptIfRunning);
  }

  @Override
  public boolean isCancelled() {
    return computation.isCancelled();
  }

  @Override
  public boolean isDone() {
    return computation.isDone();
  }

  @Override
  public V get() throws InterruptedException, ExecutionException {
    return computation.get();
  }

  @Override
  public V get(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    return computation.get(timeout, unit);
  }

  private class Computation extends FutureTask<V> {

    public Computation() {
      super(BackgroundTask.this::compute);
    }

    @Override
    protected final void done() {
      GuiExecutor.instance()
          .execute(
              () -> {
                V value = null;
                Throwable thrown = null;
                boolean cancelled = false;

                try {
                  value = get();
                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                  thrown = e.getCause();
                } catch (CancellationException e) {
                  cancelled = true;
                } finally {
                  onCompletion(value, thrown, cancelled);
                }
              });
    }
  }
}
