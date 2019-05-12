package pl.wiktor.management.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
import pl.wiktor.management.model.enums.ExaminationStatusEnum;
import pl.wiktor.management.service.*;
import pl.wiktor.management.utils.StageManager;
import pl.wiktor.management.view.FxmlView;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {


    private List<UserBO> userBOList;
    private List<PatientBO> patientBOList;
    private List<ExaminationBO> examinationBOList;

    private final AppContext appContext;
    private final StageManager stageManager;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final PatientService patientService;
    private final ExaminationService examinationService;

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
        openRegistrationWindow();
        addEventHandlers();
        openExaminationWindows();
    }

    //<editor-fold desc="GLOBAL WINDOW TAB">
    //SECTION: GLOBAL WINDOW TAB
    @FXML
    public AnchorPane window;
    @FXML
    public Label authenticatedUserLabel;
    @FXML
    public TabPane tabPaneComponent;

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

        this.searchTextBox_EXAMINATION.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && this.searchTextBox_EXAMINATION.isFocused()) {
                search_EXAMINATION();
            }
        });
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

    private void fillSearchCheckList() {
        List<String> columnNames = Arrays.asList("ID", "LASTNAME", "FIRSTNAME", "EMAIL", "ROLE");
        this.searchChoiceBox_USER.setItems(FXCollections.observableArrayList(columnNames));
        this.searchChoiceBox_USER.setValue("LASTNAME");

        columnNames = Arrays.asList("ID", "LASTNAME", "FIRSTNAME", "PESEL", "GENDER");
        this.searchChoiceBox_PATIENT.setItems(FXCollections.observableArrayList(columnNames));
        this.searchChoiceBox_PATIENT.setValue("LASTNAME");

        columnNames = Arrays.asList("ID", "LASTNAME", "FIRSTNAME", "PESEL", "EXAMINATION", "STATUS");
        this.searchChoiceBox_EXAMINATION.setItems(FXCollections.observableArrayList(columnNames));
        this.searchChoiceBox_EXAMINATION.setValue("STATUS");
    }

    private void rotateTheRefreshButton(ImageView buttonImageView) {
        Thread thr = new Thread(() -> {

            for (int i = 0; i <= 720; i += 5) {
                buttonImageView.setStyle("-fx-rotate: " + i);
                try {
                    Thread.sleep(20L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        });
        thr.start();
    }
//</editor-fold>

    //<editor-fold desc="USER TAB">
    //SECTION: USER
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
    public ContextMenu contextMenu_USER;
    @FXML
    public ImageView refreshButtonImgView_USER;

    void fillUserManagementTable(List<UserBO> userBOList) {
        column_id_USER.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_lastName_USER.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_firstName_USER.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        column_email_USER.setCellValueFactory(new PropertyValueFactory<>("email"));
        column_role_USER.setCellValueFactory(new PropertyValueFactory<>("role"));
        userManagementTable_USER.refresh();
        userBOList.remove(userBOList.indexOf(this.appContext.getAuthenticatedUser()));
        userManagementTable_USER.setItems(FXCollections.observableArrayList(userBOList));
        countResultLabel_USER.setText(String.valueOf(userBOList.size()));
    }


    @FXML
    public void search_USER() {
        List<UserBO> searchList;
        if (!this.searchTextBox_USER.getText().isEmpty() && (this.searchTextBox_USER.getText() != null)) {
            if (!this.searchChoiceBox_USER.getValue().equals("ID")) {
                searchList = userService.findByParameter(this.searchChoiceBox_USER.getValue(), this.searchTextBox_USER.getText());
                fillUserManagementTable(searchList);
            } else {
                if (this.searchTextBox_USER.getText().matches("^[0-9]*$")) {
                    searchList = userService.findByParameter(this.searchChoiceBox_USER.getValue(), this.searchTextBox_USER.getText());
                    fillUserManagementTable(searchList);
                } else {
                    ExceptionResolverService.resolve(ExceptionInfo.ID_SHOULD_BE_NUMBER);
                }
            }
        }
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
        rotateTheRefreshButton(refreshButtonImgView_USER);
    }


    public void deleteSelectedContextMenu_USER(ActionEvent actionEvent) {
        UserBO userBO = this.userManagementTable_USER.getSelectionModel().getSelectedItem();
        if (userBO != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("[DELETE USER ALERT]");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete user:\n" +
                    "User: [" + userBO.getFirstName() + " " + userBO.getLastName() + "]\n" +
                    "Role: [" + userBO.getRole() + "]?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                userService.deleteUser(userBO);
                fillUserManagementTable(userService.findAllUsers());
            }
        }
    }

    public void editSelectedContextMenu_USER(ActionEvent actionEvent) {
        UserBO userBO = this.userManagementTable_USER.getSelectionModel().getSelectedItem();
        if (userBO != null) {
            this.appContext.setUserToEdit(userBO);
            stageManager.showScene(FxmlView.USER_EDIT);
        }
    }
    //</editor-fold>

    //<editor-fold desc="PATIENT TAB">
    //SECTION: PATIENT

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
    @FXML
    public ContextMenu contextMenu_PATIENT;
    @FXML
    public ImageView refreshButtonImgView_PATIENT;


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
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("[DELETE PATIENT ALERT]");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete patient and all examinations:\nPatient: [" + patientBO.getFirstName() + " " + patientBO.getLastName() + "] ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                patientService.deletePatient(patientBO);
                fillPatientManagementTable(patientService.findAllPatients());
                fillExaminationTable(examinationService.findAllExaminations());
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
        rotateTheRefreshButton(refreshButtonImgView_PATIENT);
    }

    @FXML
    public void clearResults_PATIENT(ActionEvent actionEvent) {
        fillPatientManagementTable(this.patientBOList);
        this.searchTextBox_PATIENT.setText("");
    }
    //</editor-fold>

    //<editor-fold desc="EXAMINATION TAB">
    //SECTION: EXAMINATION
    @FXML
    public Tab examinationManagementTab;
    @FXML
    public Button refreshButton_EXAMINATION;
    @FXML
    public TableView<ExaminationBO> examinationManagementTable_EXAMINATION;
    @FXML
    public TableColumn<ExaminationBO, Long> column_id_EXAMINATION;
    @FXML
    public TableColumn<ExaminationBO, String> column_lastName_EXAMINATION;
    @FXML
    public TableColumn<ExaminationBO, String> column_firstName_EXAMINATION;
    @FXML
    public TableColumn<ExaminationBO, String> column_PESEL_EXAMINATION;
    @FXML
    public TableColumn<ExaminationBO, String> column_examination_EXAMINATION;
    @FXML
    public TableColumn<ExaminationBO, String> column_status_EXAMINATION;
    @FXML
    public Label countResultLabel_EXAMINATION;
    @FXML
    public ChoiceBox<String> searchChoiceBox_EXAMINATION;
    @FXML
    public TextField searchTextBox_EXAMINATION;
    @FXML
    public Button searchButton_EXAMINATION;
    @FXML
    public Button clearButton_EXAMINATION;
    @FXML
    public ContextMenu contextMenu_EXAMINATION;
    @FXML
    public MenuItem showResultMenuItem_EXAMINATION;
    @FXML
    public ImageView refreshButtonImgView_EXAMINATION;


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

    public void refresh_EXAMINATION(ActionEvent event) {
        this.examinationBOList = examinationService.findAllExaminations();
        this.searchTextBox_EXAMINATION.setText("");
        this.searchChoiceBox_EXAMINATION.setValue("STATUS");
        fillExaminationTable(examinationBOList);
        rotateTheRefreshButton(refreshButtonImgView_EXAMINATION);
    }


    private void openExaminationWindows() {


        this.examinationManagementTable_EXAMINATION.setRowFactory(user -> {
            TableRow<ExaminationBO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ExaminationBO examinationFromRow = row.getItem();
                    this.appContext.setExaminationToManage(examinationFromRow);
                    if (examinationFromRow != null) {
                        manageExaminationByStatus(examinationFromRow);
                    }
                }
            });
            row.setOnContextMenuRequested(event -> {
                ExaminationBO examinationBO = row.getItem();
                if (examinationBO != null) {
                    if (examinationBO.getStatus().equals(ExaminationStatusEnum.DONE.name())) {
                        this.showResultMenuItem_EXAMINATION.setDisable(false);
                    } else {
                        this.showResultMenuItem_EXAMINATION.setDisable(true);
                    }
                } else {
                    this.showResultMenuItem_EXAMINATION.setDisable(true);
                }
            });
            return row;
        });
    }

    private void manageExaminationByStatus(ExaminationBO examinationFromRow) {
        if (examinationFromRow.getStatus().equals(ExaminationStatusEnum.REGISTERED.name())) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("[REQUEST PATIENT EXAMINATION]");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to request " + examinationFromRow.getImgTechBO().getName() + " [id:" + examinationFromRow.getId() + "] examination for:\nPatient: [" + examinationFromRow.getPatientBO().getFirstName() + " " + examinationFromRow.getPatientBO().getLastName() + "] ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                patientService.changeExaminationStatus(examinationFromRow, ExaminationStatusEnum.REQUESTED);
                fillExaminationTable(examinationService.findAllExaminations());
            }
        } else if (examinationFromRow.getStatus().equals(ExaminationStatusEnum.REQUESTED.name())) {
            appContext.setExaminationToManage(examinationFromRow);
            stageManager.showScene(FxmlView.MAKE_EXAMINATION);
        }
    }

    public void search_EXAMINATION() {
        List<ExaminationBO> examinationBOList;
        if (!this.searchTextBox_EXAMINATION.getText().isEmpty() && (this.searchTextBox_EXAMINATION.getText() != null)) {
            if (!this.searchChoiceBox_EXAMINATION.getValue().equals("ID")) {
                examinationBOList = examinationService.findByParameter(this.searchChoiceBox_EXAMINATION.getValue(), this.searchTextBox_EXAMINATION.getText());
                fillExaminationTable(examinationBOList);
            } else {
                if (this.searchTextBox_EXAMINATION.getText().matches("^[0-9]*$")) {
                    examinationBOList = examinationService.findByParameter(this.searchChoiceBox_EXAMINATION.getValue(), this.searchTextBox_EXAMINATION.getText());
                    fillExaminationTable(examinationBOList);
                } else {
                    ExceptionResolverService.resolve(ExceptionInfo.ID_SHOULD_BE_NUMBER);
                }
            }
        }
    }

    public void clearResults_EXAMINATION(ActionEvent event) {
        fillExaminationTable(this.examinationBOList);
        this.searchTextBox_EXAMINATION.setText("");
    }

    public void deleteSelectedContextMenu_EXAMINATION(ActionEvent actionEvent) {
        ExaminationBO examinationBO = this.examinationManagementTable_EXAMINATION.getSelectionModel().getSelectedItem();
        if (examinationBO != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("[DELETE EXAMINATION ALERT]");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete examination:\n" +
                    "Patient: [" + examinationBO.getPatientBO().getFirstName() + " " + examinationBO.getPatientBO().getLastName() + "]\n" +
                    "Examination: [" + examinationBO.getImgTechBO().getName() + "]\n" +
                    "Status: [" + examinationBO.getStatus() + "] ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                examinationService.deletePatient(examinationBO);
                fillExaminationTable(examinationService.findAllExaminations());
            }
        }
    }

    //TODO: Implement action on showResultWindow in EXAMINATION TABLE
    public void showResultContextMenu_EXAMINATION(ActionEvent actionEvent) {
    }
    //</editor-fold>
}
