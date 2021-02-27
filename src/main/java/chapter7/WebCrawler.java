package chapter7;

import annotations.GuardedBy;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class WebCrawler {
  private static final long TIMEOUT = 500;
  private static final TimeUnit UNIT = TimeUnit.MILLISECONDS;
  @GuardedBy("this")
  private final Set<URL> urlsToCrawl = new HashSet<>();
  private final ConcurrentMap<URL, Boolean> seen = new ConcurrentHashMap<>();
  private volatile TrackingExecutor executor;

  public WebCrawler(URL startUrl) {
    urlsToCrawl.add(startUrl);
  }

  public synchronized void start() {
    executor = new TrackingExecutor(Executors.newCachedThreadPool());
    for (URL url : urlsToCrawl) {
      submitCrawlTask(url);
    }
    urlsToCrawl.clear();
  }

  public synchronized void stop() throws InterruptedException {
    try {
      saveUncrawled(executor.shutdownNow());
      if (executor.awaitTermination(TIMEOUT, UNIT)) {
        saveUncrawled(executor.getCancelledTasks());
      }
    } finally {
      executor = null;
    }
  }

  private void saveUncrawled(List<Runnable> uncrawled) {
    for (Runnable task : uncrawled) {
      urlsToCrawl.add(((CrawlTask) task).getPage());
    }
  }

  private void submitCrawlTask(URL url) {
    executor.execute(new CrawlTask(url));
  }

  protected abstract List<URL> processPage(URL url);

  private class CrawlTask implements Runnable {
    private final URL url;

    public CrawlTask(URL url) {
      this.url = url;
    }

    @Override
    public void run() {
      for (URL link : processPage(url)) {
        if (Thread.currentThread().isInterrupted()) {
          return;
        }
        submitCrawlTask(link);
      }
    }

    public URL getPage() {
      return url;
    }
  }
}
