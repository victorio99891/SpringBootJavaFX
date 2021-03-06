package pl.wiktor.management.exceptions;

public enum ExceptionInfo {
    BAD_CREDENTIALS {
        @Override
        public String getTitle() {
            return "[BAD CREDENTIALS]";
        }

        @Override
        public String getMessage() {
            return "Login or password is incorrect.";
        }
    },
    USER_EXIST {
        @Override
        public String getTitle() {
            return "[USER EXIST]";
        }

        @Override
        public String getMessage() {
            return "Email currently exist in database.\n Please try another one.";
        }
    },
    ID_SHOULD_BE_NUMBER {
        @Override
        public String getTitle() {
            return "[WRONG FORMAT]";
        }

        @Override
        public String getMessage() {
            return "ID should be an integer number.";
        }
    },
    GENDER_SHOULD_BE_MALE_OR_FEMALE {
        @Override
        public String getTitle() {
            return "[GENDER SHOULD HAVE PROPER VALUES]";
        }

        @Override
        public String getMessage() {
            return "GENDER can be only MALE or FEMALE value.";
        }
    }, PATIENT_EXIST {
        @Override
        public String getTitle() {
            return "[PATIENT WITH PESEL EXIST]";
        }

        @Override
        public String getMessage() {
            return "Patient with this PESEL currently exist in database.";
        }
    }, CANNOT_DELETE_PATIENT {
        @Override
        public String getTitle() {
            return "[PATIENT ASSIGNED TO EXAMINATION]";
        }

        @Override
        public String getMessage() {
            return "Patient cannot be deleted, because assigned to examination. Delete all patient examinations before deleting patient.";
        }
    };

    public abstract String getTitle();

    public abstract String getMessage();

}
