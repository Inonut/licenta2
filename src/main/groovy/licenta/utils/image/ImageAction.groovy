package licenta.utils.image

import javafx.scene.image.Image

import static licenta.utils.BussinesConstants.IMAGES_TYPE;

/**
 * Created by Dragos on 10.02.2016.
 */
public trait ImageAction {
    public abstract synchronized Image scalingImage(Image image, double width, double height);

    public abstract synchronized double[][] convertImageToPixelMap(Image image);

    public abstract synchronized Image getSemnificativeImage(Image image);

    public abstract synchronized Image rotate(Image image, int ungle);

    public abstract synchronized double[] convertImageToArray(Image image);

    public abstract synchronized Image convertToGray(Image image);

    public static synchronized Image convertFileToImage(File file) {
        if (isImage(file)) {
            def inputStream = new FileInputStream(file);
            def image = new Image(inputStream);
            inputStream.close();
            return image;
        }

        return null;
    }

    public static synchronized boolean isImage(File file) {
        return IMAGES_TYPE.find { file.absolutePath.endsWith(it.substring(it.indexOf("."))) };
    }
}
