package pl.ftims.ias.perfectbeta.exceptions;


public class UniqueConstraintAppException extends AbstractAppException {

    public static final String LOGIN_TAKEN = "Login is already taken";
    public static final String EMAIL_TAKEN = "Email is already taken";


    private UniqueConstraintAppException(String message) {
        super(message);
    }

    public static LoginTakenAppException createLoginTakenException() {
        return new LoginTakenAppException(LOGIN_TAKEN);
    }

    public static EmailTakenAppException createEmailTakenException() {
        return new EmailTakenAppException(EMAIL_TAKEN);
    }


    public static class LoginTakenAppException extends UniqueConstraintAppException {

        private LoginTakenAppException(String message) {
            super(message);
        }
    }


    public static class EmailTakenAppException extends UniqueConstraintAppException {

        private EmailTakenAppException(String message) {
            super(message);
        }
    }


}
