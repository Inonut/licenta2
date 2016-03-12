package licenta.action.impl

import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import licenta.action.Action

import static javafx.stage.FileChooser.ExtensionFilter
import static licenta.util.BussinesConstants.DEFAULT_DIR
import static licenta.util.BussinesConstants.IMAGES_TYPE

/**
 * Created by Dragos on 20.02.2016.
 */
public class FileDialogAction extends Action {

    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;

    public FileDialogAction() {

        this.fileChooser = new FileChooser();
        this.directoryChooser = new DirectoryChooser();

        def initDir = new File((getClass().getResource(DEFAULT_DIR) as URL).file);

        this.fileChooser.with {
            it.extensionFilters.addAll new ExtensionFilter("Image Files", IMAGES_TYPE), new ExtensionFilter("XML", "*.xml");
        };

        this.directoryChooser.initialDirectory = this.fileChooser.initialDirectory = !initDir.isDirectory() ? null : initDir;
    }

    @Override
    File getDirectoryChooser() throws IOException {
        return this.directoryChooser.showDialog(null);
    }

    @Override
    File getFileChooser() throws IOException {
        return this.fileChooser.showOpenDialog(null);
    }

    @Override
    File saveDirectoryChooser() throws IOException {
        return this.directoryChooser.showDialog(null)
    }

    @Override
    File saveFileChooser() throws IOException {
        return this.fileChooser.showSaveDialog(null)
    }
}
