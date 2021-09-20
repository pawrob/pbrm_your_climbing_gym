package pl.ftims.ias.your_climbing_gym.exceptions;

public class InvalidCredentialsException extends AbstractAppException {

    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String INVALID_PASSWORD = "Password is incorrect";
    public static final String PASSWORD_SAME_AS_OLD = "New password is same as old one";
    public static final String PASSWORDS_NOT_MATCH = "Provided passwords doesnt match";

    private InvalidCredentialsException(String message) {
        super(message);
    }


    public static InvalidCredentialsException.InvalidLoginOrPasswordException createInvalidCredentialsException() {
        return new InvalidCredentialsException.InvalidLoginOrPasswordException(INVALID_CREDENTIALS);
    }

    public static InvalidCredentialsException.InvalidPasswordException createInvalidPasswordException() {
        return new InvalidCredentialsException.InvalidPasswordException(INVALID_PASSWORD);
    }

    public static InvalidCredentialsException.PasswordSameAsOldException createPasswordSameAsOldException() {
        return new InvalidCredentialsException.PasswordSameAsOldException(PASSWORD_SAME_AS_OLD);

    }

    public static InvalidCredentialsException.PasswordNotMatchException createPasswordNotMatchException() {
        return new InvalidCredentialsException.PasswordNotMatchException(PASSWORDS_NOT_MATCH);
    }


    public static class InvalidLoginOrPasswordException extends InvalidCredentialsException {

        private InvalidLoginOrPasswordException(String message) {
            super(message);
        }
    }

    public static class PasswordNotMatchException extends InvalidCredentialsException {

        private PasswordNotMatchException(String message) {
            super(message);
        }
    }

    public static class PasswordSameAsOldException extends InvalidCredentialsException {

        private PasswordSameAsOldException(String message) {
            super(message);
        }
    }


    public static class InvalidPasswordException extends InvalidCredentialsException {

        private InvalidPasswordException(String message) {
            super(message);
        }
    }
}
