package pl.wiktor.management.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.exceptions.ExceptionResolverService;
import pl.wiktor.management.mapper.ExaminationMapper;
import pl.wiktor.management.repository.ExaminationRepository;
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

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private ExaminationMapper examinationMapper;

    public LoginController(@Lazy StageManager stageManager,
                           AuthenticationService authenticationService,
                           ExceptionResolverService exceptionResolverService) {
        this.stageManager = stageManager;
        this.authenticationService = authenticationService;
        this.exceptionResolverService = exceptionResolverService;
    }

    @FXML
    public void loginByEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            login();
        }
    }


    @FXML
    public void login() {
        System.out.println(examinationMapper.fromEntityToBO(examinationRepository.findById(1L).get()));
        if (authenticationService.checkUserCredentials(this.loginLabel.getText(), this.passwordLabel.getText())) {
            stageManager.fadeOutAnimation(window, FxmlView.MAIN);
        }
    }

    @FXML
    public void initialize() {
        stageManager.fadeInAnimation(window);
    }

    @FXML
    public void openUserCreate(MouseEvent mouseEvent) {
        stageManager.showScene(FxmlView.USER_REGISTER);
    }
}
