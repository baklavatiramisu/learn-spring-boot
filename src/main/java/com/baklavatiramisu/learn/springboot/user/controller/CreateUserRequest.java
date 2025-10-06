package com.baklavatiramisu.learn.springboot.user.controller;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(@NotBlank String name, @NotBlank String handle) {
    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "name='" + name + '\'' +
                ", handle='" + handle + '\'' +
                '}';
    }
}
