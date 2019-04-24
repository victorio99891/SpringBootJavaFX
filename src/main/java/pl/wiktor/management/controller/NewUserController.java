package pl.wiktor.management.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.entity.UserEntity;
import pl.wiktor.management.service.UserService;
import pl.wiktor.management.utils.StageManager;

@Controller
public class NewUserController {
    @FXML
    public AnchorPane window;
    @FXML
    public TextField firstNameLabel;
    @FXML
    public TextField lastNameLabel;
    @FXML
    public TextField emailLabel;
    @FXML
    public PasswordField passwordLabel;
    @FXML
    public PasswordField retypePasswordLabel;
    @FXML
    public Label nameValidationLabel;
    @FXML
    public Label lastNameValidationLabel;
    @FXML
    public Label emailValidationLabel;
    @FXML
    public Label passwordValidationLabel;
    @FXML
    public Label repeatPasswordValidationLabel;
    @FXML
    public Button registerButton;

    private final StageManager stageManager;
    private final UserService userService;


    public NewUserController(@Lazy StageManager stageManager, UserService userService) {
        this.stageManager = stageManager;
        this.userService = userService;
    }

    public void register(ActionEvent actionEvent) {
        UserEntity userEntity = new UserEntity();
        String tmp = this.firstNameLabel.getText().substring(0,1).toUpperCase()+this.firstNameLabel.getText().substring(1);
        userEntity.setFirstName(tmp);
        tmp = this.lastNameLabel.getText().substring(0,1).toUpperCase()+this.lastNameLabel.getText().substring(1);
        userEntity.setLastName(tmp);
        userEntity.setEmail(this.emailLabel.getText());
        userEntity.setPassword(this.passwordLabel.getText());
        boolean isUserCreated = userService.checkNewUserCredentials(userEntity);

        if (isUserCreated) {
            stageManager.closeStageOnEvent(actionEvent);
        }

    }


    @FXML
    void initialize() {
        stageManager.fadeInAnimation(window);
        this.registerButton.setDisable(true);
    }

    @FXML
    public void validateUserData(KeyEvent keyEvent) {
        boolean isNameCorrect = false;
        boolean isLastNameCorrect = false;
        boolean isEmailCorrect = false;
        boolean isPasswordCorrect = false;
        boolean retypedPasswordCorrect = false;

        if (this.firstNameLabel.getText().length() >= 2) {
            this.nameValidationLabel.setText("ACCEPTED!");
            this.nameValidationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            isNameCorrect = true;
        } else {
            this.nameValidationLabel.setText("Should contains min. 2 characters");
            this.nameValidationLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }

        if (this.lastNameLabel.getText().length() >= 2) {
            this.lastNameValidationLabel.setText("ACCEPTED!");
            this.lastNameValidationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            isLastNameCorrect = true;
        } else {
            this.lastNameValidationLabel.setText("Should contains min. 2 characters");
            this.lastNameValidationLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }

        if (this.emailLabel.getText().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            this.emailValidationLabel.setText("ACCEPTED!");
            this.emailValidationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            isEmailCorrect = true;
        } else {
            this.emailValidationLabel.setText("Should be proper email something@domain.com");
            this.emailValidationLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }

        if (this.passwordLabel.getText().length() >= 6) {
            this.passwordValidationLabel.setText("ACCEPTED!");
            this.passwordValidationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            isPasswordCorrect = true;
        } else {
            this.passwordValidationLabel.setText("Should contains min. 6 characters");
            this.passwordValidationLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }

        if (this.retypePasswordLabel.getText().equals(this.passwordLabel.getText()) && !this.retypePasswordLabel.getText().isEmpty()) {
            this.repeatPasswordValidationLabel.setText("ACCEPTED!");
            this.repeatPasswordValidationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            retypedPasswordCorrect = true;
        } else {
            this.repeatPasswordValidationLabel.setText("Passwords are not the same.");
            this.repeatPasswordValidationLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }


        if (isNameCorrect && isLastNameCorrect && isEmailCorrect && isPasswordCorrect && retypedPasswordCorrect) {
            this.registerButton.setDisable(false);
        } else {
            this.registerButton.setDisable(true);
        }
    }
}
