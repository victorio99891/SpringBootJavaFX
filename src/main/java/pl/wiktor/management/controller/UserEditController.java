package pl.wiktor.management.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.utils.StageManager;

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
    public TextField roleLabel;
    @FXML
    public Button submitButton;
    @FXML
    public Button saveButton;


    private UserBO userToEdit;

    private final StageManager stageManager;
    private final AppContext appContext;


    public UserEditController(@Lazy StageManager stageManager, AppContext appContext) {
        this.stageManager = stageManager;
        this.appContext = appContext;
    }

    @FXML
    void initialize() {
        stageManager.fadeInAnimation(window);
        this.userLabel.setText(this.appContext.getUserToEdit().toString());
        this.userToEdit = this.appContext.getUserToEdit();
        this.firstNameLabel.setText(userToEdit.getFirstName());
        this.lastNameLabel.setText(userToEdit.getLastName());
        this.emailLabel.setText(userToEdit.getEmail());

    }
}
