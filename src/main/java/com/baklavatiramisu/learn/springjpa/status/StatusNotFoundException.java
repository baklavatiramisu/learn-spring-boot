package com.baklavatiramisu.learn.springjpa.status;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StatusNotFoundException extends RuntimeException {
    public StatusNotFoundException(long id) {
        super(String.format("Status with ID %d not found", id));
    }
}
