package chapter6;

import utils.LaunderThrowable;

import java.util.List;
import java.util.concurrent.*;

/**
 * Renderer
 *
 * <p>Using CompletionService to render page elements as they become available
 */
public abstract class Renderer {
  private final ExecutorService executor;

  public Renderer(ExecutorService executor) {
    this.executor = executor;
  }

  public void renderPage(CharSequence source) {
    final List<ImageInfo> imageInfos = scanForImageInfo(source);
    CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executor);

    for (ImageInfo imageInfo : imageInfos) {
      completionService.submit(() -> imageInfo.downloadImage());
    }

    renderText(source);

    try {
      for (int i = 0, n = imageInfos.size(); i < n; i++) {
        Future<ImageData> future = completionService.take();
        ImageData imageData = future.get();
        renderImage(imageData);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (ExecutionException e) {
      throw LaunderThrowable.launderThrowable(e.getCause());
    }
  }

  protected abstract void renderImage(ImageData data);

  protected abstract List<ImageInfo> scanForImageInfo(CharSequence source);

  protected abstract void renderText(CharSequence source);

  interface ImageData {}

  interface ImageInfo {
    ImageData downloadImage();
  }
}
