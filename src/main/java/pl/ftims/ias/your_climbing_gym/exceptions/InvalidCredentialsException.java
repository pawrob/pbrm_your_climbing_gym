package pl.ftims.ias.your_climbing_gym.exceptions;

public class InvalidCredentialsException extends AbstractAppException {

    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String INVALID_PASSWORD = "Password is incorrect";

    private InvalidCredentialsException(String message) {
        super(message);
    }


    public static InvalidCredentialsException.InvalidLoginOrPasswordException createInvalidCredentialsException() {
        return new InvalidCredentialsException.InvalidLoginOrPasswordException(INVALID_CREDENTIALS);
    }

    public static InvalidCredentialsException.InvalidPasswordException createInvalidPasswordException() {
        return new InvalidCredentialsException.InvalidPasswordException(INVALID_PASSWORD);
    }


    public static class InvalidLoginOrPasswordException extends InvalidCredentialsException {

        private InvalidLoginOrPasswordException(String message) {
            super(message);
        }
    }


    public static class InvalidPasswordException extends InvalidCredentialsException {

        private InvalidPasswordException(String message) {
            super(message);
        }
    }
}
