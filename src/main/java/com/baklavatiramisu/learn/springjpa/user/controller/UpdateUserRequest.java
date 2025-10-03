package com.baklavatiramisu.learn.springjpa.user.controller;

public record UpdateUserRequest(String name, String handle) {
    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "name='" + name + '\'' +
                ", handle='" + handle + '\'' +
                '}';
    }
}
