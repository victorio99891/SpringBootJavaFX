package pl.wiktor.management.cellfactory;

import javafx.scene.control.TableCell;
import pl.wiktor.management.model.enums.RoleEnum;
import pl.wiktor.management.model.UserBO;

/*
 REMEMBER TO SET TWO FACTORIES:
 column_role.setCellFactory(cellFactory);
 column_role.setCellValueFactory(new PropertyValueFactory<>("role"));
 AND CUSTOM CALLBACK:
 Callback<TableColumn<UserBO,String>, TableCell<UserBO,String>> cellFactory =
    p -> new CustomTableCell();
*/

public class CustomTableCell extends TableCell<UserBO, String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            setText(item);
            if (item.equals(RoleEnum.ADMINISTRATOR.name())) {
                setStyle("-fx-background-color: red");
            } else if (item.equals(RoleEnum.USER.name())) {
                setStyle("-fx-background-color: green");
            }
        }

    }
}
