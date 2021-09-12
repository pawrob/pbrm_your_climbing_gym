package pl.ftims.ias.your_climbing_gym.exceptions;


public class NotAllowedAppException extends AbstractAppException {
    public static final String NOT_ALLOWED = "Operation not allowed";

    public NotAllowedAppException(String message) {
        super(message);
    }

    public static NotAllowedAppException createNotAllowedException() {
        return new NotAllowedAppException(NOT_ALLOWED);
    }
}
