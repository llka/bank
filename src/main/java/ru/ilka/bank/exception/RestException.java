package ru.ilka.bank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException {
    private final HttpStatus httpStatus;

    public RestException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public RestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
