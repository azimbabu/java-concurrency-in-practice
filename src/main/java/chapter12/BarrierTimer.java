package chapter12;

/**
 * BarrierTimer
 * <p/>
 * Barrier-based timer
 *
 */
public class BarrierTimer implements Runnable {
    private boolean started;
    private long startTime, endTime;

    @Override
    public synchronized void run() {
        long time = System.nanoTime();
        if (!started) {
            started = true;
            startTime = time;
        } else {
            endTime = time;
        }
    }

    public synchronized void clear() {
        started = false;
    }

    public synchronized long getTime() {
        return endTime - startTime;
    }
}
