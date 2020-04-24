package pl.wiktor.management.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.model.RoleBO;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.UserService;
import pl.wiktor.management.utils.StageManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserEditController {

    private final StageManager stageManager;
    private final AppContext appContext;
    private final UserService userService;
    @FXML
    public AnchorPane window;
    @FXML
    public TextField firstNameLabel;
    @FXML
    public TextField lastNameLabel;
    @FXML
    public TextField emailLabel;
    @FXML
    public ChoiceBox<String> roleMenu;
    @FXML
    public Button submitButton;
    @FXML
    public Button saveButton;
    private MainController mainController;
    private UserBO userToEdit;

    @Autowired
    public UserEditController(@Lazy StageManager stageManager, AppContext appContext, UserService userService, MainController mainController) {
        this.stageManager = stageManager;
        this.appContext = appContext;
        this.userService = userService;
        this.mainController = mainController;
    }

    @FXML
    void initialize() {
        stageManager.fadeInAnimation(window);
        this.userToEdit = this.appContext.getUserToEdit();
        List<String> roleStringList = this.appContext.getRoleBOList().stream().map(RoleBO::getName).collect(Collectors.toList());
        this.roleMenu.setItems(FXCollections.observableArrayList(roleStringList));
        this.roleMenu.setValue(userToEdit.getRole());
        this.firstNameLabel.setText(userToEdit.getFirstName());
        this.lastNameLabel.setText(userToEdit.getLastName());
        this.emailLabel.setText(userToEdit.getEmail());
        this.saveButton.setDisable(true);
    }


    @FXML
    public void submit(ActionEvent actionEvent) {

        if (this.firstNameLabel.getText().length() >= 2 && this.lastNameLabel.getText().length() >= 2) {
            this.userToEdit.setFirstName(this.firstNameLabel.getText().substring(0, 1).toUpperCase() +
                    this.firstNameLabel.getText().substring(1).toLowerCase());
            this.firstNameLabel.setEditable(false);
            this.firstNameLabel.setStyle("-fx-background-color: #f6ff0e");
            this.userToEdit.setLastName(this.lastNameLabel.getText().substring(0, 1).toUpperCase() +
                    this.lastNameLabel.getText().substring(1).toLowerCase());
            this.lastNameLabel.setEditable(false);
            this.lastNameLabel.setStyle("-fx-background-color: #f6ff0e");
            this.emailLabel.setEditable(false);
            this.emailLabel.setStyle("-fx-background-color: #f6ff0e");
            this.userToEdit.setRole(this.roleMenu.getValue());
            this.roleMenu.setDisable(true);
            this.roleMenu.setStyle("-fx-background-color: #f6ff0e");
            this.submitButton.setDisable(true);
            this.saveButton.setDisable(false);
        }

    }

    @FXML
    public void save(ActionEvent actionEvent) throws IOException {
        userService.updateUserData(this.userToEdit);
        stageManager.closeStageOnEvent(actionEvent);
        mainController.switchToUserManagement();
    }
}
