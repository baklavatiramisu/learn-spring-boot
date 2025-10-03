package com.baklavatiramisu.learn.springjpa.status;

import com.baklavatiramisu.learn.springjpa.user.UserEntity;
import com.baklavatiramisu.learn.springjpa.user.UserService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

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
    public List<StatusEntity> getAllStatus(final long userId) {
        return StreamSupport.stream(statusRepository.findAllStatusByUserId(userId).spliterator(), false).toList();
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
    }
}
