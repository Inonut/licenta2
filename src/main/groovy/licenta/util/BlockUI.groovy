package licenta.util

import com.sun.javafx.stage.StageHelper
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.Scene
import javafx.scene.control.ProgressIndicator
import javafx.scene.effect.GaussianBlur
import javafx.scene.layout.VBox
import javafx.stage.Stage

import static javafx.geometry.Pos.CENTER
import static javafx.stage.Modality.APPLICATION_MODAL
import static javafx.stage.StageStyle.UNDECORATED

/**
 * Created by Dragos on 08.02.2016.
 */
public class BlockUI {

    private static final BlockUI INSTANCE = new BlockUI()

    private Stage dialogStage
    private SimpleDoubleProperty progressProp
    private int nrOfBlockUI

    private BlockUI() {
        this.nrOfBlockUI = 0;
        this.progressProp = new SimpleDoubleProperty(-1f);
        this.dialogStage = new Stage();


        def pin = new ProgressIndicator();
        def vb = new VBox();
        def scene = new Scene(vb, 100, 100);

        dialogStage.with {
            it.initStyle UNDECORATED;
            it.resizable = false;
            it.initModality APPLICATION_MODAL;
            it.scene = scene;
            it.onShown = {
                def blurEffect = new GaussianBlur(7);
                StageHelper.getStages().stream().findAny().ifPresent({ stage -> stage.getScene().getRoot().setEffect(blurEffect) });
            }
            it.onHidden = {
                def blurEffect = new GaussianBlur(0);
                StageHelper.getStages().stream().findAny().ifPresent({ stage -> stage.getScene().getRoot().setEffect(blurEffect) });
            }
        }

        vb.with {
            it.spacing = 5;
            it.alignment = CENTER;
            it.children.addAll pin;
        }

        pin.with {
            it.progress = -1F;
            it.progressProperty().bind progressProp;
        }
    }

    public synchronized static void setExecute(Closure closure) {
        INSTANCE.dialogStage.show()

        Concurrency.callAsync {
            INSTANCE.nrOfBlockUI++;
            try {
                closure();
            } catch (any) {
                throw any;
            } finally {
                closeUI()
            }
        }
    }


    private synchronized static void closeUI() {
        INSTANCE.nrOfBlockUI--;
        if (INSTANCE.nrOfBlockUI == 0) {
            INSTANCE.progressProp.set(-1f);
            INSTANCE.dialogStage.close();
        }
        if (INSTANCE.progressProp.get() == 1f) {
            INSTANCE.progressProp.set(-1f);
        }
    }

    public synchronized static void setProgress(double progress) {
        if (INSTANCE.nrOfBlockUI > 0 && INSTANCE.progressProp.get() < progress) {
            INSTANCE.progressProp.set progress
        }
    }

}
