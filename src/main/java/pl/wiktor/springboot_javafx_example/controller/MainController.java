package pl.wiktor.springboot_javafx_example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.springboot_javafx_example.utils.StageManager;
import pl.wiktor.springboot_javafx_example.view.FxmlView;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class MainController implements Initializable {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    Button button1;

    @FXML
    private void showDialog() {

        stageManager.showScene(FxmlView.NEWWINDOW);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
