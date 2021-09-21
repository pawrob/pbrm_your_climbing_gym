package pl.ftims.ias.your_climbing_gym.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.ftims.ias.your_climbing_gym.dto.ExceptionDTO;

import javax.persistence.OptimisticLockException;
import java.sql.SQLException;
import java.time.ZonedDateTime;

@ControllerAdvice
public class AppExceptionHandler {

    static final String DB_CONSTRAINT_UNIQUE_LOGIN = "user_login_key";
    static final String DB_CONSTRAINT_UNIQUE_EMAIL = "user_email_key";
    public static final String LOGIN_TAKEN = "Login is already taken";
    public static final String EMAIL_TAKEN = "Email is already taken";

    @ExceptionHandler(value = SQLException.class)
    public ResponseEntity<Object> handleSQLException(SQLException e) {
        ExceptionDTO exceptionDTO;
        if (e.getMessage().contains(DB_CONSTRAINT_UNIQUE_LOGIN)) {
            exceptionDTO = new ExceptionDTO(LOGIN_TAKEN, HttpStatus.BAD_REQUEST, ZonedDateTime.now(), LOGIN_TAKEN);
        } else if (e.getMessage().contains(DB_CONSTRAINT_UNIQUE_EMAIL)) {
            exceptionDTO = new ExceptionDTO(EMAIL_TAKEN, HttpStatus.BAD_REQUEST, ZonedDateTime.now(), EMAIL_TAKEN);
        } else {
            exceptionDTO = new ExceptionDTO(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now());
        }
        return new ResponseEntity<>(exceptionDTO, exceptionDTO.getHttpStatus());
    }

    @ExceptionHandler(value = UserNotFoundAppException.class)
    public ResponseEntity<Object> handleNotFoundException(UserNotFoundAppException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now(), "NOT_FOUND");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleNotValidException(MethodArgumentNotValidException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(), "INVALID_INPUT");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UniqueConstraintAppException.class)
    public ResponseEntity<Object> handleUniqueConstraintAppException(UniqueConstraintAppException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), HttpStatus.CONFLICT, ZonedDateTime.now(), "INVALID_LOGIN_OR_EMAIL");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<Object> handleTokenException(InvalidTokenException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), HttpStatus.CONFLICT, ZonedDateTime.now(), "INVALID_TOKEN");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), HttpStatus.UNAUTHORIZED, ZonedDateTime.now(), "INVALID_CREDENTIALS");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = OptimisticLockException.class)
    public ResponseEntity<Object> handleOptimisticLockException(OptimisticLockException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), HttpStatus.CONFLICT, ZonedDateTime.now(), "OLE");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NotAllowedAppException.class)
    public ResponseEntity<Object> handleNotAllowedAppException(NotAllowedAppException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), HttpStatus.FORBIDDEN, ZonedDateTime.now(), "NOT_ALLOWED");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(value = GymNotFoundException.class)
    public ResponseEntity<Object> handleGymNotFoundException(GymNotFoundException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now(), "GYM_NOT_FOUND");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }
}
