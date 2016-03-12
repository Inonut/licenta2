package licenta.utils

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

    private static final BlockUI instance = new BlockUI();

    private Stage dialogStage;
    private SimpleDoubleProperty progressProp;
    private int nrOfBlockUI;
    private Concurrency concurrency;

    private BlockUI() {

        this.concurrency = new Concurrency();

        this.concurrency.runNow = {
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
        };
    }

    public synchronized static void setExecute(Runnable run) {
        instance.concurrency.runNow = {
            instance.dialogStage.show();
        };
        instance.nrOfBlockUI++;

        instance.concurrency.execute = {
            try {
                run.run();
            } catch (any) {
                throw any;
            } finally {
                instance.concurrency.runNow = {
                    closeUI();
                };
            }
        };
    }


    private synchronized static void closeUI() {
        instance.nrOfBlockUI--;
        if (instance.nrOfBlockUI == 0) {
            instance.progressProp.set(-1f);
            instance.dialogStage.close();
        }
        if (instance.progressProp.get() == 1f) {
            instance.progressProp.set(-1f);
        }
    }

    public synchronized static void setProgress(double progress) {
        if (instance.nrOfBlockUI > 0 && instance.progressProp.get() < progress) {
            instance.concurrency.runLater = {
                instance.progressProp.set progress;
            };
        }
    }

}
