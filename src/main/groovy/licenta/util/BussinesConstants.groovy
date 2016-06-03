package licenta.util

import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TableView
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import licenta.util.image.ImageFormatter

import static javafx.scene.control.ButtonBar.ButtonData.*

public class BussinesConstants {

    public static final String TYPE_FACE = "F";
    public static final String TYPE_DIGIT = "D";

    public static final double WIDTH = 50;
    public static final double HEIGHT = 50;

    public static final ButtonType BUTTON_YES = new ButtonType("Da", YES);
    public static final ButtonType BUTTON_NO = new ButtonType("Nu", NO); //ButtonBar.ButtonData.CANCEL_CLOSE
    public static final ButtonType BUTTON_CANCEL_CLOSE = new ButtonType("Am inteles", CANCEL_CLOSE);

    public static final String[] IMAGES_TYPE = ["*.png", "*.jpg", "*.pgm"];
    public static final String MAIN_PANEL = "/fxml/mainPanel.fxml";
    public static final String BASE_CSS = "/css/base.css";
    public static final String TEST_PANEL = "/fxml/testPanel.fxml";
    public static final String SETTINGS_PANEL = "/fxml/settingPanel.fxml";

    public static final String DEFAULT_DIR = "/";

    public static
    final List FX_ELEM = [Stage, Scene, Pane, VBox, SimpleDoubleProperty, ImageView, Alert, ImageFormatter, Node, Parent, StackPane, Region, Canvas, TableView]
}
