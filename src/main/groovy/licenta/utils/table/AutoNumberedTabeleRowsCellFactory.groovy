package licenta.utils.table

import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback

/**
 *  Created by Dragos on 2/15/2016.
 */
public class AutoNumberedTabeleRowsCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {

        return new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {

                super.updateItem(item, empty);

                if (this.getTableRow() != null && item != null) {
                    setText((this.getTableRow().getIndex() + 1) + "");
                } else {
                    setText("");
                }

            }
        };

    }
}
