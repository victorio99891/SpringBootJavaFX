package pl.wiktor.management.exceptions;

public enum ExceptionInfo {
    USER_NOT_FOUND {
        @Override
        public String getTitle() {
            return "[USER NOT FOUND]";
        }

        @Override
        public String getMessage() {
            return "Unfortunately user has been not found in database.";
        }
    }, BAD_CREDENTIALS {
        @Override
        public String getTitle() {
            return "[BAD CREDENTIALS]";
        }

        @Override
        public String getMessage() {
            return "Login or password is incorrect.";
        }
    };


    public abstract String getTitle();

    public abstract String getMessage();

}
