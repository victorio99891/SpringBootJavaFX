package pl.wiktor.management.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.exceptions.ExceptionInfo;
import pl.wiktor.management.exceptions.ExceptionResolverService;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.UserService;
import pl.wiktor.management.utils.StageManager;
import pl.wiktor.management.view.FxmlView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class UserManagementController {
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
    @FXML
    public ContextMenu contextMenu_USER;
    @FXML
    public ImageView refreshButtonImgView_USER;
    StageManager stageManager;
    private List<UserBO> userBOList;
    private UserService userService;
    private AppContext appContext;
    private MainController mainController;


    @Autowired
    public UserManagementController(@Lazy StageManager stageManager, UserService userService, AppContext appContext, MainController mainController) {
        this.stageManager = stageManager;
        this.userService = userService;
        this.appContext = appContext;
        this.mainController = mainController;
        this.userBOList = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        userBOList = userService.findAllUsers();
        fillUserManagementTable(userBOList);
        addEventHandlers();
        fillSearchCheckList();
    }


    private void addEventHandlers() {
        this.searchTextBox_USER.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && this.searchTextBox_USER.isFocused()) {
                search_USER();
            }
        });
    }

    private void fillSearchCheckList() {
        List<String> columnNames = Arrays.asList("ID", "LASTNAME", "FIRSTNAME", "EMAIL", "ROLE");
        this.searchChoiceBox_USER.setItems(FXCollections.observableArrayList(columnNames));
        this.searchChoiceBox_USER.setValue("LASTNAME");
    }

    void fillUserManagementTable(List<UserBO> userBOList) {
        column_id_USER.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_lastName_USER.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_firstName_USER.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        column_email_USER.setCellValueFactory(new PropertyValueFactory<>("email"));
        column_role_USER.setCellValueFactory(new PropertyValueFactory<>("role"));
        userManagementTable_USER.refresh();
        if (!userBOList.isEmpty()) {
            int authenticatedUserIndex = userBOList.indexOf(this.appContext.getAuthenticatedUser());
            if (authenticatedUserIndex != -1) {
                if (userBOList.size() == 1) {
                    userBOList = new ArrayList<>();
                } else {
                    userBOList.remove(authenticatedUserIndex);
                }
            }
        }
        userManagementTable_USER.setItems(FXCollections.observableArrayList(userBOList));
        countResultLabel_USER.setText(String.valueOf(userBOList.size()));
    }

    @FXML
    public void search_USER() {
        List<UserBO> searchList;
        if (!this.searchTextBox_USER.getText().isEmpty() && (this.searchTextBox_USER.getText() != null)) {
            if (!this.searchChoiceBox_USER.getValue().equals("ID")) {
                searchList = userService.findByParameter(this.searchChoiceBox_USER.getValue(), this.searchTextBox_USER.getText());
                fillUserManagementTable(searchList);
            } else {
                if (this.searchTextBox_USER.getText().matches("^[0-9]*$")) {
                    searchList = userService.findByParameter(this.searchChoiceBox_USER.getValue(), this.searchTextBox_USER.getText());
                    fillUserManagementTable(searchList);
                } else {
                    ExceptionResolverService.resolve(ExceptionInfo.ID_SHOULD_BE_NUMBER);
                }
            }
        }
    }

    @FXML
    public void clearResults_USER(ActionEvent actionEvent) {
        fillUserManagementTable(this.userBOList);
        this.searchTextBox_USER.setText("");
    }

    @FXML
    public void refresh_USER(ActionEvent actionEvent) {
        this.userBOList = userService.findAllUsers();
        this.searchTextBox_USER.setText("");
        this.searchChoiceBox_USER.setValue("LASTNAME");
        fillUserManagementTable(this.userBOList);
        mainController.rotateTheRefreshButton(refreshButtonImgView_USER);
    }

    public void deleteSelectedContextMenu_USER(ActionEvent actionEvent) {
        UserBO userBO = this.userManagementTable_USER.getSelectionModel().getSelectedItem();
        if (userBO != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("[DELETE USER ALERT]");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete user:\n" +
                    "User: [" + userBO.getFirstName() + " " + userBO.getLastName() + "]\n" +
                    "Role: [" + userBO.getRole() + "]?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                userService.deleteUser(userBO);
                fillUserManagementTable(userService.findAllUsers());
            }
        }
    }

    public void editSelectedContextMenu_USER(ActionEvent actionEvent) {
        UserBO userBO = this.userManagementTable_USER.getSelectionModel().getSelectedItem();
        if (userBO != null) {
            this.appContext.setUserToEdit(userBO);
            stageManager.showScene(FxmlView.USER_EDIT);
        }
    }


}
