package pl.wiktor.management;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.wiktor.management.controller.MainController;
import pl.wiktor.management.utils.StageManager;
import pl.wiktor.management.view.FxmlView;

@SpringBootApplication
@LiquibaseDataSource
@EnableAspectJAutoProxy
public class StartBootJavaFXApp extends Application {

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
        stageManager.switchScene(FxmlView.LOGIN);
    }


    @Bean
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }


    private ConfigurableApplicationContext springBootApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(StartBootJavaFXApp.class);
        String[] args = getParameters().getRaw().toArray(new String[0]);
        return builder.run(args);
    }
}
