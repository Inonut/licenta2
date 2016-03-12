package licenta.action

import javafx.scene.Parent
import licenta.domain.logic.GeneralModel
import licenta.exception.BussinesException

/**
 * Created by Dragos on 11.02.2016.
 */
public class Action {

    private static final Action instance = new Action();

    private GeneralModel generalModel;

    protected Action() {
        generalModel = new GeneralModel();
    }

    public GeneralModel getGeneralModel() {
        return generalModel;
    }

    public static Action getInstance() {
        return instance;
    }

    public Parent loadFXML(URL resource) throws IOException {
        throw new BussinesException("Not implemented method");
    }

    public File getDirectoryChooser() throws IOException {
        throw new BussinesException("Not implemented method");
    }

    public File getFileChooser() throws IOException {
        throw new BussinesException("Not implemented method");
    }

    public File saveFileChooser() throws IOException {
        throw new BussinesException("Not implemented method");
    }

    public File saveDirectoryChooser() throws IOException {
        throw new BussinesException("Not implemented method");
    }
}
