package pl.wiktor.management.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.exceptions.ExceptionInfo;
import pl.wiktor.management.exceptions.ExceptionResolverService;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.AuthenticationService;
import pl.wiktor.management.service.UserService;
import pl.wiktor.management.utils.StageManager;
import pl.wiktor.management.view.FxmlView;

import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {
    @FXML
    public AnchorPane window;
    @FXML
    public Label authenticatedUserLabel;
    @FXML
    public TabPane tabPaneComponent;
    @FXML
    public Tab userManagementTab;
    @FXML
    public TableColumn<UserBO, Long> column_id_USER;
    @FXML
    public TableColumn<UserBO, String> column_lastName_USER;
    @FXML
    public TableColumn<UserBO, String> column_firstName_USER;
    @FXML
    public TableColumn<UserBO, String> column_email_USER;
    @FXML
    public TableColumn<UserBO, String> column_role_USER;
    @FXML
    public TableView<UserBO> userManagementTable_USER;
    @FXML
    public Label countResultLabel_USER;
    @FXML
    public ChoiceBox<String> searchChoiceBox_USER;
    @FXML
    public TextField searchTextBox_USER;
    @FXML
    public Button searchButton_USER;
    @FXML
    public Button clearButton_USER;


    private final AppContext appContext;
    private final StageManager stageManager;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    List<UserBO> userBOList;


    public MainController(@Lazy StageManager stageManager,
                          AppContext appContext,
                          AuthenticationService authenticationService,
                          UserService userService
    ) {
        this.stageManager = stageManager;
        this.appContext = appContext;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @FXML
    public void changePassword(ActionEvent actionEvent) {
    }

    @FXML
    public void logout(ActionEvent actionEvent) {
        authenticationService.clearCredentials();
        stageManager.fadeOutAnimation(window, FxmlView.LOGIN);
    }

    @FXML
    public void initialize() {
        if (!authenticationService.isAdministrator()) {
            userManagementTab.setDisable(true);
            tabPaneComponent.getTabs().remove(userManagementTab);
        }
        stageManager.fadeInAnimation(window);

        this.authenticatedUserLabel.setText(this.appContext.getAuthenticatedUser().getLastName()
                + " "
                + this.appContext.getAuthenticatedUser().getFirstName()
                + " [ID: " + this.appContext.getAuthenticatedUser().getId() + "]" + " [" + this.appContext.getAuthenticatedUser().getRole() + "]");
        userBOList = userService.findAllUsers();
        fillUserManagementTable(userBOList);
        fillSearchCheckList();
        openUserEditWindow();
    }

    private void fillSearchCheckList() {
        List<String> columnNames = Arrays.asList("ID", "LASTNAME", "FIRSTNAME", "EMAIL", "ROLE");
        this.searchChoiceBox_USER.setItems(FXCollections.observableArrayList(columnNames));
        this.searchChoiceBox_USER.setValue("ID");
    }

    private void openUserEditWindow() {
        this.userManagementTable_USER.setRowFactory(user -> {
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

    public void fillUserManagementTable(List<UserBO> userBOList) {
        column_id_USER.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_lastName_USER.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_firstName_USER.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        column_email_USER.setCellValueFactory(new PropertyValueFactory<>("email"));
        column_role_USER.setCellValueFactory(new PropertyValueFactory<>("role"));
        userManagementTable_USER.refresh();
        userManagementTable_USER.setItems(FXCollections.observableArrayList(userBOList));
        countResultLabel_USER.setText(String.valueOf(userBOList.size()));
    }

    public void search_USER(ActionEvent actionEvent) {
        List<UserBO> serachList;
        if (!this.searchTextBox_USER.getText().isEmpty() && (this.searchTextBox_USER.getText() != null)) {
            if (!this.searchChoiceBox_USER.getValue().equals("ID")) {
                serachList = userService.findByParameter(this.searchChoiceBox_USER.getValue(), this.searchTextBox_USER.getText());
                fillUserManagementTable(serachList);
            } else {
                if (this.searchTextBox_USER.getText().matches("^[0-9]*$")) {
                    serachList = userService.findByParameter(this.searchChoiceBox_USER.getValue(), this.searchTextBox_USER.getText());
                    fillUserManagementTable(serachList);
                } else {
                    ExceptionResolverService.resolve(ExceptionInfo.ID_SHOULD_BE_NUMBER);
                }
            }
        }

    }

    public void clearResults_USER(ActionEvent actionEvent) {
        fillUserManagementTable(this.userBOList);
        this.searchTextBox_USER.setText("");
    }

    public void refresh_USER(ActionEvent actionEvent) {
        this.userBOList = userService.findAllUsers();
        this.searchTextBox_USER.setText("");
        this.searchChoiceBox_USER.setValue("ID");
        fillUserManagementTable(this.userBOList);
    }


    public void search_PATIENT(ActionEvent actionEvent) {
    }

    public void clearResults_PATIENT(ActionEvent actionEvent) {
    }

    public void refresh_EXAMINATION(ActionEvent actionEvent) {
    }

    public void search_EXAMINATION(ActionEvent actionEvent) {
    }

    public void clearResults_EXAMINATION(ActionEvent actionEvent) {
    }

    public void refresh_PATIENT(ActionEvent actionEvent) {
    }
}
