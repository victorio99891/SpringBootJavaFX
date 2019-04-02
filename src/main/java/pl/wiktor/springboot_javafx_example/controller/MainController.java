package pl.wiktor.springboot_javafx_example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.springboot_javafx_example.service.UserService;
import pl.wiktor.springboot_javafx_example.utils.StageManager;
import pl.wiktor.springboot_javafx_example.view.FxmlView;

@Controller
public class MainController {

    private final StageManager stageManager;

    private UserService userService;

    public MainController(@Lazy StageManager stageManager, UserService userService) {
        this.stageManager = stageManager;
        this.userService = userService;
    }

    @FXML
    Button button1;

    @FXML
    Label label1;

    @FXML
    private void showDialog() {
        stageManager.showScene(FxmlView.NEWWINDOW);
    }

    @FXML
    public void initialize() {
        label1.setText(userService.findOne(1L).getName());
    }
}
