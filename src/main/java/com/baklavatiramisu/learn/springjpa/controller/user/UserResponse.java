package com.baklavatiramisu.learn.springjpa.controller.user;

import com.baklavatiramisu.learn.springjpa.dto.User;

import java.time.OffsetDateTime;

public record UserResponse(Long id, String name, String handle, OffsetDateTime createdOn, OffsetDateTime updatedOn) {
    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getHandle(),
                user.getCreatedOn(),
                user.getUpdatedOn()
        );
    }

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
