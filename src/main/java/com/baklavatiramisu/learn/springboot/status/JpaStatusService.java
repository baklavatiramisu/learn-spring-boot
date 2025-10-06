package com.baklavatiramisu.learn.springboot.status;

import com.baklavatiramisu.learn.springboot.user.UserEntity;
import com.baklavatiramisu.learn.springboot.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class JpaStatusService implements StatusService {
    private final UserService userService;
    private final StatusRepository statusRepository;

    public JpaStatusService(UserService userService, StatusRepository statusRepository) {
        this.userService = userService;
        this.statusRepository = statusRepository;
    }

    @Override
    public StatusEntity createStatus(final long userId, final String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalStateException("The status is not valid");
        }
        final UserEntity user = userService.getUserById(userId);
        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setUser(user);
        statusEntity.setStatus(status);
        statusEntity.setCreatedOn(OffsetDateTime.now());
        statusEntity.setUpdatedOn(OffsetDateTime.now());
        statusEntity = statusRepository.save(statusEntity);
        return statusEntity;
    }

    @Override
    public Page<StatusEntity> getAllStatus(long userId, String query, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return statusRepository.findAllStatusByUserId(userId, pageable);
        } else {
            return statusRepository.findAllStatusByUserIdWithSearch(userId, query, pageable);
        }
    }

    @Override
    public StatusEntity getStatusById(final long userId, final long statusId) {
        final StatusEntity status = statusRepository.findById(statusId).orElseThrow(() -> new StatusNotFoundException(statusId));
        if (userId != status.getUser().getId()) {
            throw new StatusNotFoundException(statusId);
        }
        return status;
    }

    @Override
    public void updateStatus(final long userId, final long statusId, final String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalStateException("Status cannot be blank");
        }
        final StatusEntity statusEntity = statusRepository.findById(statusId).orElseThrow(() -> new StatusNotFoundException(statusId));
        if (userId != statusEntity.getUser().getId()) {
            throw new StatusNotFoundException(statusId);
        }
        statusEntity.setStatus(status);
        statusEntity.setUpdatedOn(OffsetDateTime.now());
        statusRepository.save(statusEntity);
    }

    @Override
    public void deleteStatus(long userId, long statusId) {
        final StatusEntity statusEntity = statusRepository.findById(statusId).orElseThrow(() -> new StatusNotFoundException(statusId));
        if (userId != statusEntity.getUser().getId()) {
            throw new StatusNotFoundException(statusId);
        }
        statusEntity.setDeletedOn(OffsetDateTime.now());
        statusRepository.save(statusEntity);
    }
}
