package com.baklavatiramisu.learn.springjpa.user.controller;

import java.time.OffsetDateTime;

public record UserResponse(long id, String name, String handle, OffsetDateTime createdOn, OffsetDateTime updatedOn) {
    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", handle='" + handle + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }
}
