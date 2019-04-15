package pl.wiktor.management.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.exceptions.ExceptionInfo;
import pl.wiktor.management.exceptions.ExceptionResolverService;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.AuthenticationService;
import pl.wiktor.management.service.UserService;
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
    public TableColumn<UserBO, Long> column_id;
    @FXML
    public TableColumn<UserBO, String> column_lastName;
    @FXML
    public TableColumn<UserBO, String> column_name;
    @FXML
    public TableColumn<UserBO, String> column_email;
    @FXML
    public TableColumn<UserBO, String> column_role;
    @FXML
    public TableView<UserBO> user_management_table;
    @FXML
    public Label countResultLabel;
    @FXML
    public ChoiceBox<String> columnSearchChoicebox;
    @FXML
    public TextField textSearchText;
    @FXML
    public Button searchButton;
    @FXML
    public Button clearButton;


    private final AppContext appContext;
    private final StageManager stageManager;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    List<UserBO> userBOList;


    public MainController(@Lazy StageManager stageManager,
                          AppContext appContext,
                          AuthenticationService authenticationService,
                          UserService userService
    ) {
        this.stageManager = stageManager;
        this.appContext = appContext;
        this.authenticationService = authenticationService;
        this.userService = userService;
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
                + " [ID: " + this.appContext.getAuthenticatedUser().getId() + "]");
        userBOList = userService.findAllUsers();
        fillUserManagementTable(userBOList);
        fillSearchCheckList();
        openUserEditWindow();
    }

    private void fillSearchCheckList() {
        List<String> columnNames = Arrays.asList("ID", "LASTNAME", "FIRSTNAME", "EMAIL", "ROLE");
        this.columnSearchChoicebox.setItems(FXCollections.observableArrayList(columnNames));
        this.columnSearchChoicebox.setValue("ID");
    }

    private void openUserEditWindow() {
        this.user_management_table.setRowFactory(user -> {
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

    public void fillUserManagementTable(List<UserBO> userBOList) {
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        column_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        column_role.setCellValueFactory(new PropertyValueFactory<>("role"));
        user_management_table.refresh();
        user_management_table.setItems(FXCollections.observableArrayList(userBOList));
        countResultLabel.setText(String.valueOf(userBOList.size()));
    }

    public void search(ActionEvent actionEvent) {
        List<UserBO> serachList;
        if (!this.textSearchText.getText().isEmpty() && (this.textSearchText.getText() != null)) {
            if (!this.columnSearchChoicebox.getValue().equals("ID")) {
                serachList = userService.findByParameter(this.columnSearchChoicebox.getValue(), this.textSearchText.getText());
                fillUserManagementTable(serachList);
            } else {
                if (this.textSearchText.getText().matches("^[0-9]*$")) {
                    serachList = userService.findByParameter(this.columnSearchChoicebox.getValue(), this.textSearchText.getText());
                    fillUserManagementTable(serachList);
                } else {
                    ExceptionResolverService.resolve(ExceptionInfo.ID_SHOULD_BE_NUMBER);
                }
            }
        }

    }

    public void clearResults(ActionEvent actionEvent) {
        fillUserManagementTable(this.userBOList);
        this.textSearchText.setText("");
    }

    public void refresh(ActionEvent actionEvent) {
        this.userBOList = userService.findAllUsers();
        this.textSearchText.setText("");
        this.columnSearchChoicebox.setValue("ID");
        fillUserManagementTable(this.userBOList);
    }
}
