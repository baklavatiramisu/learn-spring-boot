package com.baklavatiramisu.learn.springboot.user.controller;

public record UpdateUserRequest(String name, String handle) {
    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "name='" + name + '\'' +
                ", handle='" + handle + '\'' +
                '}';
    }
}
