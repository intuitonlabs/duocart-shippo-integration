package com.intuitionlabs.dulocartshippointegration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class TransactionFailException extends RuntimeException {
    public TransactionFailException(String message) {
        super(message);
    }

}
