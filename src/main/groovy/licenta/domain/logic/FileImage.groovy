package licenta.domain.logic

import javafx.scene.image.Image

/**
 * Created by Dragos on 10.02.2016.
 */
public class FileImage extends GenericObject implements Cloneable {

    private String name;
    private String typeImage;
    private File location;
    private Image originalImage;
    private Image grayImage;
    private Image semnificativeImage;
    private Image semnificativeScaledImage;
    private double[] semnificativeScaledImageTransformated;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(String typeImage) {
        this.typeImage = typeImage;
    }

    public File getLocation() {
        return location;
    }

    public void setLocation(File location) {
        this.location = location;
    }

    public Image getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    public double[] getSemnificativeScaledImageTransformated() {
        return semnificativeScaledImageTransformated;
    }

    public void setSemnificativeScaledImageTransformated(double[] semnificativeScaledImageTransformated) {
        this.semnificativeScaledImageTransformated = semnificativeScaledImageTransformated;
    }

    public Image getSemnificativeScaledImage() {
        return semnificativeScaledImage;
    }

    public void setSemnificativeScaledImage(Image semnificativeScaledImage) {
        this.semnificativeScaledImage = semnificativeScaledImage;
    }

    public Image getSemnificativeImage() {
        return semnificativeImage;
    }

    public void setSemnificativeImage(Image semnificativeImage) {
        this.semnificativeImage = semnificativeImage;
    }

    public Image getGrayImage() {
        return grayImage;
    }

    public void setGrayImage(Image grayImage) {
        this.grayImage = grayImage;
    }

    @Override
    public String toString() {
        return "" + typeImage + "_" + name + "_" + getId();
    }
}
