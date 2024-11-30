package com.booking.bookingservice.exception.handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.booking.bookingservice.exception.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> errors = composeCommonErrorAttributes(status, ex);
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex,
                                                                WebRequest request) {
        Map<String, Object> errors = composeCommonErrorAttributes(HttpStatusCode.valueOf(404), ex);
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> errors = composeCommonErrorAttributes(HttpStatusCode.valueOf(500), ex);
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> composeCommonErrorAttributes(HttpStatusCode status, Throwable ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", status);
        error.put("timestamp", LocalDateTime.now());
        error.put("message", ex.getLocalizedMessage());
        return error;
    }
}
