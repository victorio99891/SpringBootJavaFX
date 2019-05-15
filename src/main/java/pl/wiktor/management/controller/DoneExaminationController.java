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
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.ExaminationService;
import pl.wiktor.management.service.PatientService;
import pl.wiktor.management.utils.StageManager;

@Controller
public class DoneExaminationController {


    private final PatientService patientService;
    private final ExaminationService examinationService;
    private final AppContext appContext;
    private final StageManager stageManager;
    private final MainController mainController;

    public DoneExaminationController(@Lazy StageManager stageManager, AppContext appContext, PatientService patientService, MainController mainController, ExaminationService examinationService) {
        this.patientService = patientService;
        this.appContext = appContext;
        this.stageManager = stageManager;
        this.mainController = mainController;
        this.examinationService = examinationService;
    }

    @FXML
    public ImageView preview;
    @FXML
    public AnchorPane window;
    @FXML
    public TextArea textArea;
    @FXML
    public Button exitButton;


    @FXML
    public void initialize() {
        this.textArea.setText(appContext.getExaminationToManage().getDescription());
        preview.setImage(new Image("/examination/" + appContext.getExaminationToManage().getImgTechBO().getName() + ".jpg"));
        this.textArea.setEditable(false);
    }


    public void exit(ActionEvent actionEvent) {
        stageManager.closeStageOnEvent(actionEvent);
        mainController.enableExaminationTableView();
    }
}
