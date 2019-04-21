package pl.wiktor.management.cellfactory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import pl.wiktor.management.model.ExaminationBO;
import pl.wiktor.management.model.enums.ExaminationStatusEnum;

public class CustomStatusCell extends TableCell<ExaminationBO, String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            if (item.equals(ExaminationStatusEnum.REGISTERED.getDisplayName())) {
                setStyle("-fx-background-color: #c9ff57; -fx-text-fill: black");
                setText(ExaminationStatusEnum.REGISTERED.getDisplayName());

            } else if (item.equals(ExaminationStatusEnum.REQUESTED.getDisplayName())) {
                setStyle("-fx-background-color: #25ff3f; -fx-text-fill: black");
                setText(ExaminationStatusEnum.REQUESTED.getDisplayName());

            } else if (item.equals(ExaminationStatusEnum.IN_PROGRESS.getDisplayName())) {
                setStyle("-fx-background-color: #004a00; -fx-text-fill: white");
                setText(ExaminationStatusEnum.IN_PROGRESS.getDisplayName());

            } else if (item.equals(ExaminationStatusEnum.FOR_DESCRIPTION.getDisplayName())) {
                setStyle("-fx-background-color: #ff8ee4; -fx-text-fill: black");
                setText(ExaminationStatusEnum.FOR_DESCRIPTION.getDisplayName());

            } else if (item.equals(ExaminationStatusEnum.DONE.getDisplayName())) {
                setStyle("-fx-background-color: #ff0200; -fx-text-fill: white");
                setText(ExaminationStatusEnum.DONE.getDisplayName());
            }
        }
    }

    public static Callback<TableColumn<ExaminationBO, String>, TableCell<ExaminationBO, String>> cellFactory =
            p -> new CustomStatusCell();
}
