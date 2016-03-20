package licenta.controller

import javafx.scene.control.MenuBar
import javafx.scene.layout.StackPane
import licenta.action.Action
import licenta.domain.logic.GeneralModel
import licenta.util.ContextMenuPane

/**
 * Created by Dragos on 20.02.2016.
 */
public class MainController implements Controller {

    public ContextMenuPane _cnvContextMenu;
    public MenuBar _menuBar;
    public StackPane _panel;

    private GeneralModel _model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _model = Action.getInstance().generalModel;

        _model.panel = _panel;
    }
}
