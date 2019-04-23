package pl.wiktor.management.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.cellfactory.CustomGenderCell;
import pl.wiktor.management.cellfactory.CustomStatusCell;
import pl.wiktor.management.exceptions.ExceptionInfo;
import pl.wiktor.management.exceptions.ExceptionResolverService;
import pl.wiktor.management.model.ExaminationBO;
import pl.wiktor.management.model.PatientBO;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.service.*;
import pl.wiktor.management.utils.StageManager;
import pl.wiktor.management.view.FxmlView;

import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {
    @FXML
    public AnchorPane window;
    @FXML
    public Label authenticatedUserLabel;
    @FXML
    public TabPane tabPaneComponent;
    @FXML
    public Tab userManagementTab;
    @FXML
    public TableColumn<UserBO, Long> column_id_USER;
    @FXML
    public TableColumn<UserBO, String> column_lastName_USER;
    @FXML
    public TableColumn<UserBO, String> column_firstName_USER;
    @FXML
    public TableColumn<UserBO, String> column_email_USER;
    @FXML
    public TableColumn<UserBO, String> column_role_USER;
    @FXML
    public TableView<UserBO> userManagementTable_USER;
    @FXML
    public Label countResultLabel_USER;
    @FXML
    public ChoiceBox<String> searchChoiceBox_USER;
    @FXML
    public TextField searchTextBox_USER;
    @FXML
    public Button searchButton_USER;
    @FXML
    public Button clearButton_USER;
    @FXML
    public Tab patientManagementTab;
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


    private final AppContext appContext;
    private final StageManager stageManager;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final PatientService patientService;
    private final ExaminationService examinationService;
    public Tab examinationManagementTab;
    public Button refreshButton_EXAMINATION;
    public TableView<ExaminationBO> examinationManagementTable_EXAMINATION;
    public TableColumn<ExaminationBO, Long> column_id_EXAMINATION;
    public TableColumn<ExaminationBO, String> column_lastName_EXAMINATION;
    public TableColumn<ExaminationBO, String> column_firstName_EXAMINATION;
    public TableColumn<ExaminationBO, String> column_PESEL_EXAMINATION;
    public TableColumn<ExaminationBO, String> column_examination_EXAMINATION;
    public TableColumn<ExaminationBO, String> column_status_EXAMINATION;
    public Label countResultLabel_EXAMINATION;
    public ChoiceBox searchChoiceBox_EXAMINATION;
    public TextField searchTextBox_EXAMINATION;
    public Button searchButton_EXAMINATION;
    public Button clearButton_EXAMINATION;

    List<UserBO> userBOList;
    List<PatientBO> patientBOList;
    List<ExaminationBO> examinationBOList;


    public MainController(@Lazy StageManager stageManager,
                          AppContext appContext,
                          AuthenticationService authenticationService,
                          UserService userService,
                          PatientService patientService,
                          ExaminationService examinationService
    ) {
        this.stageManager = stageManager;
        this.appContext = appContext;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.patientService = patientService;
        this.examinationService = examinationService;
    }

    @FXML
    public void changePassword(ActionEvent actionEvent) {
        stageManager.showScene(FxmlView.CHANGE_PASSWORD);
    }

    @FXML
    public void logout(ActionEvent actionEvent) {
        authenticationService.clearCredentials();
        stageManager.fadeOutAnimation(window, FxmlView.LOGIN);
    }

    @FXML
    public void initialize() {
        if (!authenticationService.isAdministrator()) {
            userManagementTab.setDisable(true);
            tabPaneComponent.getTabs().remove(userManagementTab);
        }
        stageManager.fadeInAnimation(window);

        this.authenticatedUserLabel.setText(this.appContext.getAuthenticatedUser().getLastName()
                + " "
                + this.appContext.getAuthenticatedUser().getFirstName()
                + " [ID: " + this.appContext.getAuthenticatedUser().getId() + "]" + " [" + this.appContext.getAuthenticatedUser().getRole() + "]");
        userBOList = userService.findAllUsers();
        patientBOList = patientService.findAllPatients();
        examinationBOList = examinationService.findAllExaminations();
        fillUserManagementTable(userBOList);
        fillPatientManagementTable(patientBOList);
        fillExaminationTable(examinationBOList);
        fillSearchCheckList();
        openUserEditWindow();
        addEventHandlers();
    }


    private void fillSearchCheckList() {
        List<String> columnNames = Arrays.asList("ID", "LASTNAME", "FIRSTNAME", "EMAIL", "ROLE");
        this.searchChoiceBox_USER.setItems(FXCollections.observableArrayList(columnNames));
        this.searchChoiceBox_USER.setValue("LASTNAME");

        columnNames = Arrays.asList("ID", "LASTNAME", "FIRSTNAME", "PESEL", "GENDER");
        this.searchChoiceBox_PATIENT.setItems(FXCollections.observableArrayList(columnNames));
        this.searchChoiceBox_PATIENT.setValue("LASTNAME");
    }

    private void openUserEditWindow() {
        this.userManagementTable_USER.setRowFactory(user -> {
            TableRow<UserBO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    UserBO userFromRow = row.getItem();
                    this.appContext.setUserToEdit(userFromRow);
                    stageManager.showScene(FxmlView.USER_EDIT);
                }
            });
            return row;
        });
    }

    void fillUserManagementTable(List<UserBO> userBOList) {
        column_id_USER.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_lastName_USER.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_firstName_USER.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        column_email_USER.setCellValueFactory(new PropertyValueFactory<>("email"));
        column_role_USER.setCellValueFactory(new PropertyValueFactory<>("role"));
        userManagementTable_USER.refresh();
        userManagementTable_USER.setItems(FXCollections.observableArrayList(userBOList));
        countResultLabel_USER.setText(String.valueOf(userBOList.size()));
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

    void fillExaminationTable(List<ExaminationBO> examinationBOList) {
        column_id_EXAMINATION.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_lastName_EXAMINATION.setCellValueFactory(data -> {
            StringProperty sp = new SimpleStringProperty();
            sp.setValue(String.valueOf(data.getValue().getPatientBO().getLastName()));
            return sp;
        });
        column_firstName_EXAMINATION.setCellValueFactory(data -> {
            StringProperty sp = new SimpleStringProperty();
            sp.setValue(String.valueOf(data.getValue().getPatientBO().getFirstName()));
            return sp;
        });
        column_PESEL_EXAMINATION.setCellValueFactory(data -> {
            StringProperty sp = new SimpleStringProperty();
            sp.setValue(String.valueOf(data.getValue().getPatientBO().getPesel()));
            return sp;
        });
        column_examination_EXAMINATION.setCellValueFactory(data -> {
            StringProperty sp = new SimpleStringProperty();
            sp.setValue(String.valueOf(data.getValue().getImgTechBO().getName()));
            return sp;
        });
        column_status_EXAMINATION.setCellFactory(CustomStatusCell.cellFactory);
        column_status_EXAMINATION.setCellValueFactory(new PropertyValueFactory<>("status"));
        examinationManagementTable_EXAMINATION.refresh();
        examinationManagementTable_EXAMINATION.setItems(FXCollections.observableArrayList(examinationBOList));
        countResultLabel_EXAMINATION.setText(String.valueOf(examinationBOList.size()));
    }

    @FXML
    public void search_USER() {
        List<UserBO> serachList;
        if (!this.searchTextBox_USER.getText().isEmpty() && (this.searchTextBox_USER.getText() != null)) {
            if (!this.searchChoiceBox_USER.getValue().equals("ID")) {
                serachList = userService.findByParameter(this.searchChoiceBox_USER.getValue(), this.searchTextBox_USER.getText());
                fillUserManagementTable(serachList);
            } else {
                if (this.searchTextBox_USER.getText().matches("^[0-9]*$")) {
                    serachList = userService.findByParameter(this.searchChoiceBox_USER.getValue(), this.searchTextBox_USER.getText());
                    fillUserManagementTable(serachList);
                } else {
                    ExceptionResolverService.resolve(ExceptionInfo.ID_SHOULD_BE_NUMBER);
                }
            }
        }
    }

    private void addEventHandlers() {
        this.searchTextBox_USER.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && this.searchTextBox_USER.isFocused()) {
                search_USER();
            }
        });

        this.searchTextBox_PATIENT.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && this.searchTextBox_PATIENT.isFocused()) {
                search_PATIENT();
            }
        });
    }

    @FXML
    public void clearResults_USER(ActionEvent actionEvent) {
        fillUserManagementTable(this.userBOList);
        this.searchTextBox_USER.setText("");
    }

    @FXML
    public void refresh_USER(ActionEvent actionEvent) {
        this.userBOList = userService.findAllUsers();
        this.searchTextBox_USER.setText("");
        this.searchChoiceBox_USER.setValue("LASTNAME");
        fillUserManagementTable(this.userBOList);
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
    }

    @FXML
    public void clearResults_PATIENT(ActionEvent actionEvent) {
        fillPatientManagementTable(this.patientBOList);
        this.searchTextBox_PATIENT.setText("");
    }


    public void createPatient_PATIENT(ActionEvent actionEvent) {
        stageManager.showScene(FxmlView.REGISTER_PATIENT);
    }

    public void refresh_EXAMINATION(ActionEvent event) {
    }

    public void search_EXAMINATION(ActionEvent event) {
    }

    public void clearResults_EXAMINATION(ActionEvent event) {
    }
}
