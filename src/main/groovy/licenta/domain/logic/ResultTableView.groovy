package licenta.domain.logic

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

/**
 * Created by Dragos on 2/15/2016.
 */
public class ResultTableView {

    private SimpleStringProperty perceptronResult = new SimpleStringProperty();
    private SimpleStringProperty backPropagationResult = new SimpleStringProperty();
    private SimpleIntegerProperty nrOfClassesTested = new SimpleIntegerProperty();

    public ResultTableView() {
        this(0, "", "");
    }

    public ResultTableView(int nrOfClassesTested, String perceptronResult, String backPropagationResult) {
        setNrOfClassesTested(nrOfClassesTested);
        setPerceptronResult(perceptronResult);
        setBackPropagationResult(backPropagationResult);
    }

    public String getPerceptronResult() {
        return perceptronResult.get();
    }

    public SimpleStringProperty perceptronResultProperty() {
        return perceptronResult;
    }

    public void setPerceptronResult(String perceptronResult) {
        this.perceptronResult.set(perceptronResult);
    }

    public String getBackPropagationResult() {
        return backPropagationResult.get();
    }

    public SimpleStringProperty backPropagationResultProperty() {
        return backPropagationResult;
    }

    public void setBackPropagationResult(String backPropagationResult) {
        this.backPropagationResult.set(backPropagationResult);
    }

    public int getNrOfClassesTested() {
        return nrOfClassesTested.get();
    }

    public SimpleIntegerProperty nrOfClassesTestedProperty() {
        return nrOfClassesTested;
    }

    public void setNrOfClassesTested(int nrOfClassesTested) {
        this.nrOfClassesTested.set(nrOfClassesTested);
    }
}
