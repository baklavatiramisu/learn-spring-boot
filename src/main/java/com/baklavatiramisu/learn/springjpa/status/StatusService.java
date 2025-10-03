package com.baklavatiramisu.learn.springjpa.status;

import java.util.List;

public interface StatusService {
    StatusEntity createStatus(long userId, String status);

    List<StatusEntity> getAllStatus(long userId);

    StatusEntity getStatusById(long statusId);

    void updateStatus(long statusId, String status);

    void deleteStatus(long statusId);
}
