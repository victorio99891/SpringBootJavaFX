package pl.wiktor.management.view;

import java.util.ResourceBundle;

public enum FxmlView {


    LOGIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/LoginWindow.fxml";
        }
    },

    MAIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("main.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/MainWindow.fxml";
        }
    },

    USER_EDIT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("user_edit.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/UserEditWindow.fxml";
        }

    },
    USER_REGISTER {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("user_register.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/CreateNewUserWindow.fxml";
        }

    };

    public abstract String getTitle();

    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key) {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}
