package pl.wiktor.management.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.model.ExaminationBO;
import pl.wiktor.management.model.PatientBO;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.ExaminationService;
import pl.wiktor.management.service.PatientService;
import pl.wiktor.management.utils.StageManager;

import java.time.LocalDate;

@Controller
public class DescribeExaminationController {

    private final PatientService patientService;
    private final ExaminationService examinationService;
    private final AppContext appContext;
    private final StageManager stageManager;
    private final MainController mainController;

    public DescribeExaminationController(@Lazy StageManager stageManager, AppContext appContext, PatientService patientService, MainController mainController, ExaminationService examinationService) {
        this.patientService = patientService;
        this.appContext = appContext;
        this.stageManager = stageManager;
        this.mainController = mainController;
        this.examinationService = examinationService;
    }

    @FXML
    public Button confirmButton;
    @FXML
    public Button exitButton;
    @FXML
    public TextArea textArea;
    @FXML
    public ImageView preview;
    @FXML
    public AnchorPane window;

    @FXML
    public void initialize() {
        preview.setImage(new Image("/examination/" + appContext.getExaminationToManage().getImgTechBO().getName() + ".jpg"));
        textArea.setText(getExampleDescription(appContext.getExaminationToManage().getPatientBO(), appContext.getExaminationToManage()));
    }

    public void saveDescription(ActionEvent actionEvent) {
        examinationService.saveDescription(appContext.getExaminationToManage(), textArea.getText());
        confirmButton.setDisable(true);
        textArea.setEditable(false);
        mainController.fillExaminationTable(examinationService.findAllExaminations());
    }

    public void exit(ActionEvent actionEvent) {
        stageManager.closeStageOnEvent(actionEvent);
        mainController.enableExaminationTableView();
    }


    private String getExampleDescription(PatientBO patientBO, ExaminationBO examinationBO) {
        return "=======================================" +
                "\n                                        PATIENT DETAILS                                    " +
                "\n=======================================" +
                "\nName: " + patientBO.getFirstName() +
                "\nLastname: " + patientBO.getLastName() +
                "\nPESEL: " + patientBO.getPesel() +
                "\n=======================================" +
                "\n                                 EXAMINATION DETAILS                                  " +
                "\n=======================================" +
                "\nDate: " + LocalDate.now().toString() +
                "\nExamination type: " + examinationBO.getImgTechBO().getName() +
                "\n\nDescription: \n";
    }
}
