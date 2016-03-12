package licenta

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

        //def list = [1,2,3,4,5,10,12,122]

        //println list.collect{"${it}".padLeft(3)}

        launch(Main.class, args);
    }

    def decorateMethodsWithLogging(clazz) {
        def mc = clazz.metaClass
        mc.invokeMethod = { String methodName, args ->
            println "[" + new Date() + "] before $methodName, args = $args"
            def result = mc.getMetaMethod(methodName, args).invoke(delegate, args)
            println "[" + new Date() + "] after $methodName"
            return result
        }
    }

    @Override
    void start(Stage primaryStage) throws Exception {

        //decorateMethodsWithLogging(ImageFormatter.class)

        /*def file = new File("F:\\MEGA\\IdeaProjects\\licenta_v8.1.5\\src\\main\\resources\\asd")

        def list = Util.getImagesFrom(file.toPath(),null,-1)

        list.each {println it}

        Platform.exit();*/

        /* def imageAction = new ImageFormatter()
         def image = ImageFormatter.convertFileToImage(new File("F:\\MEGA\\IdeaProjects\\licenta_v8.1.5\\src\\main\\resources\\images\\cifra_2_19.png"))

         def fileImage = Util.prepareFileImage(image)

         def image45 = imageAction.rotate(fileImage.semnificativeScaledImage,45)
         def newFile = new File("C:\\Users\\Dragos\\Desktop\\result.png")
         ImageIO.write(SwingFXUtils.fromFXImage(image45, null), FilenameUtils.getExtension(newFile.absolutePath), newFile);
 */


        Thread.setDefaultUncaughtExceptionHandler({ Thread t, Throwable e ->
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

        }

        /*new Thread({
            execute = {

                def n = 1000000;
                for (int i=1;i<n;i++){
                    progress = i/n;
                    //println i;
                }
            };
        }).start();*/

        /*def list = [1,2,3,4];

        list.add("sddsds");

        list.each { println it };



        def out = System.out.&println
        out "Hello"*/

        /*def textField = new TextField();

        def panel = new StackPane();
        panel.getChildren().add(textField);

        def scene = new Scene(panel,500,500);
        primaryStage.setScene(scene);
        primaryStage.show();*/


    }
}
