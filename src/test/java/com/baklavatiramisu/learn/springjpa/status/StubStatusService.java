package com.baklavatiramisu.learn.springjpa.status;

import com.baklavatiramisu.learn.springjpa.user.UserEntity;
import org.springframework.boot.test.context.TestComponent;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestComponent
public class StubStatusService implements StatusService {
    private final List<UserEntity> users = new ArrayList<>();
    private final List<StatusEntity> statuses = new ArrayList<>();
    private long id = 1L;

    public StubStatusService() {
        super();
        createStatus(1L, "Hello Stubbed World!");
    }

    @Override
    public StatusEntity createStatus(long userId, String status) {
        Optional<UserEntity> userOptional = users.stream().filter(u -> u.getId() == userId).findFirst();
        final UserEntity user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new UserEntity();
            user.setId(userId);
            user.setName(String.format("Test User %d", userId));
            user.setHandle(String.format("testuser%d", userId));
            user.setCreatedOn(OffsetDateTime.now());
            user.setUpdatedOn(OffsetDateTime.now());
            users.add(user);
        }
        final StatusEntity statusEntity = new StatusEntity();
        statusEntity.setId(id++);
        statusEntity.setUser(user);
        statusEntity.setStatus(status);
        statusEntity.setCreatedOn(OffsetDateTime.now());
        statusEntity.setUpdatedOn(OffsetDateTime.now());
        statuses.add(statusEntity);
        return statusEntity;
    }

    @Override
    public List<StatusEntity> getAllStatus(long userId) {
        return statuses.stream()
                .filter(s -> s.getUser().getId() == userId && s.getDeletedOn() == null)
                .toList();
    }

    @Override
    public StatusEntity getStatusById(long userId, long statusId) {
        return statuses.stream()
                .filter(s -> s.getUser().getId() == userId && s.getId() == statusId && s.getDeletedOn() == null)
                .findFirst()
                .orElseThrow(() -> new StatusNotFoundException(statusId));
    }

    @Override
    public void updateStatus(long userId, long statusId, String status) {
        final StatusEntity statusEntity = getStatusById(userId, statusId);
        statusEntity.setStatus(status);
        statusEntity.setUpdatedOn(OffsetDateTime.now());
    }

    @Override
    public void deleteStatus(long userId, long statusId) {
        StatusEntity entity = getStatusById(userId, statusId);
        entity.setDeletedOn(OffsetDateTime.now());
    }

    public StatusEntity findStatusById(final long statusId) {
        return statuses.stream()
                .filter(s -> s.getId() == statusId)
                .findFirst()
                .orElseThrow(() -> new StatusNotFoundException(statusId));
    }
}
