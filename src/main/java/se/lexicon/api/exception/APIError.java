package se.lexicon.api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class APIError {
    private HttpStatus status;
    private String statusText;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;

    public APIError() {
        this.timestamp = LocalDateTime.now();
    }

    public APIError(HttpStatus status, String statusText) {
        this();
        this.status = status;
        this.statusText = statusText;
    }
}
