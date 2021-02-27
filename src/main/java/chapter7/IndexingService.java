package chapter7;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * IndexingService
 *
 * <p>Shutdown with poison pill
 */
public class IndexingService {
  public static final int CAPACITY = 1000;
  private static final File POISON = new File("");
  private final BlockingQueue<File> queue;
  private final File root;
  private final FileFilter filter;

  private final IndexerThread consumer = new IndexerThread();
  private final CrawlerThread producer = new CrawlerThread();

  public IndexingService(File root, FileFilter filter) {
    this.root = root;
    this.filter = file -> file.isDirectory() || filter.accept(file);
    this.queue = new LinkedBlockingQueue<>(CAPACITY);
  }

  public void start() {
    producer.start();
    consumer.start();
  }

  public void stop() {
    producer.interrupt();
  }

  public void awaitTermination() throws InterruptedException {
    consumer.join();
  }

  class CrawlerThread extends Thread {
    @Override
    public void run() {
      try {
        crawl(root);
      } catch (InterruptedException e) {
        /* fall through */
      } finally {
        while (true) {
          try {
            queue.put(POISON);
            break;
          } catch (InterruptedException ignored) {
            /* retry */
          }
        }
      }
    }

    private void crawl(File root) throws InterruptedException {
      File[] entries = root.listFiles(filter);
      if (entries != null) {
        for (File entry : entries) {
          if (entry.isDirectory()) {
            crawl(entry);
          } else if (!alreadyIndexed(entry)) {
            queue.put(entry);
          }
        }
      }
    }

    private boolean alreadyIndexed(File file) {
      return false;
    }
  }

  class IndexerThread extends Thread {
    @Override
    public void run() {
      try {
        while (true) {
          File file = queue.take();
          if (file == POISON) {
            break;
          } else {
            indexFile(file);
          }
        }
      } catch (InterruptedException consumed) {
      }
    }

    private void indexFile(File file) {
      /*...*/
    }
  }
}
