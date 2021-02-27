package chapter6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LifecycleWebServer
 * <p/>
 * Web server with shutdown support
 *
 */
public class LifecycleWebServer {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (!executorService.isShutdown()) {
            try {
                final Socket connection = socket.accept();
                executorService.execute(() -> handleRequest(connection));
            } catch (RejectedExecutionException e) {
                if (!executorService.isShutdown()) {
                    log("task submission rejected", e);
                }
            }
        }
    }

    public void stop() {
        executorService.shutdown();
    }

    private void log(String msg, Exception e) {
        Logger.getAnonymousLogger().log(Level.WARNING, msg, e);
    }

    private void handleRequest(Socket connection) {
        Request request = readRequest(connection);
        if (isShutdownRequest(request)) {
            stop();
        } else {
            dispatchRequest(request);
        }
    }

    private void dispatchRequest(Request request) {
    }

    private boolean isShutdownRequest(Request request) {
        return false;
    }

    interface Request {
    }

    private Request readRequest(Socket connection) {
        return null;
    }


}
