package licenta.controller

import javafx.embed.swing.SwingFXUtils
import javafx.event.ActionEvent
import javafx.scene.canvas.Canvas
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import licenta.action.Action
import licenta.domain.logic.GeneralModel
import licenta.util.BlockUI
import org.apache.commons.io.FilenameUtils

import javax.imageio.ImageIO

import static javafx.scene.input.MouseButton.PRIMARY
import static javafx.scene.input.MouseButton.SECONDARY

/**
 * Created by Dragos on 20.02.2016.
 */
public class TestController implements Controller {

    public Pane _pCnvInput;
    public Canvas _cnvInput;
    public TableView _tvOutput;

    private GeneralModel _model;
    public TextField _tfName ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        _model = Action.getInstance().generalModel;

        _model.with {
            it.mainPanelCnvInput = _cnvInput
            it.mainPanelTableResult = _tvOutput
        }

        _cnvInput.with {
            it.widthProperty().bind _pCnvInput.widthProperty();
            it.heightProperty().bind _pCnvInput.heightProperty();

            it.graphicsContext2D.lineWidth = 21;
        }
    }

    public void onMousePressedCnvInput(MouseEvent event) {
        _model.contextMenuCmCnv.hide();
        if (event.button == PRIMARY) {
            _cnvInput.graphicsContext2D.with {
                it.beginPath();
                it.moveTo(event.x, event.y);
                it.stroke();
            };
        }
    }

    public void onMouseDraggedCnvInput(MouseEvent event) {

        if (event.button == PRIMARY) {
            _cnvInput.graphicsContext2D.with {
                it.lineTo(event.x, event.y);
                it.stroke();
            };
        }
    }

    public void onMouseClickCnvInput(MouseEvent event) {
        if (event.button == SECONDARY) {
            _model.contextMenuCmCnv.show this._cnvInput, event.screenX, event.screenY;
        }
    }

    private int id;
    public void click_onSavePredef(ActionEvent actionEvent) {
        _tfName.text = _tfName.text.substring(0,_tfName.text.lastIndexOf('_'))+ "_${++Integer.parseInt(_tfName.text.substring(_tfName.text.lastIndexOf('_')+1))}"

        def file = new File("C:\\Users\\Dragos\\IdeaProjects\\licenta2\\src\\main\\resources\\images2\\${_tfName.getText()}.png")

        if (file != null) {
            def image;

            BlockUI.execute = {
                image = _model.mainPanelCnvInput.snapshot(null, null)
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), FilenameUtils.getExtension(file.absolutePath), file);
            };
        }
    }
}
