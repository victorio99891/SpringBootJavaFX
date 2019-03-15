package pl.wiktor.springboot_javafx_example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.springboot_javafx_example.utils.StageManager;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class NewWindowController implements Initializable {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    Button button1;


    @FXML
    private void changeScene(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
