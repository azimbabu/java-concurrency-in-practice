package chapter7;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UEHLogger
 * <p/>
 * UncaughtExceptionHandler that logs the exception
 *
 */
public class UEHLogger implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger = Logger.getAnonymousLogger();
    logger.log(Level.SEVERE, "Thread terminated with exception: " + t.getName(), e);
        Deque<Character> stack = new ArrayDeque<>();
        stack.peek();
    }
}
