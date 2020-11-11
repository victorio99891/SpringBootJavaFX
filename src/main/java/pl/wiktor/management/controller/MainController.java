package pl.wiktor.management.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.model.entity.QuestionEntity;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.QuestionService;
import pl.wiktor.management.utils.StageManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class MainController {


    private final AppContext appContext;
    private final StageManager stageManager;
    private final QuestionService questionService;
    private List<QuestionEntity> questionEntities = new ArrayList<>();
    private QuestionEntity editedQuestionEntity = null;

    @FXML
    private Pane window;

    @FXML
    private TextField answerTextFieldA;

    @FXML
    private TextField answerTextFieldB;

    @FXML
    private TextField answerTextFieldC;

    @FXML
    private TextField answerTextFieldD;

    @FXML
    private TextField questionTextField;

    @FXML
    private Label explodeLabel;

    @FXML
    private TextArea textArea;

    @FXML
    private ListView<String> listView;

    public MainController(@Lazy StageManager stageManager,
                          AppContext appContext,
                          QuestionService questionService
    ) {
        this.stageManager = stageManager;
        this.appContext = appContext;
        this.questionService = questionService;
    }

    @FXML
    public void initialize() throws IOException {
        stageManager.fadeInAnimation(window);
        fetchQuestions();
    }

    @FXML
    void save(ActionEvent event) {
        QuestionEntity entity = this.questionEntities.stream()
                .filter(item -> item.getId().equals(this.editedQuestionEntity.getId()))
                .findFirst().orElseThrow(() -> new RuntimeException("Cannot find question with ID: " + this.editedQuestionEntity.getId()));
        log.info("Save {}", this.editedQuestionEntity);
    }

    @FXML
    void delete(ActionEvent event) {
        log.info("Delete {}", this.editedQuestionEntity);
    }

    @FXML
    void explode(ActionEvent event) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, URISyntaxException {
        /*
         * export JAVA_HOME="/c/Program Files/Java/jdk1.8.0_241/"
         * "$JAVA_HOME\bin\java" -jar target/PackagedJar.jar
         *
         * "$JAVA_HOME\bin\javac" -d . MojaKlasa.java
         * "$JAVA_HOME\bin\jar" -cf External.jar pl/
         * copy External.jar to the application resources
         */

//        URL url = getClass().getClassLoader().getResource("External.jar");
//        log.info(url.toExternalForm());

        try {
            URL url = new File("lib/External.jar").toURI().toURL();
            log.info(url.toString());
            URLClassLoader child = new URLClassLoader(new URL[]{url}, this.getClass().getClassLoader());
            Class classToLoad = Class.forName("pl.wiktor.MojaKlasa", true, child);
            Method method = classToLoad.getDeclaredMethod("generate");
            Object instance = classToLoad.newInstance();
            method.invoke(instance);
            explodeLabel.setText("Udało sie!");
        } catch (Exception e) {
            this.textArea.setText(e.toString());
            explodeLabel.setText("Nie udało sie!");
        }


    }


    private void fetchQuestions() {
        this.questionEntities = this.questionService.findAll();
        this.listView.setItems(FXCollections.observableArrayList(this.questionEntities.stream()
                .map(QuestionEntity::getContent)
                .collect(Collectors.toList())));
        this.listView.setOnMouseClicked(event -> {
            this.editedQuestionEntity = this.questionEntities.get(listView.getSelectionModel().getSelectedIndex());
            fillQuestionAndAnswerFields();
        });
    }

    private void fillQuestionAndAnswerFields() {
        questionTextField.setText(this.editedQuestionEntity.getContent());
        answerTextFieldA.setText(getAnswerFromIndex(0));
        answerTextFieldB.setText(getAnswerFromIndex(1));
        answerTextFieldC.setText(getAnswerFromIndex(2));
        answerTextFieldD.setText(getAnswerFromIndex(3));
    }

    private String getAnswerFromIndex(int index) {
        try {
            return this.editedQuestionEntity.getAnswers().get(index).getContent();
        } catch (IndexOutOfBoundsException e) {
            log.error(e.getMessage());
        }
        return "(empty)";
    }


    //</editor-fold>
}
