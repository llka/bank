package ru.ilka.bank.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(RestException.class)
    public ResponseEntity<String> handleRestException(RestException exception) {
        if (exception.getHttpStatus().is5xxServerError()) {
            log.warn("Rest exception with Server Error status!", exception);
        }
        return new ResponseEntity<>(Optional.ofNullable(exception.getMessage()).orElse("Something went wrong."),
                Optional.ofNullable(exception.getHttpStatus()).orElse(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAnyException(Exception exception) {
        log.error("Unexpected error occurred.", exception);
        return new ResponseEntity<>("Unexpected error occurred. " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MissingRequestHeaderException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<String> handleNotValidException(MethodArgumentNotValidException exception) {
        String message = "Not valid arguments were found: " +
                String.join(";\n", exception.getBindingResult().getAllErrors()
                        .stream()
                        .map(ObjectError::getDefaultMessage)
                        .toArray(String[]::new));
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception) {
        String message = "Following constraint violations were found: " +
                String.join(";\n", exception.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .toArray(String[]::new));
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
