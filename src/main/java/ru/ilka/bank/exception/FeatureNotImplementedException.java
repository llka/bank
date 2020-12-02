package ru.ilka.bank.exception;

import org.springframework.http.HttpStatus;

public class FeatureNotImplementedException extends RestException {
    public FeatureNotImplementedException(String featureTitle) {
        super(featureTitle + " is not implemented!", HttpStatus.NOT_IMPLEMENTED);
    }
}
