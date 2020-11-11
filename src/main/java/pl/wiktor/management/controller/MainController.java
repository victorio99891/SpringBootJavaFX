package pl.wiktor.management.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.ExceptionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.wiktor.management.model.entity.AnswerEntity;
import pl.wiktor.management.model.entity.QuestionEntity;
import pl.wiktor.management.service.AppContext;
import pl.wiktor.management.service.QuestionService;
import pl.wiktor.management.utils.StageManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
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
    private AnswerEntity editedAnswerEntity = null;

    @FXML
    private Pane window;

    @FXML
    private TextField answerTextField;

    @FXML
    private TextField questionTextField;

    @FXML
    private ListView<String> questionListView;

    @FXML
    private Label explodeLabel;

    @FXML
    private TextArea textArea;

    @FXML
    private ListView<String> answerListView;

    @FXML
    private Button addQuestionBtn;

    @FXML
    private Button deleteQuestionBtn;

    @FXML
    private Button addAnswerBtn;

    @FXML
    private Button deleteAnswerBtn;

    @FXML
    private Button saveQuestionBtn;

    @FXML
    private Button saveAnswerBtn;

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
    void addQuestion(ActionEvent event) {
        QuestionEntity newQuestion = new QuestionEntity("Nowe pytanie");
        newQuestion.getAnswers().add(new AnswerEntity("Nowa odpowiedź"));
        this.questionEntities.add(newQuestion);
        this.questionListView.setItems(FXCollections.observableArrayList(this.questionEntities.stream()
                .map(QuestionEntity::getContent)
                .collect(Collectors.toList())));
    }

    @FXML
    void deleteQuestion(ActionEvent event) {

    }

    @FXML
    void addAnswer(ActionEvent event) {
        AnswerEntity newAnswer = new AnswerEntity("Nowa odpowiedź");
        this.editedQuestionEntity.getAnswers().add(newAnswer);
        this.answerListView.setItems(FXCollections.observableArrayList(this.editedQuestionEntity.getAnswers().stream()
                .map(AnswerEntity::getContent)
                .collect(Collectors.toList())));
    }

    @FXML
    void deleteAnswer(ActionEvent event) {
        this.editedAnswerEntity = null;
        this.answerTextField.setText(null);
        this.editedQuestionEntity.getAnswers().remove(this.answerListView.getSelectionModel().getSelectedIndex());
        this.answerListView.setItems(FXCollections.observableArrayList(this.editedQuestionEntity.getAnswers().stream()
                .map(AnswerEntity::getContent)
                .collect(Collectors.toList())));
    }

    @FXML
    void saveAnswer(ActionEvent event) {
        this.editedAnswerEntity.setContent(this.answerTextField.getText());
        this.editedQuestionEntity.getAnswers().get(this.answerListView.getSelectionModel().getSelectedIndex()).setContent(this.answerTextField.getText());
        this.answerListView.setItems(FXCollections.observableArrayList(this.editedQuestionEntity.getAnswers().stream()
                .map(AnswerEntity::getContent)
                .collect(Collectors.toList())));

    }

    @FXML
    void saveQuestion(ActionEvent event) {

    }

    @FXML
    void chooseFile(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR Archive", "*.jar"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            StringBuilder bld = new StringBuilder();
            bld.append(file.getPath());
            bld.append("\n\n");
            textArea.setText(bld.toString());
            loadJarFile(file);
        } else {
            textArea.setText("File is null!");
        }

    }

    @FXML
    void explode(ActionEvent event) {
        /*
         * export JAVA_HOME="/c/Program Files/Java/jdk1.8.0_241/"
         * "$JAVA_HOME\bin\java" -jar target/PackagedJar.jar
         *
         * "$JAVA_HOME\bin\javac" -d . MojaKlasa.java
         * "$JAVA_HOME\bin\jar" -cf External.jar pl/
         * copy External.jar to the application resources
         */
        File file = new File("lib/External.jar");
        loadJarFile(file);


    }

    private void loadJarFile(File file) {

        // STATIC: "lib/External.jar"

        try {
            URL url = file.toURI().toURL();
            log.info(url.toString());
            URLClassLoader child = new URLClassLoader(new URL[]{url}, this.getClass().getClassLoader());
            Class classToLoad = Class.forName("pl.wiktor.MojaKlasa", true, child);
            Method method = classToLoad.getDeclaredMethod("generate");
            Object instance = classToLoad.newInstance();
            method.invoke(instance);
            explodeLabel.setText("Udało sie!");
        } catch (Exception e) {
            StringBuilder bld = new StringBuilder();
            bld.append(this.textArea.getText());
            bld.append(ExceptionUtils.getStackTrace(e));
            this.textArea.setText(bld.toString());
            explodeLabel.setText("Nie udało sie!");
        }
    }


    private void fetchQuestions() {
        this.questionEntities = this.questionService.findAll();
        this.questionListView.setItems(FXCollections.observableArrayList(this.questionEntities.stream()
                .map(QuestionEntity::getContent)
                .collect(Collectors.toList())));
        this.questionListView.setOnMouseClicked(event -> {
            int selectedIndexQuestion = questionListView.getSelectionModel().getSelectedIndex();
            if (selectedIndexQuestion != -1) {
                this.editedQuestionEntity = this.questionEntities.get(selectedIndexQuestion);
                this.answerListView.setItems(FXCollections.observableArrayList(this.editedQuestionEntity.getAnswers().stream()
                        .map(AnswerEntity::getContent)
                        .collect(Collectors.toList())));
                questionTextField.setText(editedQuestionEntity.getContent());
                editedAnswerEntity = null;
                answerTextField.setText(null);
                this.answerListView.setOnMouseClicked(event2 -> {
                    int selectedIndexAnswerQuestion = answerListView.getSelectionModel().getSelectedIndex();
                    if (selectedIndexAnswerQuestion != -1) {
                        this.editedAnswerEntity = this.editedQuestionEntity.getAnswers()
                                .get(selectedIndexAnswerQuestion);
                        answerTextField.setText(editedAnswerEntity.getContent());
                    }
                });
            }
        });
    }

    private void fillQuestionAndAnswerFields() {
        questionTextField.setText(this.editedQuestionEntity.getContent());
    }

    private String getAnswerFromIndex(int index) {
        try {
            return this.editedQuestionEntity.getAnswers().get(index).getContent();
        } catch (IndexOutOfBoundsException e) {
            log.error(e.getMessage());
        }
        return null;
    }


    //</editor-fold>
}
