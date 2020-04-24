package pl.wiktor.management.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.service.*;
import pl.wiktor.management.utils.StageManager;
import pl.wiktor.management.view.FxmlView;

import java.io.IOException;

@Controller
public class MainController {


    private final AppContext appContext;
    private final StageManager stageManager;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final PatientService patientService;
    private final ExaminationService examinationService;

    @FXML
    public AnchorPane window;
    @FXML
    public Label authenticatedUserLabel;
    @FXML
    public TabPane tabPaneComponent;

    @FXML
    public Tab userManagementTab;

    @FXML
    public Tab patientManagementTab;

    @FXML
    public Tab examinationManagementTab;


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
    public void initialize() throws IOException {
        resolveLoggedUserRoles();
        stageManager.fadeInAnimation(window);
        this.authenticatedUserLabel.setText(this.appContext.getAuthenticatedUser().getLastName()
                + " "
                + this.appContext.getAuthenticatedUser().getFirstName()
                + " [ID: " + this.appContext.getAuthenticatedUser().getId() + "]" + " [" + this.appContext.getAuthenticatedUser().getRole() + "]");
        if (authenticationService.isUser()) {
            this.authenticatedUserLabel.setText(this.authenticatedUserLabel.getText() + " PLEASE CONTACT ADMINISTRATOR FOR ADDITIONAL PRIVILEGES!");
            this.authenticatedUserLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
        }


        switchToUserManagement();
        if (userManagementTab.isDisabled()) {
            switchToPatientManagement();
        }


        registerSwitchTabEvent();
    }


    void registerSwitchTabEvent() {
        tabPaneComponent.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab.idProperty().getValue().equals("userManagementTab")) {
                System.err.println("changed to MANAGEMENT TAB");
                try {
                    switchToUserManagement();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (newTab.idProperty().getValue().equals("patientManagementTab")) {
                System.err.println("changed to PATIENT MANAGEMENT TAB");
                try {
                    switchToPatientManagement();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (newTab.idProperty().getValue().equals("examinationManagementTab")) {
                System.err.println("changed to EXAMINATION MANAGEMENT TAB");
                try {
                    switchToExaminationManagement();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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


    private void resolveLoggedUserRoles() {
        if (authenticationService.isUser()) {
            userManagementTab.setDisable(true);
            tabPaneComponent.getTabs().remove(userManagementTab);
            patientManagementTab.setDisable(true);
            tabPaneComponent.getTabs().remove(patientManagementTab);
            examinationManagementTab.setDisable(true);
            tabPaneComponent.getTabs().remove(examinationManagementTab);
        }
        if (!authenticationService.isAdministrator()) {
            userManagementTab.setDisable(true);
            tabPaneComponent.getTabs().remove(userManagementTab);
        }
    }

    public void rotateTheRefreshButton(ImageView buttonImageView) {
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


    public void switchToUserManagement() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserManagementWindow.fxml"));
        loader.setController(new UserManagementController(stageManager, userService, appContext, this));
        HBox box = loader.load();
        userManagementTab.setContent(box);
    }

    public void switchToPatientManagement() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PatientManagementWindow.fxml"));
        loader.setController(new PatientManagementController(stageManager, patientService, appContext, this));
        HBox box = loader.load();
        patientManagementTab.setContent(box);
    }

    public void switchToExaminationManagement() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ExaminationManagementWindow.fxml"));
        loader.setController(new ExaminationManagementController(stageManager, examinationService, patientService, appContext, this));
        HBox box = loader.load();
        examinationManagementTab.setContent(box);
    }


    //</editor-fold>
}
