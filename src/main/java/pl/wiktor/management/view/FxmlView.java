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

    },
    CHANGE_PASSWORD {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("change_password.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/ChangePasswordWindow.fxml";
        }
    },
    REGISTER_PATIENT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("patient_register.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/RegisterNewPatientWindow.fxml";
        }
    },
    REGISTER_EXAMINATION {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("examination_register.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/RegisterExaminationWindow.fxml";
        }
    },
    MAKE_EXAMINATION {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("examination_make.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/MakeExaminationWindow.fxml";
        }
    },
    DESCRIBE_EXAMINATION {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("examination_desc.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/DescriptionExaminationWindow.fxml";
        }
    },
    DONE_EXAMINATION {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("examination_done.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/DoneExaminationWindow.fxml";
        }
    };

    public abstract String getTitle();

    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key) {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}
