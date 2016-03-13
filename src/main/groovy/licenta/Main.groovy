package licenta

import com.sun.javafx.application.PlatformImpl
import javafx.application.Application
import javafx.stage.Stage
import licenta.util.ImageFormatter
import licenta.util.Util

import static licenta.util.BussinesConstants.FX_ELEM

/**
 * Created by Dragos on 19.02.2016.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(Main.class, args);

    }

    enum Type {
        RESPONSE, REQUEST
    }


    @Override
    void start(Stage primaryStage) throws Exception {

        FX_ELEM.each {
            it.metaClass.invokeMethod = { String name, args ->
                def m = delegate.metaClass.getMetaMethod(name, *args)
                def result = null
                def that = delegate
                PlatformImpl.runAndWait {
                    result = m.invoke(that, *args)
                }
                result
            }
        }

        Thread.start {
            try {
                def image = ImageFormatter.of(new File("C:\\Users\\Dragos\\Pictures\\Camera Roll\\Untitled1.png")).convertToGray()
                def file = new File("C:\\Users\\Dragos\\Desktop\\test.png");
                Util.writeImage(image, file)

                println "--------"
            } catch (Exception e) {
                e.printStackTrace()
            }

        }

        /*CountDownLatch doneLatch = new CountDownLatch(1);
        Thread.start {

            PlatformImpl.runLater({
                println "sdsdsdsd"
                doneLatch.countDown()
            })

        }

        Thread.start {
            doneLatch.await()
            println "----"
        }*/

        /* Thread.start {
             PlatformImpl.runAndWait({
                 println "sdsdsdsd"

             })
             println "----"
         }.join()*/

        /* Thread.setDefaultUncaughtExceptionHandler({ Thread t, Throwable e ->
             if (e instanceof BussinesException) {
                 def bussinesException = e;

                 def concurrency = new Concurrency();
                 concurrency.runNow = {
                     def buttonType = Util.errorMessage(bussinesException.message).showAndWait();
                     if (buttonType != null) {
                         bussinesException.runAfterOk();
                     }
                 };
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

             new Concurrency().runLater = {
                 primaryStage.with {
                     it.scene = scene;
                     it.show();
                 };
             }

         }*/

    }
}
