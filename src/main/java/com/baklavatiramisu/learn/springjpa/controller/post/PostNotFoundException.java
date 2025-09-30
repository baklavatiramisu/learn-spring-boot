package com.baklavatiramisu.learn.springjpa.controller.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(final Long postId) {
        super(String.format("Post with ID %d not found", postId));
    }
}
