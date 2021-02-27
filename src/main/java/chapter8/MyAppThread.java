package chapter8;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MyAppThread
 * <p/>
 * Custom thread base class
 *
 */
public class MyAppThread extends Thread {
    private static final AtomicInteger created = new AtomicInteger();
    private static final AtomicInteger alive = new AtomicInteger();
    private static final Logger log = Logger.getAnonymousLogger();
    private static final String DEFAULT_NAME = "MyAppThread";
    private static volatile boolean debugLifeCycle = false;

    public MyAppThread(Runnable runnable) {
        this(runnable, DEFAULT_NAME);
    }

    public MyAppThread(Runnable runnable, String name) {
        super(runnable, name + "-" + created.incrementAndGet());
        setUncaughtExceptionHandler((t, e) -> log.log(Level.SEVERE, "UNCAUGHT in thread " + t.getName(), e));
    }

    @Override
    public void run() {
        // Copy debug flag to ensure consistent value throughout.
        boolean debug = debugLifeCycle;
        if (debug) {
            log.log(Level.FINE, "Created " + getName());
        }

        try {
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            if (debug) {
                log.log(Level.FINE, "Exiting " + getName());
            }
        }
    }

    public static int getThreadsCreated() {
        return created.get();
    }

    public static int getThreadsAlive() {
        return alive.get();
    }

    public static boolean getDebug() {
        return debugLifeCycle;
    }

    public static void setDebug(boolean debug) {
        debugLifeCycle = debug;
    }
}
