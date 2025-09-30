package com.baklavatiramisu.learn.springjpa.controller.post;

import jakarta.validation.constraints.NotBlank;

public record CreatePostRequest(@NotBlank String content) {
}
