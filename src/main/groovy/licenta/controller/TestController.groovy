package licenta.controller

import javafx.scene.canvas.Canvas
import javafx.scene.control.TableView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import licenta.action.Action
import licenta.domain.logic.GeneralModel

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

            it.graphicsContext2D.lineWidth = 9;
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
}
