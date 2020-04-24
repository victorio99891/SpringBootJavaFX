package pl.wiktor.management.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.cellfactory.CustomGenderCell;
import pl.wiktor.management.exceptions.ExceptionInfo;
import pl.wiktor.management.exceptions.ExceptionResolverService;
import pl.wiktor.management.model.PatientBO;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.PatientService;
import pl.wiktor.management.utils.StageManager;
import pl.wiktor.management.view.FxmlView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class PatientManagementController {

    private final PatientService patientService;
    @FXML
    public Button refreshButton_PATIENT;
    @FXML
    public TableView<PatientBO> patientManagementTable_PATIENT;
    @FXML
    public TableColumn<PatientBO, Long> column_id_PATIENT;
    @FXML
    public TableColumn<PatientBO, String> column_lastName_PATIENT;
    @FXML
    public TableColumn<PatientBO, String> column_firstName_PATIENT;
    @FXML
    public TableColumn<PatientBO, String> column_PESEL_PATIENT;
    @FXML
    public TableColumn<PatientBO, Boolean> column_gender_PATIENT;
    @FXML
    public Label countResultLabel_PATIENT;
    @FXML
    public ChoiceBox<String> searchChoiceBox_PATIENT;
    @FXML
    public TextField searchTextBox_PATIENT;
    @FXML
    public Button searchButton_PATIENT;
    @FXML
    public Button clearButton_PATIENT;
    @FXML
    public ContextMenu contextMenu_PATIENT;
    @FXML
    public ImageView refreshButtonImgView_PATIENT;
    private List<PatientBO> patientBOList;
    private AppContext appContext;

    private MainController mainController;

    private StageManager stageManager;

    @Autowired
    public PatientManagementController(@Lazy StageManager stageManager, PatientService patientService, AppContext appContext, MainController mainController) {
        this.patientService = patientService;
        this.stageManager = stageManager;
        this.appContext = appContext;
        this.mainController = mainController;
        this.patientBOList = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        patientBOList = patientService.findAllPatients();
        fillPatientManagementTable(patientBOList);
        fillSearchCheckList();
        addEventHandlers();
        openRegistrationWindow();
    }


    private void addEventHandlers() {
        this.searchTextBox_PATIENT.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && this.searchTextBox_PATIENT.isFocused()) {
                search_PATIENT();
            }
        });
    }

    private void fillSearchCheckList() {
        List<String> columnNames = Arrays.asList("ID", "LASTNAME", "FIRSTNAME", "PESEL", "GENDER");
        this.searchChoiceBox_PATIENT.setItems(FXCollections.observableArrayList(columnNames));
        this.searchChoiceBox_PATIENT.setValue("LASTNAME");
    }


    private void openRegistrationWindow() {
        this.patientManagementTable_PATIENT.setRowFactory(user -> {
            TableRow<PatientBO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    PatientBO patientFromRow = row.getItem();
                    this.appContext.setPatientToRegister(patientFromRow);
                    stageManager.showScene(FxmlView.REGISTER_EXAMINATION);
                }
            });
            return row;
        });
    }

    void fillPatientManagementTable(List<PatientBO> patientBOList) {
        column_id_PATIENT.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_firstName_PATIENT.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        column_lastName_PATIENT.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_PESEL_PATIENT.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        column_gender_PATIENT.setCellFactory(CustomGenderCell.cellFactory);
        column_gender_PATIENT.setCellValueFactory(new PropertyValueFactory<>("women"));
        patientManagementTable_PATIENT.refresh();
        patientManagementTable_PATIENT.setItems(FXCollections.observableArrayList(patientBOList));
        countResultLabel_PATIENT.setText(String.valueOf(patientBOList.size()));
    }

    public void deleteSelectedContextMenu_PATIENT(ActionEvent actionEvent) {
        PatientBO patientBO = this.patientManagementTable_PATIENT.getSelectionModel().getSelectedItem();
        if (patientBO != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("[DELETE PATIENT ALERT]");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete patient and all examinations:\nPatient: [" + patientBO.getFirstName() + " " + patientBO.getLastName() + "] ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                patientService.deletePatient(patientBO);
                fillPatientManagementTable(patientService.findAllPatients());
            }
        }
    }

    public void createPatient_PATIENT(ActionEvent actionEvent) {
        stageManager.showScene(FxmlView.REGISTER_PATIENT);
    }

    public void editSelectedContextMenu_PATIENT(ActionEvent actionEvent) {
        PatientBO patientBO = this.patientManagementTable_PATIENT.getSelectionModel().getSelectedItem();
        if (patientBO != null) {
            this.appContext.setPatientToEditAction(true);
            this.appContext.setPatientToEdit(patientBO);
            stageManager.showScene(FxmlView.REGISTER_PATIENT);
        }
    }

    @FXML
    public void search_PATIENT() {
        List<PatientBO> searchList;
        if (!this.searchTextBox_PATIENT.getText().isEmpty() && (this.searchTextBox_PATIENT.getText() != null)) {
            if (!this.searchChoiceBox_PATIENT.getValue().equals("ID") && !this.searchChoiceBox_PATIENT.getValue().equals("GENDER")) {
                searchList = patientService.findByParameter(this.searchChoiceBox_PATIENT.getValue(), this.searchTextBox_PATIENT.getText());
                fillPatientManagementTable(searchList);
            } else if (this.searchChoiceBox_PATIENT.getValue().equals("ID")) {
                if (this.searchTextBox_PATIENT.getText().matches("^[0-9]*$")) {
                    searchList = patientService.findByParameter(this.searchChoiceBox_PATIENT.getValue(), this.searchTextBox_PATIENT.getText());
                    fillPatientManagementTable(searchList);
                } else {
                    ExceptionResolverService.resolve(ExceptionInfo.ID_SHOULD_BE_NUMBER);
                }
            } else if (this.searchChoiceBox_PATIENT.getValue().equals("GENDER")) {
                if (this.searchTextBox_PATIENT.getText().equalsIgnoreCase("MALE") || this.searchTextBox_PATIENT.getText().equalsIgnoreCase("FEMALE")) {
                    searchList = patientService.findByParameter(this.searchChoiceBox_PATIENT.getValue(), this.searchTextBox_PATIENT.getText());
                    fillPatientManagementTable(searchList);
                } else {
                    ExceptionResolverService.resolve(ExceptionInfo.GENDER_SHOULD_BE_MALE_OR_FEMALE);
                }
            }
        }
    }

    @FXML
    public void refresh_PATIENT(ActionEvent actionEvent) {
        this.patientBOList = patientService.findAllPatients();
        this.searchTextBox_PATIENT.setText("");
        this.searchChoiceBox_PATIENT.setValue("LASTNAME");
        fillPatientManagementTable(this.patientBOList);
        mainController.rotateTheRefreshButton(refreshButtonImgView_PATIENT);
    }

    @FXML
    public void clearResults_PATIENT(ActionEvent actionEvent) {
        fillPatientManagementTable(this.patientBOList);
        this.searchTextBox_PATIENT.setText("");
    }


}
