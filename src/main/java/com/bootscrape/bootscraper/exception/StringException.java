package com.bootscrape.bootscraper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class StringException extends RuntimeException {
    public StringException(String message) {
        super(message);
    }
}
