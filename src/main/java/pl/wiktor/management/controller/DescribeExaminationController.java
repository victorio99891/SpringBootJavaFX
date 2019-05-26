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
import pl.wiktor.management.service.AuthenticationService;
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
    private final AuthenticationService authenticationService;

    public DescribeExaminationController(@Lazy StageManager stageManager, AppContext appContext, PatientService patientService, MainController mainController, ExaminationService examinationService, AuthenticationService authenticationService) {
        this.patientService = patientService;
        this.appContext = appContext;
        this.stageManager = stageManager;
        this.mainController = mainController;
        this.examinationService = examinationService;
        this.authenticationService = authenticationService;
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
        Long examinationId = this.appContext.getExaminationId();
        preview.setImage(new Image("/examination/" + this.appContext.getExaminationToManage().get(examinationId).getImgTechBO().getName() + ".jpg"));
        textArea.setText(getExampleDescription(this.appContext.getExaminationToManage().get(examinationId).getPatientBO(), this.appContext.getExaminationToManage().get(examinationId)));
        if (authenticationService.isTechnican()) {
            textArea.setEditable(false);
            confirmButton.setDisable(true);
        }
    }

    public void saveDescription(ActionEvent actionEvent) {
        Long examinationId = this.appContext.getExaminationId();
        textArea.setText(textArea.getText() + "\n\nExamination described by doctor: " + appContext.getAuthenticatedUser().getLastName().toUpperCase() + " " + appContext.getAuthenticatedUser().getFirstName().toUpperCase());
        examinationService.saveDescription(this.appContext.getExaminationToManage().get(examinationId), textArea.getText());
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
