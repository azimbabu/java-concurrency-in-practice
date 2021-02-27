package chapter6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * TaskExecutionWebServer
 * <p/>
 * Web server using a thread pool
 *
 */
public class TaskExecutionWebServer {
    private static final int NUM_THREADS = 100;
    private static final Executor executor = Executors.newFixedThreadPool(NUM_THREADS);

  public static void main(String[] args) throws IOException {
      ServerSocket socket = new ServerSocket(80);
      while (true) {
          Socket connection = socket.accept();
          Runnable task = () -> handleRequest(connection);
          executor.execute(task);
      }
  }

    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }
}
