package pl.wiktor.management.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.model.ImgTechBO;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.ExaminationService;
import pl.wiktor.management.service.ImgTechService;
import pl.wiktor.management.utils.StageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class RegisterExaminationController {

    @FXML
    public Label firstNameRegisterLabel;
    @FXML
    public Label lastNameRegisterLabel;
    @FXML
    public Label peselRegisterLabel;
    @FXML
    public ChoiceBox<String> examinationRegisterChoicebox;
    @FXML
    public Button registerButton;


    private final StageManager stageManager;
    private final ImgTechService imgTechService;
    private final ExaminationService examinationService;
    private final AppContext appContext;
    private final MainController mainController;

    public RegisterExaminationController(@Lazy StageManager stageManager,
                                         ImgTechService imgTechService,
                                         ExaminationService examinationService,
                                         AppContext appContext,
                                         MainController mainController) {
        this.stageManager = stageManager;
        this.imgTechService = imgTechService;
        this.examinationService = examinationService;
        this.appContext = appContext;
        this.mainController = mainController;
    }


    public void registerExamination(ActionEvent actionEvent) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("[REGISTER ALERT]");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to register:\n patient: [" + this.firstNameRegisterLabel.getText() + " " + this.lastNameRegisterLabel.getText() + "]\n examination: [" + this.examinationRegisterChoicebox.getValue() + "] ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            examinationService.registerExamination(this.appContext.getPatientToRegister(), this.examinationRegisterChoicebox.getValue());
            mainController.fillExaminationTable(examinationService.findAllExaminations());
            mainController.tabPaneComponent.getSelectionModel().select(mainController.examinationManagementTab);
            stageManager.closeStageOnEvent(actionEvent);
        }
    }

    @FXML
    void initialize() {
        registerButton.setDisable(true);
        List<String> imgTechBOList = imgTechService.findAllImagingTechniques().stream().map(ImgTechBO::getName).collect(Collectors.toList());
        imgTechBOList.add(0, "");
        examinationRegisterChoicebox.setItems(FXCollections.observableArrayList(imgTechBOList));
        examinationRegisterChoicebox.setValue("");
        unlockRegisterButton();
        firstNameRegisterLabel.setText(this.appContext.getPatientToRegister().getFirstName());
        lastNameRegisterLabel.setText(this.appContext.getPatientToRegister().getLastName());
        peselRegisterLabel.setText(this.appContext.getPatientToRegister().getPesel());
    }

    private void unlockRegisterButton() {
        examinationRegisterChoicebox.getSelectionModel().selectedItemProperty().addListener((observableValue, before, after) -> {
            if (after.isEmpty()) {
                registerButton.setDisable(true);
            } else {
                registerButton.setDisable(false);
            }
        });
    }
}
