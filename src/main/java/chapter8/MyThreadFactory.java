package chapter8;

import java.util.concurrent.ThreadFactory;

/**
 * MyThreadFactory
 * <p/>
 * Custom thread factory
 *
 */
public class MyThreadFactory implements ThreadFactory {
    private final String poolName;

    public MyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new MyAppThread(runnable, poolName);
    }
}
