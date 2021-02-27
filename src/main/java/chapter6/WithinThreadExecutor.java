package chapter6;

import java.util.concurrent.Executor;

/**
 * WithinThreadExecutor
 * <p/>
 * Executor that executes tasks synchronously in the calling thread
 *
 */
public class WithinThreadExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
