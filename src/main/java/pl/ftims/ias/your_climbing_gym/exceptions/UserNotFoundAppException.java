package pl.ftims.ias.your_climbing_gym.exceptions;

import lombok.NoArgsConstructor;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
@NoArgsConstructor
public class UserNotFoundAppException extends AbstractAppException {

    public static final String USER_NOT_EXISTS = "User with identifier: %s does not exist";
    public static final String USER_WITH_EMAIL_NOT_EXISTS = "User with email: %s does not exist";
    public static final String USER_WITH_LOGIN_NOT_EXISTS = "User with login: %s does not exist";

    private UserNotFoundAppException(String message) {
        super(message);
    }

    public static UserNotFoundAppException createUserWithProvidedIdNotFoundException(long id) {
        return new UserNotFoundAppException(String.format(USER_NOT_EXISTS, id));
    }

    public static UserNotFoundAppException createUserWithProvidedEmailNotFoundException(String email) {
        return new UserNotFoundAppException(String.format(USER_WITH_EMAIL_NOT_EXISTS, email));
    }

    public static UserNotFoundAppException createUserWithProvidedLoginNotFoundException(String login) {
        return new UserNotFoundAppException(String.format(USER_WITH_LOGIN_NOT_EXISTS, login));
    }

}
