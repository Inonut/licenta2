package licenta.controller

import javafx.scene.control.ContextMenu
import licenta.action.Action
import licenta.domain.logic.GeneralModel
import licenta.exception.BussinesException
import licenta.util.BlockUI
import licenta.util.image.impl.ImageFormatter

/**
 * Created by Dragos on 20.02.2016.
 */
public class ContextMenuController implements Controller {
    public ContextMenu _cmCnv;

    private GeneralModel _model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        _model = Action.getInstance().generalModel;

        _model.contextMenuCmCnv = _cmCnv;
    }

    public void onBtnClearInput() {
        def cnvInput = _model.mainPanelCnvInput;

        cnvInput.graphicsContext2D.with {
            it.clearRect 0, 0, cnvInput.width, cnvInput.height;
        };
    }

    public void onBtnImport() {
        def cnvInput = _model.mainPanelCnvInput;

        def file = _model.fileDialog.getFileChooser();

        if (file != null) {
            def originalImage;

            BlockUI.execute = {
                originalImage = ImageFormatter.convertFileToImage(file);
                if (originalImage != null) {
                    def graphicsContext = cnvInput.graphicsContext2D;
                    def scaledImage = new ImageFormatter().scalingImage(originalImage, cnvInput.width, cnvInput.height);
                    graphicsContext.drawImage(scaledImage, 0, 0);
                } else {
                    throw new BussinesException("Fisierul incarcat nu este o imagine.");
                }
            };
        }
    }


}
