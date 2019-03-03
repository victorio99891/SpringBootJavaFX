package pl.wiktor.springboot_javafx_example.view;

import java.util.ResourceBundle;

public enum FxmlView {

    MAIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("main.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Main.fxml";
        }
    },

    NEWWINDOW {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("newwindow.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/NewWindow.fxml";
        }

    };

    public abstract String getTitle();

    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key) {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}
