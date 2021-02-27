package chapter7;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class LogService2 {
  private static final long TIMEOUT = 1;
  private static final TimeUnit UNIT = TimeUnit.SECONDS;

  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final PrintWriter writer;

  public LogService2(Writer writer) {
    this.writer = new PrintWriter(writer);
  }

  public void start() {}

  public void stop() throws InterruptedException {
    try {
      executor.shutdown();
      executor.awaitTermination(TIMEOUT, UNIT);
    } finally {
      writer.close();
    }
  }

  public void log(String message) {
    try {
      executor.execute(new WriteTask(message));
    } catch (RejectedExecutionException ignored) {
    }
  }

  private class WriteTask implements Runnable {
    private final String message;

    private WriteTask(String message) {
      this.message = message;
    }

    @Override
    public void run() {
      writer.println(message);
    }
  }
}
