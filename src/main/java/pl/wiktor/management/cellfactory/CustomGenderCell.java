package pl.wiktor.management.cellfactory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import pl.wiktor.management.model.PatientBO;

public class CustomGenderCell extends TableCell<PatientBO, Boolean> {
    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            if (item) {
                setText("FEMALE");
            } else {
                setText("MALE");
            }
        }
    }

    public static Callback<TableColumn<PatientBO, Boolean>, TableCell<PatientBO, Boolean>> cellFactory =
            p -> new CustomGenderCell();
}
