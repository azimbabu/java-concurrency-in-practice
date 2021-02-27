package chapter6;

import utils.LaunderThrowable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * FutureRenderer
 *
 * <p>Waiting for image download with \Future
 */
public abstract class FutureRenderer {
  private final ExecutorService executor = Executors.newCachedThreadPool();

  public void renderPage(CharSequence source) {
    List<ImageInfo> imageInfos = scanForImageInfo(source);
    Callable<List<ImageData>> task =
        () -> {
          List<ImageData> result = new ArrayList<>();
          for (ImageInfo imageInfo : imageInfos) {
            result.add(imageInfo.downloadImage());
          }
          return result;
        };

    Future<List<ImageData>> future = executor.submit(task);
    renderText(source);

    try {
      List<ImageData> imageDataList = future.get();
      for (ImageData imageData : imageDataList) {
        renderImage(imageData);
      }
    } catch (InterruptedException e) {
      // Re-assert the thread's interrupted status
      Thread.currentThread().interrupt();
      // We don't need the result, so cancel the task too
      future.cancel(true);
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
