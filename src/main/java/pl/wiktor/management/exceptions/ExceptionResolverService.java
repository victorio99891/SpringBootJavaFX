package pl.wiktor.management.exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExceptionResolverService {
    private ExceptionResolverService() {
    }

    public static void resolve(ExceptionInfo exceptionInfo) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(exceptionInfo.getTitle());
        alert.setHeaderText(null);
        alert.setContentText(exceptionInfo.getMessage());
        log.error(exceptionInfo.getTitle() + " " + exceptionInfo.getMessage());
        alert.showAndWait();
    }

}
