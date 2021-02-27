package chapter5;

import utils.LaunderThrowable;

import java.util.concurrent.*;

/**
 * Memoizer
 * <p/>
 * Final implementation of Memoizer
 *
 */
public class Memoizer<A, V> implements Computable<A, V> {
    private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computable;

    public Memoizer(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        while (true) {
            Future<V> future = cache.get(arg);
            if (future == null) {
                Callable<V> eval = () -> computable.compute(arg);
                FutureTask<V> futureTask = new FutureTask<>(eval);
                future = cache.putIfAbsent(arg, futureTask);
                if (future == null) {
                    future = futureTask;
                    futureTask.run();
                }
            }

            try{
                return future.get();
            } catch (CancellationException e) {
                cache.remove(arg, future);
            } catch (ExecutionException e) {
                throw LaunderThrowable.launderThrowable(e.getCause());
            }
        }
    }
}
