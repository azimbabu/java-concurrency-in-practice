package chapter6;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * OutOfTime
 *
 * <p>Class illustrating confusing Timer behavior
 */
public class OutOfTime {
  public static void main(String[] args) throws InterruptedException {
    Timer timer = new Timer();
    timer.schedule(new ThrowTask(), 1);
    TimeUnit.SECONDS.sleep(1);
    timer.schedule(new ThrowTask(), 1);
    TimeUnit.SECONDS.sleep(5);
  }

  private static class ThrowTask extends TimerTask {

    @Override
    public void run() {
      throw new RuntimeException();
    }
  }
}
