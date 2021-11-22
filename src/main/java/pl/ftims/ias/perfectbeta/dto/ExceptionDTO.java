package pl.ftims.ias.perfectbeta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
public class ExceptionDTO {
    private String message;
    private HttpStatus httpStatus;
    private ZonedDateTime timestamp;
    private String key;

    public ExceptionDTO(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public ExceptionDTO(String message, HttpStatus httpStatus, ZonedDateTime timestamp, String key) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
        this.key = key;
    }
}
