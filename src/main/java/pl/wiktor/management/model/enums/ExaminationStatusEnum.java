package pl.wiktor.management.model.enums;

public enum ExaminationStatusEnum {

    REGISTERED {
        @Override
        public String getDisplayName() {
            return "REGISTERED";
        }
    },
    REQUESTED {
        @Override
        public String getDisplayName() {
            return "REQUESTED";
        }
    },
    IN_PROGRESS {
        @Override
        public String getDisplayName() {
            return "IN PROGRESS";
        }
    },
    FOR_DESCRIPTION {
        @Override
        public String getDisplayName() {
            return "FOR DESCRIPTION";
        }
    },
    DONE {
        @Override
        public String getDisplayName() {
            return "DONE";
        }
    };


    public abstract String getDisplayName();
}
