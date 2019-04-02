package pl.wiktor.springboot_javafx_example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.springboot_javafx_example.service.UserService;
import pl.wiktor.springboot_javafx_example.utils.StageManager;

@Controller
public class NewWindowController {

    private final StageManager stageManager;

    private final UserService userService;

    public NewWindowController(@Lazy StageManager stageManager, @Lazy UserService userService) {
        this.stageManager = stageManager;
        this.userService = userService;
    }

    @FXML
    Label label1;

    @FXML
    Button button1;


    @FXML
    private void changeScene(ActionEvent event) {
        stageManager.closeStageOnEvent(event);
    }

    @FXML
    public void initialize() {
        label1.setText(userService.findOne(1L).getName());
    }
}
