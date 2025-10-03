package com.baklavatiramisu.learn.springjpa.status.controller;

import jakarta.validation.constraints.NotBlank;

public record StatusRequest(@NotBlank String status) {
    @Override
    public String toString() {
        return "StatusRequest{" +
                "status='" + status + '\'' +
                '}';
    }
}
