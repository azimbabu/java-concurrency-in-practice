package chapter7;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * ReaderThread
 *
 * <p>Encapsulating nonstandard cancellation in a Thread by overriding interrupt
 */
public class ReaderThread extends Thread {
  private static final int BUFFER_SIZE = 512;
  private final Socket socket;
  private final InputStream inputStream;

  public ReaderThread(Socket socket) throws IOException {
    this.socket = socket;
    this.inputStream = socket.getInputStream();
  }

  @Override
  public void interrupt() {
    try {
      socket.close();
    } catch (IOException ignored) {
    } finally {
      super.interrupt();
    }
  }

  @Override
  public void run() {
    try {
      byte[] buffer = new byte[BUFFER_SIZE];
      while (true) {
        int count = inputStream.read(buffer);
        if (count < 0) {
          break;
        } else if (count > 0) {
          processBuffer(buffer, count);
        }
      }
    } catch (IOException e) {
      /* Allow thread to exit */
    }
  }

  private void processBuffer(byte[] buffer, int count) {}
}
