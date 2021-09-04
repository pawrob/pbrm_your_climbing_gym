package pl.ftims.ias.your_climbing_gym.exceptions;

public class InvalidCredentialsException extends AbstractAppException {

    public static final String INVALID_CREDENTIALS = "Invalid credentials";

    private InvalidCredentialsException(String message) {
        super(message);
    }


    public static InvalidCredentialsException createInvalidCredentialsException() {
        return new InvalidCredentialsException(INVALID_CREDENTIALS);
    }

}
