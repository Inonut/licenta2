package licenta.util.table

import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.value.ObservableValue
import javafx.scene.control.TableColumn
import javafx.util.Callback

/**
 * Created by Dragos on 2/15/2016.
 */
public class AutoNumberedTabeleRowsCellValueFactory<S, T> implements Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {

    @Override
    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {
        return new ReadOnlyObjectWrapper(param.getValue());
    }
}
