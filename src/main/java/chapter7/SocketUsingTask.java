package chapter7;

import annotations.GuardedBy;
import annotations.ThreadSafe;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * SocketUsingTask
 *
 * <p>Encapsulating nonstandard cancellation in a task with newTaskFor
 */
public abstract class SocketUsingTask<T> implements CancellableTask<T> {
  @GuardedBy("this")
  private Socket socket;

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void cancel() {
    try {
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
    }
  }

  @Override
  public RunnableFuture<T> newTask() {
    return new FutureTask<>(this) {
      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        try {
          SocketUsingTask.this.cancel();
        } finally {
          return super.cancel(mayInterruptIfRunning);
        }
      }
    };
  }
}

interface CancellableTask<T> extends Callable<T> {
  void cancel();

  RunnableFuture<T> newTask();
}

@ThreadSafe
class CancellingExecutor extends ThreadPoolExecutor {

  public CancellingExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  public CancellingExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
  }

  public CancellingExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
  }

  public CancellingExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory,
      RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
  }

  @Override
  protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
    if (callable instanceof CancellableTask) {
        return ((CancellableTask<T>) callable).newTask();
    } else {
      return super.newTaskFor(callable);
    }
  }
}
