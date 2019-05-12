package pl.wiktor.management.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.PatientService;
import pl.wiktor.management.utils.StageManager;

@Controller
public class MakeExaminationController {

    private final PatientService patientService;
    private final AppContext appContext;
    private final StageManager stageManager;
    private final MainController mainController;

    public MakeExaminationController(@Lazy StageManager stageManager, AppContext appContext, PatientService patientService, MainController mainController) {
        this.patientService = patientService;
        this.appContext = appContext;
        this.stageManager = stageManager;
        this.mainController = mainController;
    }


    @FXML
    public AnchorPane window;
    @FXML
    public Button makeExaminationButton;
    @FXML
    public ImageView preview;
    @FXML
    public Button exitButton;

    @FXML
    public void initialize() {

    }

    @FXML
    public void makeExamination(ActionEvent actionEvent) {

        String examinationName = "CT";

        Thread thr = new Thread(() -> {

            this.exitButton.setDisable(true);

            for (int i = 0; i <= 15; i++) {
                if (i % 4 == 0) {
                    preview.setImage(new Image("/examination/PRE0.png"));
                } else if (i % 4 == 1) {
                    preview.setImage(new Image("/examination/PRE1.png"));
                } else if (i % 4 == 2) {
                    preview.setImage(new Image("/examination/PRE2.png"));
                } else if (i % 4 == 3) {
                    preview.setImage(new Image("/examination/PRE3.png"));
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            preview.setImage(new Image("/examination/" + examinationName + ".jpg"));
            this.exitButton.setDisable(false);
            this.makeExaminationButton.setDisable(true);
        });
        thr.start();

    }

    public void closeWindow(ActionEvent actionEvent) {
        stageManager.closeStageOnEvent(actionEvent);
    }
}
