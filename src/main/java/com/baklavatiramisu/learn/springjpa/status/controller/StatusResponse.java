package com.baklavatiramisu.learn.springjpa.status.controller;

import com.baklavatiramisu.learn.springjpa.status.StatusEntity;

import java.time.OffsetDateTime;

public record StatusResponse(long id, String status, OffsetDateTime createdOn, OffsetDateTime updatedOn) {
    public static StatusResponse fromStatusEntity(StatusEntity entity) {
        return new StatusResponse(
                entity.getId(),
                entity.getStatus(),
                entity.getCreatedOn(),
                entity.getUpdatedOn()
        );
    }

    @Override
    public String toString() {
        return "StatusResponse{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }
}
