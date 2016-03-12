package licenta.domain.logic

import javafx.scene.canvas.Canvas
import javafx.scene.control.ContextMenu
import javafx.scene.control.TableView
import javafx.scene.layout.StackPane
import licenta.action.Action
import licenta.algorithm.classification.Classification

/**
 * Created by Dragos on 11.02.2016.
 */
public class GeneralModel {

    //private Scene primaryScene;
    //private Action mainPanel;
    private Canvas mainPanelCnvInput;
    private ContextMenu contextMenuCmCnv;
    private StackPane panel;
    private List<FileImage> trainData;
    private Action fxLoader;
    private Classification perceptronClassification;
    private Classification backPropagationClassification;
    private TableView<ResultTableView> mainPanelTableResult;
    private Action fileDialog

    public Canvas getMainPanelCnvInput() {
        return mainPanelCnvInput;
    }

    public void setMainPanelCnvInput(Canvas mainPanelCnvInput) {
        this.mainPanelCnvInput = mainPanelCnvInput;
    }

    public ContextMenu getContextMenuCmCnv() {
        return contextMenuCmCnv;
    }

    public void setContextMenuCmCnv(ContextMenu contextMenuCmCnv) {
        this.contextMenuCmCnv = contextMenuCmCnv;
    }

    public StackPane getPanel() {
        return panel;
    }

    public void setPanel(StackPane panel) {
        this.panel = panel;
    }

    public Action getFxLoader() {
        return fxLoader;
    }

    public void setFxLoader(Action fxLoader) {
        this.fxLoader = fxLoader;
    }

    public List<FileImage> getTrainData() {
        return trainData;
    }

    public void setTrainData(List<FileImage> trainData) {
        this.trainData = trainData;
    }

    public TableView<ResultTableView> getMainPanelTableResult() {
        return mainPanelTableResult;
    }

    public void setMainPanelTableResult(TableView<ResultTableView> mainPanelTableResult) {
        this.mainPanelTableResult = mainPanelTableResult;
    }

    Classification getPerceptronClassification() {
        return perceptronClassification
    }

    void setPerceptronClassification(Classification perceptronClassification) {
        this.perceptronClassification = perceptronClassification
    }

    Classification getBackPropagationClassification() {
        return backPropagationClassification
    }

    void setBackPropagationClassification(Classification backPropagationClassification) {
        this.backPropagationClassification = backPropagationClassification
    }

    Action getFileDialog() {
        return fileDialog
    }

    void setFileDialog(Action fileDialog) {
        this.fileDialog = fileDialog
    }
}
