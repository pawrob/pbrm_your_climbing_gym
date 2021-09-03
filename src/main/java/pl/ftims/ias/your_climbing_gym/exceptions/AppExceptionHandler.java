package pl.ftims.ias.your_climbing_gym.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.ftims.ias.your_climbing_gym.dto.ExceptionDTO;

import java.sql.SQLException;
import java.time.ZonedDateTime;

@ControllerAdvice
public class AppExceptionHandler {

    static final String DB_CONSTRAINT_UNIQUE_LOGIN = "user_login_key";
    static final String DB_CONSTRAINT_UNIQUE_EMAIL = "user_email_key";
    public static final String LOGIN_TAKEN = "Login is already taken";
    public static final String EMAIL_TAKEN = "Email is already taken";

    @ExceptionHandler(value = SQLException.class)
    public ResponseEntity<Object> handleSQLException(SQLException e){
        ExceptionDTO exceptionDTO;
        if (e.getMessage().contains(DB_CONSTRAINT_UNIQUE_LOGIN)) {
            exceptionDTO = new ExceptionDTO(LOGIN_TAKEN, HttpStatus.BAD_REQUEST, ZonedDateTime.now(),LOGIN_TAKEN);
        }else if (e.getMessage().contains(DB_CONSTRAINT_UNIQUE_EMAIL)) {
            exceptionDTO = new ExceptionDTO(EMAIL_TAKEN, HttpStatus.BAD_REQUEST, ZonedDateTime.now(),EMAIL_TAKEN);
        } else{
            exceptionDTO = new ExceptionDTO(e.getMessage(),HttpStatus.BAD_REQUEST, ZonedDateTime.now());
        }
        return new ResponseEntity<>(exceptionDTO, exceptionDTO.getHttpStatus());
    }

    @ExceptionHandler(value = UserNotFoundAppException.class)
    public ResponseEntity<Object> handleNotFoundException(UserNotFoundAppException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now(),"NOT_FOUND");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleNotValidException(MethodArgumentNotValidException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now(),"INVALID_INPUT");
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }
}
