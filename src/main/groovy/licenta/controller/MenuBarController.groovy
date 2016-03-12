package licenta.controller

import groovy.xml.MarkupBuilder
import javafx.embed.swing.SwingFXUtils
import javafx.event.ActionEvent
import licenta.action.Action
import licenta.algorithm.classification.impl.BackPropagationMethod
import licenta.algorithm.classification.impl.PerceptronMethod
import licenta.domain.logic.*
import licenta.exception.BussinesException
import licenta.util.BlockUI
import licenta.util.Concurrency
import licenta.util.Util
import org.apache.commons.io.FilenameUtils

import javax.imageio.ImageIO
import java.util.concurrent.CountDownLatch

import static licenta.util.BussinesConstants.SETTINGS_PANEL
import static licenta.util.BussinesConstants.TEST_PANEL

/**
 * Created by Dragos on 20.02.2016.
 */
public class MenuBarController implements Controller {

    private GeneralModel _model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _model = Action.getInstance().generalModel;
    }

    public void onSaveCurrentImage() {

        def file = _model.fileDialog.saveFileChooser()

        if (file != null) {
            def image;

            BlockUI.execute = {
                new Concurrency().runNow = { image = _model.mainPanelCnvInput.snapshot(null, null) };
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), FilenameUtils.getExtension(file.absolutePath), file);
            };
        }
    }

    public void onSaveCurrentScaledImage() {

        def file = _model.fileDialog.saveFileChooser();

        if (file != null) {
            def originalImage;

            BlockUI.execute = {
                new Concurrency().runNow = { originalImage = _model.mainPanelCnvInput.snapshot(null, null) };
                def fileImage = Util.prepareFileImage(originalImage)

                ImageIO.write(SwingFXUtils.fromFXImage(fileImage.semnificativeScaledImage, null), FilenameUtils.getExtension(file.absolutePath), file);
            };
        }
    }

    public void onShowTrainPanel() {
        BlockUI.execute = {
            new Concurrency().runNow = {
                _model.panel.children.clear();
                _model.panel.children.add(_model.getFxLoader().loadFXML(getClass().getResource(SETTINGS_PANEL)));
            }
        };

    }

    public void onShowTestPanel() {
        BlockUI.execute = {
            new Concurrency().runNow = {
                _model.panel.children.clear();
                _model.panel.children.add(_model.getFxLoader().loadFXML(getClass().getResource(TEST_PANEL)));
            }
        };
    }

    public void onSaveTrainDataImage() {

        def file = _model.fileDialog.saveDirectoryChooser();

        if (file != null) {

            BlockUI.execute = {
                _model.getTrainData().each { FileImage fileImage ->
                    def newFile = new File(file.absolutePath + "\\" + fileImage + ".png");
                    ImageIO.write(SwingFXUtils.fromFXImage(fileImage.semnificativeScaledImage, null), FilenameUtils.getExtension(newFile.absolutePath), newFile);
                }
            };
        }
    }

    public void onTestCurrentImage() throws BussinesException {
        BlockUI.execute = {
            if (_model.trainData != null) {
                def originalImage;
                def fileImage;

                new Concurrency().runNow = { originalImage = _model.mainPanelCnvInput.snapshot(null, null) };
                fileImage = Util.prepareFileImage(originalImage)

                def countDownLatch = new CountDownLatch(2);
                def classificationDataPerceptron = new ClassificationData();
                new Concurrency().execute = {
                    classificationDataPerceptron = _model.perceptronClassification.recognize(fileImage.semnificativeScaledImageTransformated);
                    countDownLatch.countDown();
                };

                def classificationDataBP = new ClassificationData();
                new Concurrency().execute = {
                    classificationDataBP = _model.backPropagationClassification.recognize(fileImage.semnificativeScaledImageTransformated);
                    countDownLatch.countDown();
                };
                countDownLatch.await();

                _model.mainPanelTableResult.items.add(new ResultTableView(1, classificationDataPerceptron.name, classificationDataBP.name));
            } else {
                throw new BussinesException("Trebuie antrenate datele intai").setAftarOk({ onShowTrainPanel() });
            }
        }

    }

    public void onTestAnotherImages() throws IOException {

        def file = _model.fileDialog.saveDirectoryChooser();

        BlockUI.execute = {
            if (file != null) {
                testMoreImages(Util.getImagesFrom(file.toPath(), null, -1));
            }
        };

    }

    public void onTestTrainImages() {
        testMoreImages(_model.getTrainData());
    }

    private void testMoreImages(List<FileImage> fileImages) {

        BlockUI.execute = {
            if (_model.trainData != null) {
                def procentPerceptron = 0;
                def prosentBP = 0;
                def nrOfTests = fileImages.size();
                for (FileImage fileImage : fileImages) {

                    def semnificativeScaledImageTransformated = fileImage.semnificativeScaledImageTransformated;

                    def countDownLatch = new CountDownLatch(2);
                    def classificationDataPerceptron = new ClassificationData();
                    new Concurrency().execute = {
                        classificationDataPerceptron = _model.perceptronClassification.recognize(semnificativeScaledImageTransformated);
                        countDownLatch.countDown();
                    };

                    def classificationDataBP = new ClassificationData();
                    new Concurrency().execute = {
                        classificationDataBP = _model.backPropagationClassification.recognize(semnificativeScaledImageTransformated);
                        countDownLatch.countDown();
                    };
                    countDownLatch.await();

                    if (fileImage.name.contains(classificationDataPerceptron.name)) {
                        procentPerceptron++;
                    }
                    if (fileImage.name.contains(classificationDataBP.name)) {
                        prosentBP++;
                    }
                }

                _model.mainPanelTableResult.items.add(new ResultTableView(nrOfTests, "" + ((double) procentPerceptron) / nrOfTests, "" + ((double) prosentBP) / nrOfTests));
            } else {
                throw new BussinesException("Trebuie antrenate datele intai").setAftarOk({ onShowTrainPanel() });
            }
        }
    }

    public void onSaveTrainNet(ActionEvent actionEvent) {


        BlockUI.execute = {
            if (_model.trainData != null) {

                def file;
                new Concurrency().runNow = {
                    file = _model.fileDialog.saveFileChooser();
                }

                if (file != null) {

                    def dataPerceptron = Action.getInstance().getGeneralModel().getPerceptronClassification().gettingData() as List<DataPerceptron>;
                    def dataBackPropagation = Action.getInstance().getGeneralModel().getBackPropagationClassification().gettingData();

                    def writer = new StringWriter()
                    def xml = new MarkupBuilder(writer)
                    xml.classification {
                        traintData {
                            dataPerceptron.collect { elem ->
                                "element" {
                                    "name" elem.getName()
                                    "data" {
                                        elem.getData().eachWithIndex { entry, i -> "id" entry as List }
                                    }
                                }
                            }
                        }
                        perceptron {
                            dataPerceptron.collect { elem ->
                                "element" {
                                    "name" elem.getName()
                                    "weight" elem.getWeight() as List
                                }
                            }
                        }
                        backPropagation {
                            dataBackPropagation["network"].collect { Layer[] layers ->
                                "layer" {
                                    layers.each { Layer layer ->
                                        "${layer.class.getName()}" {
                                            "error" layer.error
                                            "output" layer.output
                                            "weights" layer.weights as List
                                            if (layer instanceof Hidden) {
                                            }
                                            if (layer instanceof Output) {
                                                "target" layer.target
                                                "name" layer.name
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //println writer.toString()

                    def xmlParser = new XmlParser().parseText(writer.toString())
                    def pw = new PrintWriter(new FileOutputStream(file))
                    def printer = new XmlNodePrinter(pw)
                    printer.setPreserveWhitespace(true)
                    printer.print(xmlParser)
                    pw.close()

                }

            } else {
                throw new BussinesException("Trebuie antrenate datele intai").setAftarOk({
                    onShowTrainPanel()
                });
            }
        }
    }

    public void onImportTrainNet(ActionEvent actionEvent) {

        BlockUI.execute = {
            def file;
            new Concurrency().runNow = {
                file = _model.fileDialog.fileChooser;
            }

            if (file != null) {

                def trainData = new ArrayList<>()
                def perceptronData = new ArrayList()
                def backPropagationData = new HashMap()

                def xml = new XmlSlurper().parse(file)

                xml.traintData.element.each { elem ->
                    trainData.add(new ClassificationData(name: elem.name, data: elem.data.id.collect {
                        Util.toDouble(it.toString())
                    }))
                };


                perceptronData = trainData.collect { new DataPerceptron(name: it.name, data: it.data) }
                xml.perceptron.element.each { elem ->
                    perceptronData.find {
                        it.name.compareTo(elem.name.toString()) == 0
                    }.collect()[0].weight = Util.toDouble(elem.weight.toString())
                };

                Util.showTrainData(trainData[5].data[0] as List, 100)
                backPropagationData.trainData = trainData
                backPropagationData.network = xml.backPropagation.layer.collect {
                    it.children().collect {

                        def mapParam = [error  : it.error.toString() as double,
                                        output : it.output.toString() as double,
                                        weights: Util.toDouble(it.weights.toString()),
                                        target : it.target.toString() == "" ? null : it.target.toString() as double,
                                        name   : it.name.toString()]

                        switch (it.name()) {
                            case "licenta.domain.logic.Hidden": return new Hidden(mapParam)
                            case "licenta.domain.logic.Layer": return new Layer(mapParam)
                            case "licenta.domain.logic.Output": return new Output(mapParam)
                        }
                    }
                }


                Action.getInstance().getGeneralModel().setPerceptronClassification(new PerceptronMethod(data: perceptronData))
                Action.getInstance().getGeneralModel().setBackPropagationClassification(new BackPropagationMethod(data: trainData, layers: backPropagationData.network))
                Action.getInstance().getGeneralModel().setTrainData(trainData)
            }

        }
    }
}
