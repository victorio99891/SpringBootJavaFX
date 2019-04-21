package pl.wiktor.management.controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.exceptions.ExceptionInfo;
import pl.wiktor.management.exceptions.ExceptionResolverService;
import pl.wiktor.management.model.PatientBO;
import pl.wiktor.management.service.PatientService;
import pl.wiktor.management.utils.StageManager;

import java.util.HashMap;
import java.util.Map;

@Controller
public class NewPatientController {

    @FXML
    public AnchorPane window;
    @FXML
    public TextField firstNameLabel;
    @FXML
    public Label nameValidationLabel;
    @FXML
    public TextField lastNameLabel;
    @FXML
    public Label lastNameValidationLabel;
    @FXML
    public ChoiceBox<String> genderChoiceBox;
    @FXML
    public TextField peselLabel;
    @FXML
    public Label peselValidationLabel;
    @FXML
    public Button registerButton;
    private Map<String, Boolean> genderMap = new HashMap<>();

    private PatientBO newPatient = new PatientBO();

    private final PatientService patientService;
    private final StageManager stageManager;
    private final MainController mainController;

    public NewPatientController(@Lazy StageManager stageManager, PatientService patientService, MainController mainController) {
        this.patientService = patientService;
        this.stageManager = stageManager;
        this.mainController = mainController;
    }

    public void register(ActionEvent event) {
        System.out.println(newPatient);
        if (!patientService.checkIfPatientExist(newPatient.getPesel())) {
            patientService.savePatient(newPatient);
            mainController.fillPatientManagementTable(patientService.findAllPatients());
            stageManager.closeStageOnEvent(event);
        } else {
            ExceptionResolverService.resolve(ExceptionInfo.PATIENT_EXIST);
        }
    }

    public void validatePatientData() {
        boolean isNameCorrect = false;
        boolean isLastNameCorrect = false;
        boolean isPeselCorrect = false;

        if (this.firstNameLabel.getText().length() >= 2) {
            this.nameValidationLabel.setText("ACCEPTED!");
            this.nameValidationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            isNameCorrect = true;
            this.newPatient.setFirstName(
                    this.firstNameLabel.getText().substring(0, 1).toUpperCase() +
                            this.firstNameLabel.getText().substring(1).toLowerCase());
        } else {
            this.nameValidationLabel.setText("Should contains min. 2 characters");
            this.nameValidationLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }

        if (this.lastNameLabel.getText().length() >= 2) {
            this.lastNameValidationLabel.setText("ACCEPTED!");
            this.lastNameValidationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            isLastNameCorrect = true;
            this.newPatient.setLastName(
                    this.lastNameLabel.getText().substring(0, 1).toUpperCase() +
                            this.lastNameLabel.getText().substring(1).toLowerCase());
        } else {
            this.lastNameValidationLabel.setText("Should contains min. 2 characters");
            this.lastNameValidationLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }


        if (checkIsPeselCorrect()) {
            this.peselValidationLabel.setText("ACCEPTED!");
            this.peselValidationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold");
            this.newPatient.setPesel(this.peselLabel.getText());
            isPeselCorrect = true;
        } else {
            this.peselValidationLabel.setText("Wrong PESEL number.");
            this.peselValidationLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }


        if (isNameCorrect && isLastNameCorrect && isPeselCorrect) {
            this.registerButton.setDisable(false);
        } else {
            this.registerButton.setDisable(true);
        }
    }

    private boolean checkIsPeselCorrect() {
        if (this.peselLabel.getLength() == 11) {
            if (!this.peselLabel.getText().substring(4, 6).matches("^(0[1-9]|1[0-9]|2[0-9]|3[01])$")) {
                return false;
            }
            int genderNumber = Integer.valueOf(this.peselLabel.getText().substring(9, 10));
            if (genderNumber % 2 == 0 && this.genderChoiceBox.getValue().equals("FEMALE")) {
                this.newPatient.setWomen(true);
                return true;
            } else if (genderNumber % 2 != 0 && this.genderChoiceBox.getValue().equals("MALE")) {
                this.newPatient.setWomen(false);
                return true;
            }
        }
        return false;
    }

    @FXML
    public void initialize() {
        this.registerButton.setDisable(true);
        makeTextFieldNumericWithSetLength();
        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                validatePatientData();
            }
        };
        genderMap.put("MALE", false);
        genderMap.put("FEMALE", true);
        this.genderChoiceBox.setItems(FXCollections.observableArrayList(genderMap.keySet()));
        this.genderChoiceBox.setValue("MALE");
        this.genderChoiceBox.getSelectionModel().selectedItemProperty().addListener(changeListener);

    }

    private void makeTextFieldNumericWithSetLength() {
        this.peselLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                this.peselLabel.setText(oldValue);

            }
            if (!newValue.matches("\\w{0,11}")) {
                this.peselLabel.setText(oldValue);
            }

        });
    }


}
