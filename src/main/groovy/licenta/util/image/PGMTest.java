package licenta.util.image;

/**
 * Created by Dragos on 6/3/2016.
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.IIOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PGMTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Label root = new Label();
        Image image;

        long start = System.currentTimeMillis();
        DataInputStream input = new DataInputStream(getClass().getResourceAsStream("/house.l.pgm"));
        try {
            image = readImage(input);
        } finally {
            input.close();
        }
        System.out.printf("Read image (%f x %f) in: %d ms\n", image.getWidth(), image.getHeight(), System.currentTimeMillis() - start);

        root.setGraphic(new ImageView(image));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static Image readImage(final InputStream input) throws IOException {
        // First parse PGM header
        DataInputStream dataInputStream = new DataInputStream(input);
        PNMHeader header = PNMHeader.parse(dataInputStream);

        if(header == null){
            dataInputStream.close();
            return new Image(input);
        }

        WritableImage image = new WritableImage(header.getWidth(), header.getHeight());
        PixelWriter pixelWriter = image.getPixelWriter();

        int maxSample = header.getMaxSample(); // Needed for normalization

//        PixelFormat<ByteBuffer> gray = PixelFormat.createByteIndexedInstance(createGrayColorMap());

        byte[] rowBuffer = new byte[header.getWidth()];
        for (int y = 0; y < header.getHeight(); y++) {
            dataInputStream.readFully(rowBuffer); // Read one row

//            normalize(rowBuffer, maxSample);
//            pixelWriter.setPixels(0, y, width, 1, gray, rowBuffer, 0, width); // Gives weird NPE for me...

            // As I can't get setPixels to work, we'll set pixels directly
            // Performance is probably worse than setPixels, but it seems "ok"-ish
            for (int x = 0; x < rowBuffer.length; x++) {
                int gray = (rowBuffer[x] & 0xff) * 255 / maxSample; // Normalize [0...255]
                pixelWriter.setArgb(x, y, 0xff000000 | gray << 16 | gray << 8 | gray);
            }
        }

        dataInputStream.close();

        return image;
    }

    private int[] createGrayColorMap() {
        int[] colors = new int[256];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = 0xff000000 | i << 16 | i << 8 | i;
        }
        return colors;
    }

    /**
     * Simplified version of my PNMHeader parser
     */
    private static class PNMHeader {
        public static final int PGM = 'P' << 8 | '5';

        private final int width;
        private final int height;
        private final int maxSample;

        private PNMHeader(final int width, final int height, final int maxSample) {
            this.width = width;
            this.height = height;
            this.maxSample = maxSample;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getMaxSample() {
            return maxSample;
        }

        public static PNMHeader parse(final DataInputStream input) throws IOException {
            short type = input.readShort();

            if (type != PGM) {
                return null;
            }

            int width = 0;
            int height = 0;
            int maxSample = 0;

            while (width == 0 || height == 0 || maxSample == 0) {
                String line = input.readLine(); // For PGM I guess this is ok...

                if (line == null) {
                    throw new IIOException("Unexpeced end of stream");
                }

                if (line.indexOf('#') >= 0) {
                    // Skip comment
                    continue;
                }

                line = line.trim();

                if (!line.isEmpty()) {
                    // We have tokens...
                    String[] tokens = line.split("\\s");
                    for (String token : tokens) {
                        if (width == 0) {
                            width = Integer.parseInt(token);
                        } else if (height == 0) {
                            height = Integer.parseInt(token);
                        } else if (maxSample == 0) {
                            maxSample = Integer.parseInt(token);
                        } else {
                            throw new IIOException("Unknown PBM token: " + token);
                        }
                    }
                }
            }

            return new PNMHeader(width, height, maxSample);
        }
    }
}