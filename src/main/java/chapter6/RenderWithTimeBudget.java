package chapter6;

import java.util.concurrent.*;

/**
 * RenderWithTimeBudget
 *
 * <p>Fetching an advertisement with a time budget
 */
public class RenderWithTimeBudget {

  public static final int TIME_BUDGET = 1000;
  public static final Ad DEFAULT_AD = new Ad();
  private static final ExecutorService executor = Executors.newCachedThreadPool();

  public Page renderPageWithAd() throws InterruptedException {
    long endNanos = System.nanoTime() + TIME_BUDGET;
    Future<Ad> future = executor.submit(new FetchAdTask());

    // Render the page while waiting for the ad
    Page page = renderPageBody();
    Ad ad;

    try {
      // Only wait for the remaining time budget
      long timeLeft = endNanos - System.nanoTime();
      ad = future.get(timeLeft, TimeUnit.NANOSECONDS);
    } catch (ExecutionException e) {
      ad = DEFAULT_AD;
    } catch (TimeoutException e) {
      ad = DEFAULT_AD;
      future.cancel(true);
    }

    page.setAd(ad);
    return page;
  }

  private Page renderPageBody() {
    return new Page();
  }

  private static class Ad {}

  private static class Page {
    public void setAd(Ad ad) {}
  }

  private static class FetchAdTask implements Callable<Ad> {

    @Override
    public Ad call() throws Exception {
      return new Ad();
    }
  }
}
