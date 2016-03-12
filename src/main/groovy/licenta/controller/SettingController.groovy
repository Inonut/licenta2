package licenta.controller

import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import licenta.action.Action
import licenta.algorithm.classification.impl.PerceptronMethod
import licenta.domain.logic.BackPropagationSettings
import licenta.domain.logic.GeneralModel
import licenta.domain.logic.PerceptronSettings
import licenta.domain.logic.Settings
import licenta.exception.BussinesException
import licenta.util.BlockUI
import licenta.util.Concurrency
import licenta.util.Util

import java.nio.file.Paths
import java.util.concurrent.CountDownLatch

import static licenta.util.BussinesConstants.TEST_PANEL;

/**
 * Created by Dragos on 20.02.2016.
 */
public class SettingController implements Controller {

    public RadioButton _rbPathDB;
    public CheckBox _cbFace;
    public CheckBox _cbDigit;
    public ChoiceBox _cbNbClasses;
    public TextField _tfEpochsPerceptron;
    public TextField _tfEpochsBP;
    public TextField _tfHidden1BP;
    public TextField _tfHidden2BP;
    public TextField _tfErrorBP;

    private GeneralModel _model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _model = Action.getInstance().generalModel;

    }

    public void onBtnStartTrain() {

        BlockUI.execute = {

            /*** validari ***/
            def epochPerceptron;
            def epocsBP;
            def hidden1BP;
            def hidden2BP;
            def pathDB;
            def errorBP;
            def category;
            def nbClasses;

            category = new ArrayList<String>();
            if (_cbDigit.selected) {
                category.add(_cbDigit.getText().toUpperCase());
            }
            if (_cbFace.selected) {
                category.add(_cbFace.getText().toUpperCase());
            }
            if (category.size() == 0) {
                throw new BussinesException("Nu este selectata nici o categorie.");
            }

            try {
                epochPerceptron = Integer.parseInt(_tfEpochsPerceptron.text);
            } catch (Exception e) {
                throw new BussinesException("Numarul de epoci pentru perceptron este invalid.");
            }

            try {
                epocsBP = Integer.parseInt(_tfEpochsBP.text);
            } catch (Exception e) {
                throw new BussinesException("Numarul de epoci pentru back propagation este invalid.");
            }

            try {
                hidden1BP = Integer.parseInt(_tfHidden1BP.text);
            } catch (Exception e) {
                throw new BussinesException("Numarul de noduri pentru stratul ascuns 1 este invalid.");
            }

            try {
                hidden2BP = Integer.parseInt(_tfHidden2BP.text);
            } catch (Exception e) {
                throw new BussinesException("Numarul de noduri pentru stratul ascuns 2 este invalid.");
            }

            try {
                errorBP = Double.parseDouble(_tfErrorBP.text);
            } catch (Exception e) {
                throw new BussinesException("Eroarea pentru back propagation este invalida.");
            }

            if (!_rbPathDB.selected) {
                throw new BussinesException("Nu este selectata nici o cale la baza de date.");
            }
            pathDB = _rbPathDB.text;

            nbClasses = Integer.parseInt(_cbNbClasses.value.toString());

            /* citire date din director */

            _model.trainData = []
            if (_rbPathDB.selected) {
                print "unghi 0"
                _model.trainData += Util.getImagesFrom(Paths.get(pathDB), category, nbClasses, 0)
                /*print "unghi 45"
                _model.trainData += Util.getImagesFrom(Paths.get(pathDB),category,nbClasses,45)
                print "unghi 90"
                _model.trainData += Util.getImagesFrom(Paths.get(pathDB),category,nbClasses,90)
                print "unghi 135"
                _model.trainData += Util.getImagesFrom(Paths.get(pathDB),category,nbClasses,135)
                print "unghi 180"
                _model.trainData += Util.getImagesFrom(Paths.get(pathDB),category,nbClasses,180)
                print "unghi 225"
                _model.trainData += Util.getImagesFrom(Paths.get(pathDB),category,nbClasses,225)
                print "unghi 270"
                _model.trainData += Util.getImagesFrom(Paths.get(pathDB),category,nbClasses,270)
                print "unghi 315"
                _model.trainData += Util.getImagesFrom(Paths.get(pathDB),category,nbClasses,315)*/
            }

            def classificationDatas = PerceptronMethod.prepareData(_model.trainData);
            def settings = new Settings();
            def perceptronSettings = new PerceptronSettings();
            def backPropagationSettings = new BackPropagationSettings();

            perceptronSettings.with {
                it.epochs = epochPerceptron;
            };

            backPropagationSettings.with {
                it.epochs = epocsBP;
                it.error = errorBP;
                it.hidden = [hidden1BP, hidden2BP] as int[];
            };

            settings.with {
                it.backPropagationSettings = backPropagationSettings;
                it.perceptrinSettings = perceptronSettings;
            };


            def countDownLatch = new CountDownLatch(2);
            new Concurrency().execute = {
                _model.perceptronClassification.settingData(classificationDatas);
                _model.perceptronClassification.train(settings);
                countDownLatch.countDown();
            };

            new Concurrency().execute = {
                _model.backPropagationClassification.settingData(classificationDatas);
                _model.backPropagationClassification.train(settings);
                countDownLatch.countDown();
            };

            countDownLatch.await();

            new Concurrency().runNow = {
                _model.panel.children.clear();
                _model.panel.children.add(_model.getFxLoader().loadFXML(getClass().getResource(TEST_PANEL)));
            };
        };

    }

    public void onRbPathDB() {

        def file = _model.fileDialog.getDirectoryChooser();

        if (file != null) {
            _rbPathDB.text = file.absolutePath;
            _rbPathDB.selected = true;
        } else {
            _rbPathDB.selected = false;
        }
    }
}
