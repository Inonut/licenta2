package licenta.util

import javafx.scene.control.ContextMenu
import javafx.scene.layout.Pane;

/**
 * Created by Dragos on 13.02.2016.
 */
public class ContextMenuPane extends Pane {

    private ContextMenu contextMenu;

    public void setContextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

}