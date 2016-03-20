package licenta

import com.sun.javafx.application.PlatformImpl
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import licenta.action.Action
import licenta.action.impl.FileDialogAction
import licenta.action.impl.LoadFXMLPanelAction
import licenta.algorithm.classification.impl.BackPropagationMethod
import licenta.algorithm.classification.impl.PerceptronMethod
import licenta.exception.BussinesException
import licenta.util.BlockUI
import licenta.util.Concurrency
import licenta.util.Util

import static licenta.util.BussinesConstants.*

/**
 * Created by Dragos on 19.02.2016.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(Main.class, args);

    }

    @Override
    void start(Stage primaryStage) throws Exception {

        FX_ELEM.each {
            it.metaClass.invokeMethod = { String name, args ->
                def m = delegate.metaClass.getMetaMethod(name, *args)
                def result = null
                def that = delegate
                //println "call $name from $it"
                PlatformImpl.runAndWait {
                    result = m.invoke(that, *args)
                }
                result
            }
        }

        Thread.setDefaultUncaughtExceptionHandler({ Thread t, Throwable e ->
             if (e instanceof BussinesException) {
                 def bussinesException = e;

                 Concurrency.callAsync {
                     def buttonType = Util.errorMessage(bussinesException.message).showAndWait();
                     if (buttonType != null) {
                         bussinesException.runAfterOk();
                     }
                 }.get();
             } else {
                 e.printStackTrace();
             }
         });


         BlockUI.execute = {

             def model = Action.getInstance().generalModel;
             def fxLoaderAction = new LoadFXMLPanelAction();
             def sileDialogAction = new FileDialogAction();
             def scene = new Scene(fxLoaderAction.loadFXML(getClass().getResource(MAIN_PANEL)));

             scene.with {
                 it.stylesheets.add getClass().getResource(BASE_CSS).toString();
             };

             model.with {
                 it.fxLoader = fxLoaderAction;
                 it.panel.children.add fxLoaderAction.loadFXML(getClass().getResource(TEST_PANEL));
                 it.perceptronClassification = new PerceptronMethod();
                 it.backPropagationClassification = new BackPropagationMethod();
                 it.fileDialog = sileDialogAction;
             };


             primaryStage.with {
                 it.scene = scene;
                 it.show();
             }
         }

    }
}
