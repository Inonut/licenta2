package licenta.util

import javafx.beans.NamedArg
import javafx.scene.image.*

/**
 * Created by Dragos on 13.03.2016.
 */
class ImageFormatter extends Image {

    private ImageView iv
    private WritablePixelFormat pixelFormat
    private int width
    private int height
    private int[] pixels

    ImageFormatter(@NamedArg("is") InputStream is) {
        super(is)
    }

    synchronized static of(File file) {
        def inputStream = new FileInputStream(file)
        def imageFormatter = new ImageFormatter(inputStream)
        inputStream.close()

        imageFormatter.iv = new ImageView()
        imageFormatter.pixelFormat = PixelFormat.getIntArgbPreInstance()
        imageFormatter.width = imageFormatter.getWidth() as int
        imageFormatter.height = imageFormatter.getHeight() as int

        return imageFormatter
    }

    public Image scalingImage(double width, double height) {
        iv.with {
            it.image = this;
            it.fitWidth = width;
            it.fitHeight = height;
            it.smooth = true;
            it.cache = true;
        };

        return iv.snapshot(null, null);
    }


    public Image rotate(int ungle) {

        iv.with {
            it.image = this;
            it.rotate = ungle;
            it.smooth = true;
            it.cache = true;
        };

        return iv.snapshot(null, null);
    }


    public synchronized Image convertToGray() {

        def wImage = new WritableImage(width, height);
        def pixelWriter = wImage.pixelWriter;

        def pixels = convertImageToArray()

        def newPixels = new int[pixels.length];
        pixels.eachWithIndex { int c, int i ->
            int a = c & 0xFF000000;
            int r = (c >> 16) & 0xFF;
            int g = (c >> 8) & 0xFF;
            int b = c & 0xFF;
            int gray = ((r + g + b) / 3);
            newPixels[i] = a | (gray << 16) | (gray << 8) | gray;
        }

        pixelWriter.setPixels(0, 0, width, height, pixelFormat, newPixels, 0, width);

        return wImage;
    }

    public synchronized int[] convertImageToArray() {
        return convertImageToArray(null)
    }

    public synchronized int[] convertImageToArray(Closure closure) {

        if (pixels == null) {
            pixels = new int[width * height];
            this.pixelReader.getPixels(0, 0, width, height, pixelFormat, pixels, 0, width);
        }

        return pixels.each { pixel -> closure ? closure(pixel) : pixel }.collect() as int[]
    }

}
