package chapter6;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleThreadRenderer {

  void renderPage(CharSequence source) {
    renderText(source);

    List<ImageData> imageDataList = new ArrayList<>();
    for (ImageInfo imageInfo : scanForImageInfo(source)) {
      imageDataList.add(imageInfo.downloadImage());
    }

    for (ImageData imageData : imageDataList) {
      renderImage(imageData);
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
