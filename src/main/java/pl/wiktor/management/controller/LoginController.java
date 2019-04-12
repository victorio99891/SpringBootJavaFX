package pl.wiktor.management.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.exceptions.ExceptionResolverService;
import pl.wiktor.management.service.AuthenticationService;
import pl.wiktor.management.utils.StageManager;
import pl.wiktor.management.view.FxmlView;

@Controller
public class LoginController {

    @FXML
    public AnchorPane window;
    @FXML
    public TextField loginLabel;
    @FXML
    public PasswordField passwordLabel;
    @FXML
    public Button loginButton;

    private final StageManager stageManager;
    private final AuthenticationService authenticationService;
    private final ExceptionResolverService exceptionResolverService;

    public LoginController(@Lazy StageManager stageManager,
                           AuthenticationService authenticationService,
                           PasswordEncoder passwordEncoder,
                           ExceptionResolverService exceptionResolverService) {
        this.stageManager = stageManager;
        this.authenticationService = authenticationService;
        this.exceptionResolverService = exceptionResolverService;
    }

    @FXML
    public void login(ActionEvent actionEvent) {

        if (authenticationService.checkUserCredentials(this.loginLabel.getText(), this.passwordLabel.getText())) {
            stageManager.fadeOutAnimation(window, FxmlView.MAIN);
        }

    }

    @FXML
    public void initialize() {
        stageManager.fadeInAnimation(window);
    }
}
