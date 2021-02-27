package chapter5;

import utils.LaunderThrowable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Preloader
 *
 * <p>Using FutureTask to preload data that is needed later
 */
public class Preloader {
  private final FutureTask<ProductInfo> future = new FutureTask<>(() -> loadProductInfo());
  private final Thread thread = new Thread(future);

  ProductInfo loadProductInfo() throws DataLoadException {
    return null;
  }

  public void start() {
    thread.start();
  }

  public ProductInfo get() throws InterruptedException, DataLoadException {
    try {
      return future.get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof DataLoadException) {
        throw (DataLoadException) cause;
      } else {
        throw LaunderThrowable.launderThrowable(cause);
      }
    }
  }

  interface ProductInfo {}
}

class DataLoadException extends Exception {}
