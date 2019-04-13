package pl.wiktor.management.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.utils.StageManager;

@Controller
public class UserEditController {

    @FXML
    public AnchorPane window;
    @FXML
    public Label userLabel;

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
    }
}
