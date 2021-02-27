package chapter5;

import java.util.concurrent.BlockingQueue;

/**
 * TaskRunnable
 * <p/>
 * Restoring the interrupted status so as not to swallow the interrupt
 *
 */
public class TaskRunnable implements Runnable {
    private BlockingQueue<Task> queue;

    @Override
    public void run() {
        try {
            processTask(queue.take());
        } catch (InterruptedException e) {
            // restore interrupted status
            Thread.currentThread().interrupt();
        }
    }

    private void processTask(Task task) {
        // Handle the task
    }

    interface Task {
    }
}
