package pl.wiktor.management.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.AuthenticationService;
import pl.wiktor.management.service.UserService;
import pl.wiktor.management.utils.StageManager;
import pl.wiktor.management.view.FxmlView;

import java.util.List;

@Controller
public class MainController {
    @FXML
    public AnchorPane window;
    @FXML
    public Label authenticatedUserLabel;
    @FXML
    public TableColumn<UserBO, Long> column_id;
    @FXML
    public TableColumn<UserBO, String> column_lastName;
    @FXML
    public TableColumn<UserBO, String> column_name;
    @FXML
    public TableColumn<UserBO, String> column_email;
    @FXML
    public TableView<UserBO> user_management_table;


    private final AppContext appContext;
    private final StageManager stageManager;
    private final AuthenticationService authenticationService;
    private final UserService userService;


    public MainController(@Lazy StageManager stageManager,
                          AppContext appContext,
                          AuthenticationService authenticationService,
                          UserService userService) {
        this.stageManager = stageManager;
        this.appContext = appContext;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @FXML
    public void logout(ActionEvent actionEvent) {
        authenticationService.clearCredentials();
        stageManager.fadeOutAnimation(window, FxmlView.LOGIN);
    }

    @FXML
    public void initialize() {
        stageManager.fadeInAnimation(window);
        this.authenticatedUserLabel.setText(this.appContext.getAuthenticatedUser().getLastName()
                + " "
                + this.appContext.getAuthenticatedUser().getFirstName()
                + " [ID: " + this.appContext.getAuthenticatedUser().getId() + "]");
        fillUserManagementTable();
        openUserEditWindow();
    }

    private void openUserEditWindow() {
        this.user_management_table.setRowFactory(user -> {
            TableRow<UserBO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    UserBO userFromRow = row.getItem();
                    this.appContext.setUserToEdit(userFromRow);
                    stageManager.showScene(FxmlView.USER_EDIT);
                }
            });
            return row;
        });
    }

    private void fillUserManagementTable() {
        List<UserBO> userBOList = userService.findAllUsers();
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        column_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        user_management_table.setItems(FXCollections.observableArrayList(userBOList));
    }
}
