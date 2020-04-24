package pl.wiktor.management.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import pl.wiktor.management.cellfactory.CustomStatusCell;
import pl.wiktor.management.exceptions.ExceptionInfo;
import pl.wiktor.management.exceptions.ExceptionResolverService;
import pl.wiktor.management.model.ExaminationBO;
import pl.wiktor.management.model.enums.ExaminationStatusEnum;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.ExaminationService;
import pl.wiktor.management.service.PatientService;
import pl.wiktor.management.utils.StageManager;
import pl.wiktor.management.view.FxmlView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class ExaminationManagementController {
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

    private ExaminationService examinationService;

    private PatientService patientService;

    private AppContext appContext;

    private MainController mainController;

    private StageManager stageManager;
    private List<ExaminationBO> examinationBOList;

    @Autowired
    public ExaminationManagementController(@Lazy StageManager stageManager, ExaminationService examinationService, PatientService patientService, AppContext appContext, MainController mainController) {
        this.stageManager = stageManager;
        this.examinationService = examinationService;
        this.patientService = patientService;
        this.mainController = mainController;
        this.appContext = appContext;
        this.examinationBOList = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        examinationBOList = examinationService.findAllExaminations();
        fillExaminationTable(examinationBOList);
        fillSearchCheckList();
        addEventHandlers();
        openExaminationWindows();
        enableExaminationTableView();
    }


    private void fillSearchCheckList() {
        List<String> columnNames = Arrays.asList("ID", "LASTNAME", "FIRSTNAME", "PESEL", "EXAMINATION", "STATUS");
        this.searchChoiceBox_EXAMINATION.setItems(FXCollections.observableArrayList(columnNames));
        this.searchChoiceBox_EXAMINATION.setValue("STATUS");
    }


    private void addEventHandlers() {
        this.searchTextBox_EXAMINATION.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && this.searchTextBox_EXAMINATION.isFocused()) {
                search_EXAMINATION();
            }
        });
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

    public void refresh_EXAMINATION(ActionEvent event) {
        this.examinationBOList = examinationService.findAllExaminations();
        this.searchTextBox_EXAMINATION.setText("");
        this.searchChoiceBox_EXAMINATION.setValue("STATUS");
        fillExaminationTable(examinationBOList);
        this.mainController.rotateTheRefreshButton(refreshButtonImgView_EXAMINATION);
    }


    private void openExaminationWindows() {


        this.examinationManagementTable_EXAMINATION.setRowFactory(user -> {
            TableRow<ExaminationBO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ExaminationBO examinationFromRow = row.getItem();
                    //FIXME: single ExaminationBO
                    //this.appContext.setExaminationToManage(examinationFromRow);
                    if (examinationFromRow != null) {
                        this.appContext.getExaminationToManage().put(examinationFromRow.getId(), examinationFromRow);
                        this.appContext.setExaminationId(examinationFromRow.getId());
                        manageExaminationByStatus(examinationFromRow);
                    }
                }
            });
//            row.setOnContextMenuRequested(event -> {
//                ExaminationBO examinationBO = row.getItem();
//                if (examinationBO != null) {
//                    if (examinationBO.getStatus().equals(ExaminationStatusEnum.DONE.name())) {
//                        this.showResultMenuItem_EXAMINATION.setDisable(false);
//                    } else {
//                        this.showResultMenuItem_EXAMINATION.setDisable(true);
//                    }
//                } else {
//                    this.showResultMenuItem_EXAMINATION.setDisable(true);
//                }
//            });
            return row;
        });
    }

    private void manageExaminationByStatus(ExaminationBO examinationFromRow) {
        if (examinationFromRow.getStatus().equals(ExaminationStatusEnum.REGISTERED.getDisplayName())) {
            this.examinationManagementTable_EXAMINATION.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("[REQUEST PATIENT EXAMINATION]");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to request " + examinationFromRow.getImgTechBO().getName() + " [id:" + examinationFromRow.getId() + "] examination for:\nPatient: [" + examinationFromRow.getPatientBO().getFirstName() + " " + examinationFromRow.getPatientBO().getLastName() + "] ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                patientService.changeExaminationStatus(examinationFromRow, ExaminationStatusEnum.REQUESTED);
                fillExaminationTable(examinationService.findAllExaminations());
            }
            this.examinationManagementTable_EXAMINATION.setDisable(false);
        } else if (examinationFromRow.getStatus().equals(ExaminationStatusEnum.REQUESTED.getDisplayName())) {
            this.examinationManagementTable_EXAMINATION.setDisable(true);
            this.appContext.getExaminationToManage().put(examinationFromRow.getId(), examinationFromRow);
            this.appContext.setExaminationId(examinationFromRow.getId());
            stageManager.showUndecoratedScene(FxmlView.MAKE_EXAMINATION);
        } else if (examinationFromRow.getStatus().equals(ExaminationStatusEnum.FOR_DESCRIPTION.getDisplayName())) {
            this.examinationManagementTable_EXAMINATION.setDisable(true);
            this.appContext.getExaminationToManage().put(examinationFromRow.getId(), examinationFromRow);
            this.appContext.setExaminationId(examinationFromRow.getId());
            stageManager.showUndecoratedScene(FxmlView.DESCRIBE_EXAMINATION);
        } else if (examinationFromRow.getStatus().equals(ExaminationStatusEnum.DONE.getDisplayName())) {
            this.examinationManagementTable_EXAMINATION.setDisable(true);
            this.appContext.getExaminationToManage().put(examinationFromRow.getId(), examinationFromRow);
            this.appContext.setExaminationId(examinationFromRow.getId());
            stageManager.showUndecoratedScene(FxmlView.DONE_EXAMINATION);
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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

    public void enableExaminationTableView() {
        this.examinationManagementTable_EXAMINATION.setDisable(false);
    }

}
