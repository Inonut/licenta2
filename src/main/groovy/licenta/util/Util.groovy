package licenta.util

import javafx.embed.swing.SwingFXUtils
import javafx.scene.control.Alert
import javafx.scene.image.Image
import org.apache.commons.io.FilenameUtils

import javax.imageio.ImageIO

import static javafx.scene.control.Alert.AlertType.CONFIRMATION
import static javafx.scene.control.Alert.AlertType.ERROR
import static licenta.utils.BussinesConstants.*

/**
 * Created by Dragos on 12.03.2016.
 */
public class Util {

    public synchronized static Alert confirmMessage(String headerText) {

        return new Alert(CONFIRMATION).with {
            it.headerText = headerText
            it.buttonTypes.clear()
            it.buttonTypes.addAll BUTTON_YES, BUTTON_NO
            return it
        }
    }

    public synchronized static Alert errorMessage(String headerText) {

        return new Alert(ERROR).with {
            it.headerText = headerText;
            it.buttonTypes.clear();
            it.buttonTypes.addAll BUTTON_CANCEL_CLOSE;
            return it
        };
    }

    public synchronized static void writeImage(Image image, File file) {
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), FilenameUtils.getExtension(file.absolutePath), file);
    }

    public synchronized static void writeArray() {

    }

}
