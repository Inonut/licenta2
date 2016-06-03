package licenta.util.image

import javafx.geometry.Rectangle2D
import javafx.scene.image.*
import licenta.util.BussinesConstants
import org.apache.commons.io.FilenameUtils

import javax.imageio.IIOException

/**
 * Created by Dragos on 14.03.2016.
 */
class ImageFormatter extends ImageView {

    private WritablePixelFormat pixelFormat

    synchronized static ImageFormatter of(Image image) {
        new ImageFormatter(image: image, smooth: true, cache: true, pixelFormat: PixelFormat.getIntArgbPreInstance())
    }

    synchronized static ImageFormatter of(File file) {

        if (!isImage(file)) {
            return;
        }

        def inputStream = new FileInputStream(file)
        def image = null
        try{
            image = PGMTest.readImage(inputStream)
        } catch (Exception e) {
            image = new Image(inputStream)
        }
        inputStream.close()

        of(image)
    }

    synchronized static isImage(File file) {
        file.isFile() && (BussinesConstants.IMAGES_TYPE as List).stream().anyMatch {
            it.endsWith(FilenameUtils.getExtension(file.getAbsolutePath()))
        }
    }

    synchronized ImageFormatter scalingImage(double width, double height) {

        this.fitWidth = width;
        this.fitHeight = height;
        this.image = this.snapshot(null, null);
        resetSettings()

        this
    }

    synchronized ImageFormatter rotate(int ungle) {

        this.rotate = ungle;
        this.image = this.snapshot(null, null);
        resetSettings()

        this
    }

    synchronized int[] convertImageToArray() {
        return convertImageToArray(null)
    }

    synchronized int[] convertImageToArray(Closure closure) {

        def width = image.getWidth() as int;
        def height = image.getHeight() as int;


        def pixels = new int[width * height];
        image.pixelReader.getPixels(0, 0, width, height, pixelFormat, pixels, 0, width);


        return pixels.collect { pixel -> closure ? closure(pixel) : pixel } as int[]
    }

    synchronized ImageFormatter convertToGray() {

        def width = image.getWidth() as int;
        def height = image.getHeight() as int;

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

        this.image = wImage

        this
    }

    public synchronized ImageFormatter getSemnificativeImage() {

        def imageMap = convertImageToPixelMap(image);
        def viewportRect = getBounds(imageMap);

        this.viewport = viewportRect;
        this.image = this.snapshot(null, null);
        resetSettings()

        this
    }

    private synchronized double[][] convertImageToPixelMap(Image image) {

        def width = image.getWidth() as int;
        def height = image.getHeight() as int;

        def map = new double[image.height][image.width];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                map[h][w] = image.getPixelReader().getArgb(w, h);
            }
        }

        return map;
    }

    private boolean vLine(int w, double[][] map) {
        for (double[] aMap : map) {
            if (aMap[w] != -1) {
                return true;
            }
        }
        return false;
    }

    private boolean hLine(int h, double[][] map) {
        for (int i = 0; i < map[0].length; i++) {
            if (map[h][i] != -1) {
                return true;
            }
        }
        return false;
    }

    private Rectangle2D getBounds(double[][] map) {
        def minX = 0;
        def minY = 0;
        def h = map.length - 1;
        def w = map[0].length - 1;

        for (; minX < h; minX++) {
            if (hLine(minX, map)) {
                break;
            }
        }

        for (; minX < h; h--) {
            if (hLine(h, map)) {
                break;
            }
        }

        for (; minY < w; minY++) {
            if (vLine(minY, map)) {
                break;
            }
        }

        for (; minY < w; w--) {
            if (vLine(w, map)) {
                break;
            }
        }

        return new Rectangle2D(minY, minX, w - minY + 1, h - minX + 1);
    }

    private void resetSettings() {
        this.viewport = null;
        this.rotate = 0;
        this.fitWidth = 0;
        this.fitHeight = 0;
    }

}
