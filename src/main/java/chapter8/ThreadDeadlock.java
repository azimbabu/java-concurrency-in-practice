package chapter8;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ThreadDeadlock
 *
 * <p>Task that deadlocks in a single-threaded Executor
 */
public class ThreadDeadlock {
  ExecutorService executor = Executors.newSingleThreadExecutor();

  public static void main(String[] args) {
    ThreadDeadlock threadDeadlock = new ThreadDeadlock();
    threadDeadlock.executor.submit(threadDeadlock.new RenderPageTask());
  }

  public class LoadFileTask implements Callable<String> {
    private final String fileName;

    public LoadFileTask(String fileName) {
      this.fileName = fileName;
    }

    @Override
    public String call() throws Exception {
      // Here's where we would actually read the file
      return "";
    }
  }

  public class RenderPageTask implements Callable<String> {

    @Override
    public String call() throws Exception {
      Future<String> header = executor.submit(new LoadFileTask("header.html"));
      Future<String> footer = executor.submit(new LoadFileTask("footer.html"));
      String page = renderBody();
      // Will deadlock -- task waiting for result of subtask
      return header.get() + page + footer.get();
    }

    private String renderBody() {
      // Here's where we would actually render the page
      return "";
    }
  }
}
