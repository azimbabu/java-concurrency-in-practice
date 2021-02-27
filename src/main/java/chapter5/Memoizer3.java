package chapter5;

import utils.LaunderThrowable;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Memoizer3
 * <p/>
 * Memoizing wrapper using FutureTask
 *
 */
public class Memoizer3<A, V> implements Computable<A, V> {
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computable;

    public Memoizer3(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        Future<V> future = cache.get(arg);
        if (future == null) {
            Callable<V> eval = () -> computable.compute(arg);
            FutureTask<V> futureTask = new FutureTask<>(eval);
            future = futureTask;
            cache.put(arg, future);
            futureTask.run();   // call to computable.compite happens here
        }

        try {
            return future.get();
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }
    }
}
