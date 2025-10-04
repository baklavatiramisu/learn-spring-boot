package com.baklavatiramisu.learn.springjpa.status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StatusService {
    StatusEntity createStatus(long userId, String status);

    Page<StatusEntity> getAllStatus(long userId, String query, Pageable pageable);

    StatusEntity getStatusById(long userId, long statusId);

    void updateStatus(long userId, long statusId, String status);

    void deleteStatus(long userId, long statusId);
}
