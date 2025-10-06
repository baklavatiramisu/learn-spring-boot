package com.baklavatiramisu.learn.springboot.status.controller;

import jakarta.validation.constraints.NotBlank;

public record StatusRequest(@NotBlank String status) {
    @Override
    public String toString() {
        return "StatusRequest{" +
                "status='" + status + '\'' +
                '}';
    }
}
