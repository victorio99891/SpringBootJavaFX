package pl.wiktor.management.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.UserService;
import pl.wiktor.management.utils.StageManager;

@Controller
public class ChangePasswordController {
    @FXML
    public AnchorPane window;
    @FXML
    public PasswordField passwordLabel;
    @FXML
    public Label validationLabel;
    @FXML
    public PasswordField retypePasswordLabel;
    @FXML
    public Label repeatValidationLabel;
    @FXML
    public Button changePasswordButton;


    private final AppContext appContext;
    private final StageManager stageManager;
    private final UserService userService;

    public ChangePasswordController(@Lazy StageManager stageManager, AppContext appContext, UserService userService) {
        this.appContext = appContext;
        this.stageManager = stageManager;
        this.userService = userService;
    }

    public void validateUserData(KeyEvent keyEvent) {
        boolean isPasswordCorrect = false;
        boolean retypedPasswordCorrect = false;

        if (this.passwordLabel.getText().length() >= 6) {
            this.validationLabel.setText("ACCEPTED!");
            this.validationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            isPasswordCorrect = true;
        } else {
            this.validationLabel.setText("Should contains min. 6 characters");
            this.validationLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }

        if (this.retypePasswordLabel.getText().equals(this.passwordLabel.getText()) && !this.retypePasswordLabel.getText().isEmpty()) {
            this.repeatValidationLabel.setText("ACCEPTED!");
            this.repeatValidationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            retypedPasswordCorrect = true;
        } else {
            this.repeatValidationLabel.setText("Passwords are not the same.");
            this.repeatValidationLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }


        if (isPasswordCorrect && retypedPasswordCorrect) {
            this.changePasswordButton.setDisable(false);
        } else {
            this.changePasswordButton.setDisable(true);
        }
    }

    public void changePassword(ActionEvent event) {
        UserBO loggedUser = appContext.getAuthenticatedUser();
        userService.changePassword(loggedUser, this.passwordLabel.getText());
        this.stageManager.closeStageOnEvent(event);
    }

    @FXML
    public void initialize() {
        this.changePasswordButton.setDisable(true);
    }

}
