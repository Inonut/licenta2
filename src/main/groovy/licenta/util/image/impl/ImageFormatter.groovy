package licenta.util.image.impl

import javafx.geometry.Rectangle2D
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import licenta.util.Concurrency
import licenta.util.image.ImageAction

/**
 * Created by Dragos on 10.02.2016.
 */
public class ImageFormatter implements ImageAction {

    @Override
    public synchronized Image scalingImage(Image image, double width, double height) {
        def iv = new ImageView();
        iv.with {
            it.image = image;
            it.fitWidth = width;
            it.fitHeight = height;
            it.smooth = true;
            it.cache = true;
        };

        def scaledImage;
        new Concurrency().runNow = { scaledImage = iv.snapshot(null, null) };

        return scaledImage;
    }

    @Override
    public synchronized double[][] convertImageToPixelMap(Image image) {

        def map = new double[image.height][image.width];
        for (int h = 0; h < map.length; h++) {
            for (int w = 0; w < map[h].length; w++) {
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

    @Override
    public synchronized Image getSemnificativeImage(Image image) {

        def imageMap = convertImageToPixelMap(image);
        def viewportRect = getBounds(imageMap);
        def iv = new ImageView();
        iv.with {
            it.viewport = viewportRect;
            it.image = image;
            it.smooth = true;
            it.cache = true;
        };

        def scaledImage;
        new Concurrency().runNow = { scaledImage = iv.snapshot(null, null) };

        return scaledImage;
    }

    @Override
    public synchronized Image rotate(Image image, int ungle) {

        def iv = new ImageView();
        iv.with {
            it.image = image;
            it.rotate = ungle;
            it.smooth = true;
            it.cache = true;
        };

        def scaledImage;
        new Concurrency().runNow = { scaledImage = iv.snapshot(null, null) };

        return scaledImage;
    }


    @Override
    public synchronized double[] convertImageToArray(Image image) {

        def width = image.getWidth() as int;
        def height = image.getHeight() as int;

        def pixels = new int[width * height];
        def pixelFormat = PixelFormat.getIntArgbPreInstance();
        image.pixelReader.getPixels(0, 0, width, height, pixelFormat, pixels, 0, width);

        def newPixels = new double[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            newPixels[i] = ((pixels[i] & 0xFF) % 255) % 100;
        }

        return newPixels;
    }

    @Override
    public synchronized Image convertToGray(Image image) {

        def wImage = new WritableImage(image.width as int, image.height as int);
        def pixelWriter = wImage.pixelWriter;

        def width = image.width as int;
        def height = image.height as int;

        def pixels = new int[width * height];
        def pixelFormat = PixelFormat.getIntArgbPreInstance();
        image.pixelReader.getPixels(0, 0, width, height, pixelFormat, pixels, 0, width);

        def newPixels = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            def c = pixels[i];
            def a = c & 0xFF000000;
            def r = (c >> 16) & 0xFF;
            def g = (c >> 8) & 0xFF;
            def b = c & 0xFF;
            def gray = ((r + g + b) / 3) as int;
            newPixels[i] = a | (gray << 16) | (gray << 8) | gray;
        }

        pixelWriter.setPixels(0, 0, width, height, pixelFormat, newPixels, 0, width);

        return wImage;
    }


}
