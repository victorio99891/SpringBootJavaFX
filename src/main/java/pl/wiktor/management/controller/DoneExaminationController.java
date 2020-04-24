package pl.wiktor.management.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.ExaminationService;
import pl.wiktor.management.service.PatientService;
import pl.wiktor.management.utils.StageManager;

import java.io.IOException;

@Controller
public class DoneExaminationController {


    private final AppContext appContext;
    private final StageManager stageManager;
    private final MainController mainController;
    @FXML
    public ImageView preview;
    @FXML
    public AnchorPane window;
    @FXML
    public TextArea textArea;
    @FXML
    public Button exitButton;

    @Autowired
    public DoneExaminationController(@Lazy StageManager stageManager, AppContext appContext, PatientService patientService, MainController mainController, ExaminationService examinationService) {
        this.appContext = appContext;
        this.stageManager = stageManager;
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        Long examinationId = this.appContext.getExaminationId();
        this.textArea.setText(appContext.getExaminationToManage().get(examinationId).getDescription());
        preview.setImage(new Image("/examination/" + this.appContext.getExaminationToManage().get(examinationId).getImgTechBO().getName() + ".jpg"));
        this.textArea.setEditable(false);
    }


    public void exit(ActionEvent actionEvent) throws IOException {
        stageManager.closeStageOnEvent(actionEvent);
        mainController.switchToExaminationManagement();
    }
}
