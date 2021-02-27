package chapter7;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * BrokenPrimeProducer
 * <p/>
 * Unreliable cancellation that can leave producers stuck in a blocking operation
 *
 */
public class BrokenPrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;
    private volatile boolean cancelled = false;

    public BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        BigInteger prime = BigInteger.ONE;
        try {
            while (!cancelled) {
                queue.put(prime.nextProbablePrime());
            }
        } catch (InterruptedException e) {

        }
    }

    public void cancel() {
        cancelled = true;
    }
}
