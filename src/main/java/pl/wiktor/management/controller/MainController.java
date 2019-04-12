package pl.wiktor.management.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.service.AuthenticationService;
import pl.wiktor.management.service.UserService;
import pl.wiktor.management.utils.StageManager;
import pl.wiktor.management.view.FxmlView;

@Controller
public class MainController {
    @FXML
    public AnchorPane window;

    @FXML
    public Label authenticatedUser;

    private final StageManager stageManager;
    private final AuthenticationService authenticationService;
    private final UserService userService;


    public MainController(@Lazy StageManager stageManager, AuthenticationService authenticationService, UserService userService) {
        this.stageManager = stageManager;
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
        this.authenticatedUser.setText(authenticationService.getAuthenticatedUser().toString());
    }
}
