package chapter5;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ProducerConsumer
 * <p/>
 * Producer and consumer tasks in a desktop search application
 *
 */
public class ProducerConsumer {

    public static final int BOUND = 10;
    private static final int NUM_CONSUMERS = Runtime.getRuntime().availableProcessors();

    static class FileCrawler implements Runnable {
        private final BlockingQueue<File> queue;
        private final FileFilter filter;
        private final File root;

        public FileCrawler(BlockingQueue<File> queue, FileFilter filter, File root) {
            this.queue = queue;
            this.root = root;
            this.filter = file -> file.isDirectory() || filter.accept(file);
        }

        @Override
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        private void crawl(File root) throws InterruptedException {
            File[] entries = root.listFiles(filter);
            if (entries == null) {
                return;
            }

            for (File entry : entries) {
                if (entry.isDirectory()) {
                    crawl(entry);
                } else if (!isAlreadyIndexed(entry)) {
                    queue.put(entry);
                }
            }
        }

        private boolean isAlreadyIndexed(File entry) {
            return false;
        }
    }

    static class Indexer implements Runnable {
        private final BlockingQueue<File> queue;

        public Indexer(BlockingQueue<File> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    indexFile(queue.take());
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        private void indexFile(File file) {
            // Index the file...
        }
    }

    public static void startIndexing(File[] roots) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(BOUND);
        FileFilter filter = file -> true;
        for (File root : roots) {
            new Thread(new FileCrawler(queue, filter, root)).start();
        }
        
        for (int i=0; i < NUM_CONSUMERS; i++) {
            new Thread(new Indexer(queue)).start();
        }
    }
}
