package com.baklavatiramisu.learn.springjpa.user.controller;

import com.baklavatiramisu.learn.springjpa.user.UserEntity;

import java.time.OffsetDateTime;

public record UserResponse(long id, String name, String handle, OffsetDateTime createdOn, OffsetDateTime updatedOn) {

    public static UserResponse fromUserEntity(UserEntity entity) {
        return new UserResponse(
                entity.getId(),
                entity.getName(),
                entity.getHandle(),
                entity.getCreatedOn(),
                entity.getUpdatedOn()
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
