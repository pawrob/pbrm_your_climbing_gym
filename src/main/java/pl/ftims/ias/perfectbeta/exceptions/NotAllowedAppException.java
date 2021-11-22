package pl.ftims.ias.perfectbeta.exceptions;


public class NotAllowedAppException extends AbstractAppException {
    public static final String NOT_ALLOWED = "Operation not allowed";
    public static final String GYM_NOT_VERIFIED = "Gym must be verified before that operation";
    public static final String NOT_OWNER_OR_MAINTAINER = "You must be owner or maintainer to perform that operation";

    public NotAllowedAppException(String message) {
        super(message);
    }

    public static NotAllowedAppException createNotAllowedException() {
        return new NotAllowedAppException(NOT_ALLOWED);
    }


    public static GymNotVerifiedException createGymNotVerifiedException() {
        return new GymNotVerifiedException(GYM_NOT_VERIFIED);
    }

    public static class GymNotVerifiedException extends NotAllowedAppException {

        private GymNotVerifiedException(String message) {
            super(message);
        }
    }

    public static YouAreNotMaintainerOrOwnerException createYouAreNotMaintainerOrOwnerException() {
        return new YouAreNotMaintainerOrOwnerException(NOT_OWNER_OR_MAINTAINER);
    }

    public static class YouAreNotMaintainerOrOwnerException extends NotAllowedAppException {

        private YouAreNotMaintainerOrOwnerException(String message) {
            super(message);
        }
    }
}
