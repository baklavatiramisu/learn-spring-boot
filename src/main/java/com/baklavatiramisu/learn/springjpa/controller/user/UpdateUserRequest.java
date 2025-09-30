package com.baklavatiramisu.learn.springjpa.controller.user;

public record UpdateUserRequest(String name, String handle) {
    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "name='" + name + '\'' +
                ", handle='" + handle + '\'' +
                '}';
    }
}
