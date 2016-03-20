package licenta.action.impl

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.util.Pair
import licenta.action.Action

/**
 * Created by Dragos on 11.02.2016.
 */
public class LoadFXMLPanelAction extends Action {

    private Map<URL, Pair<Parent, FXMLLoader>> map;

    public LoadFXMLPanelAction() {
        map = new HashMap<>();
    }

    @Override
    public Parent loadFXML(URL resource) throws IOException {

        if (map.containsKey(resource)) {
            return map.get(resource).key;
        } else {
            def fxLoad = new FXMLLoader(resource);
            def parent = fxLoad.load();
            def controller = fxLoad.controller
            map.put(resource, new Pair<>(parent, controller));
            return parent;
        }

    }
}
