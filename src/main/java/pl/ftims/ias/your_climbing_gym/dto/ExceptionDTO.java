package pl.ftims.ias.your_climbing_gym.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
public class ExceptionDTO {
    private  String message;
    private  HttpStatus httpStatus;
    private  ZonedDateTime timestamp;

    public ExceptionDTO(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}