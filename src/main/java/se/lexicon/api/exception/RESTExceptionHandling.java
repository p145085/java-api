package se.lexicon.api.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RESTExceptionHandling extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, "Malformed JSON request"));
    }

    private ResponseEntity<Object> buildResponseEntity(APIError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder details = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.append(error.getObjectName());
            details.append(",");
            details.append(error.getField());
            details.append(",");
            details.append(error.getRejectedValue());
            details.append(",");
            details.append(error.getDefaultMessage());
        }
        return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, details.toString()));
    }

    @ExceptionHandler(DataNotFoundException.class)
    protected ResponseEntity<Object> dataNotFoundException(DataNotFoundException ex) {
        return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(DataDuplicateException.class)
    protected ResponseEntity<Object> dataDuplicateException(DataDuplicateException ex) {
        System.out.println("ex = " + ex.getMessage());
        return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(ArgumentException.class)
    protected ResponseEntity<Object> argumentException(ArgumentException ex) {
        return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(DataConstraintViolationException.class)
    protected ResponseEntity<Object> dataConstraintViolationException(DataConstraintViolationException ex) {
        return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex) {
        return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, ex.getName() + " is not valid"));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> globalException(Exception ex) {
        APIError apiError = new APIError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setStatusText("INTERNAL_ERROR:" + apiError.getTimestamp().toString());
        System.out.println("#####globalException---------------#####");
        System.out.println("########## " + apiError.getTimestamp());
        ex.printStackTrace();
        System.out.println("#####------------------------------#####");
        return buildResponseEntity(apiError);
    }

}