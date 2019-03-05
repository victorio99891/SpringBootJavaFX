package pl.wiktor.springboot_javafx_example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pl.wiktor.springboot_javafx_example.utils.StageManager;
import pl.wiktor.springboot_javafx_example.view.FxmlView;

@SpringBootApplication
public class SpringbootJavafxExampleApplication extends Application {

    private ConfigurableApplicationContext springContext;
    private StageManager stageManager;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() {
        springContext = springBootApplicationContext();
    }

    @Override
    public void start(Stage stage) {
        stageManager = springContext.getBean(StageManager.class, stage);
        displayInitialScene();
    }

    @Override
    public void stop() {
        springContext.close();
    }

    protected void displayInitialScene() {
        stageManager.switchScene(FxmlView.MAIN);
    }


    private ConfigurableApplicationContext springBootApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SpringbootJavafxExampleApplication.class);
        String[] args = getParameters().getRaw().toArray(new String[0]);
        return builder.run(args);
    }
}
