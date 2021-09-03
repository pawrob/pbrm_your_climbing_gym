package pl.ftims.ias.your_climbing_gym.exceptions;


public class InvalidTokenException extends AbstractAppException {

    public static final String INVALID_TOKEN = "User with username: %s have different token";
    public static final String TOKEN_EXPIRED = "Token is not valid any more";

    private InvalidTokenException(String message) {
        super(message);
    }


    public static InvalidTokenException createInvalidTokenException(String username) {
        return new InvalidTokenException(String.format(INVALID_TOKEN, username));
    }

    public static InvalidTokenException createTokenExpiredException() {
        return new InvalidTokenException(TOKEN_EXPIRED);
    }
}
