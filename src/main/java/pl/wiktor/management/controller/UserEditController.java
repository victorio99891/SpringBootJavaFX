package pl.wiktor.management.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.model.RoleBO;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.UserService;
import pl.wiktor.management.utils.StageManager;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserEditController {

    @FXML
    public AnchorPane window;
    @FXML
    public Label userLabel;
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


    private UserBO userToEdit;

    private final StageManager stageManager;
    private final AppContext appContext;
    private final MainController mainController;
    private final UserService userService;


    public UserEditController(@Lazy StageManager stageManager, AppContext appContext, MainController mainController, UserService userService) {
        this.stageManager = stageManager;
        this.appContext = appContext;
        this.mainController = mainController;
        this.userService = userService;
    }

    @FXML
    void initialize() {
        stageManager.fadeInAnimation(window);
        this.userToEdit = this.appContext.getUserToEdit();
        List<String> roleStringList = this.appContext.getRoleBOList().stream().map(RoleBO::getName).collect(Collectors.toList());
        this.roleMenu.setItems(FXCollections.observableArrayList(roleStringList));
        this.roleMenu.setValue(userToEdit.getRole());
        this.userLabel.setText(userToEdit.toString());
        this.firstNameLabel.setText(userToEdit.getFirstName());
        this.lastNameLabel.setText(userToEdit.getLastName());
        this.emailLabel.setText(userToEdit.getEmail());
        this.saveButton.setDisable(true);
    }


    @FXML
    public void submit(ActionEvent actionEvent) {

        this.userToEdit.setFirstName(this.firstNameLabel.getText());
        this.firstNameLabel.setEditable(false);
        this.firstNameLabel.setStyle("-fx-background-color: #f6ff0e");
        this.userToEdit.setLastName(this.lastNameLabel.getText());
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

    @FXML
    public void save(ActionEvent actionEvent) {
        userService.updateUserData(this.userToEdit);
        mainController.fillUserManagementTable(mainController.userBOList);
        stageManager.closeStageOnEvent(actionEvent);
    }
}
