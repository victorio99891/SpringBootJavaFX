package pl.wiktor.management.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.ExaminationService;
import pl.wiktor.management.service.PatientService;
import pl.wiktor.management.utils.StageManager;

import java.io.IOException;

@Controller
public class MakeExaminationController {

    private final PatientService patientService;
    private final ExaminationService examinationService;
    private final AppContext appContext;
    private final StageManager stageManager;
    private final MainController mainController;
    private Thread thr;


    public MakeExaminationController(@Lazy StageManager stageManager, AppContext appContext, PatientService patientService, MainController mainController, ExaminationService examinationService) {
        this.patientService = patientService;
        this.appContext = appContext;
        this.stageManager = stageManager;
        this.mainController = mainController;
        this.examinationService = examinationService;
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
    public ProgressIndicator progressIndicator;

    @FXML
    public void initialize() {

    }

    @FXML
    public void makeExamination(ActionEvent actionEvent) {
        this.makeExaminationButton.setDisable(true);

        Long examinationId = this.appContext.getExaminationId();

        thr = new Thread(() -> {


            examinationService.makeExamination(this.appContext.getExaminationToManage().get(examinationId), true);


            this.exitButton.setDisable(true);

            for (int i = 0; i <= 49; i++) {
                if (i % 4 == 0) {
                    preview.setImage(new Image("/examination/PRE0.png"));
                } else if (i % 4 == 1) {
                    preview.setImage(new Image("/examination/PRE1.png"));
                } else if (i % 4 == 2) {
                    preview.setImage(new Image("/examination/PRE2.png"));
                } else if (i % 4 == 3) {
                    preview.setImage(new Image("/examination/PRE3.png"));
                }

                progressIndicator.setProgress(0.02 * i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            progressIndicator.setProgress(1);

            preview.setImage(new Image("/examination/" + this.appContext.getExaminationToManage().get(examinationId).getImgTechBO().getName() + ".jpg"));

            examinationService.makeExamination(this.appContext.getExaminationToManage().get(examinationId), false);
            this.exitButton.setDisable(false);

        });
        if (!thr.isAlive()) {
            thr.start();
        }


    }

    public void closeWindow(ActionEvent actionEvent) throws IOException {
        stageManager.closeStageOnEvent(actionEvent);
        mainController.switchToExaminationManagement();
    }
}
