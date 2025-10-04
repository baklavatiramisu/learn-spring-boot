package com.baklavatiramisu.learn.springjpa.status;

import com.baklavatiramisu.learn.springjpa.user.UserEntity;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@TestComponent
public class StubStatusService implements StatusService {
    private final List<UserEntity> users = new ArrayList<>();
    private final List<StatusEntity> statuses = new ArrayList<>();
    private long id = 1L;

    public StubStatusService() {
        super();
        final long userId = 1L;
        final UserEntity user = new UserEntity();
        user.setId(userId);
        user.setName(String.format("Test User %d", userId));
        user.setHandle(String.format("testuser%d", userId));
        user.setCreatedOn(OffsetDateTime.now());
        user.setUpdatedOn(OffsetDateTime.now());
        users.add(user);

        final String[] statusTexts = new String[]{
                "Hello Stubbed World!",
                "I want to learn Java",
                "I want to learn Spring Boot",
                "I want to learn Angular",
                "I want to learn about containers",
                "I want to learn Kubernetes",
                "I want to get certified for Azure",
                "Baklava Tiramisu is so yummy!",
                "This APIs are awesome!",
                "Thank you very much!",
        };

        final OffsetDateTime startTimestamp = OffsetDateTime.now().minusMinutes(statusTexts.length);
        long minutesToAdd = 0L;
        for (final String status : statusTexts) {
            final OffsetDateTime timestamp = startTimestamp.plusMinutes(minutesToAdd++);
            final StatusEntity statusEntity = new StatusEntity();
            statusEntity.setId(id++);
            statusEntity.setUser(user);
            statusEntity.setStatus(status);
            statusEntity.setCreatedOn(timestamp);
            statusEntity.setUpdatedOn(timestamp);
            statuses.add(statusEntity);
        }
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
    public Page<StatusEntity> getAllStatus(long userId, String query, Pageable pageable) {
        final long offset = pageable.getOffset();
        final long size = pageable.getPageSize();
        final Sort sort = pageable.getSort();
        final Sort.Order order = sort.getOrderFor("created_on");
        final List<StatusEntity> sortedStatuses;
        final Comparator<StatusEntity> ascending = (a, b) -> Math.toIntExact(a.getCreatedOn().toInstant().toEpochMilli() - b.getCreatedOn().toInstant().toEpochMilli());
        final Comparator<StatusEntity> descending = (a, b) -> Math.toIntExact(b.getCreatedOn().toInstant().toEpochMilli() - a.getCreatedOn().toInstant().toEpochMilli());
        Stream<StatusEntity> stream = statuses.stream();
        if (query != null && !query.isBlank()) {
            stream = stream.filter(s -> s.getStatus().contains(query));
        }
        if (order != null) {
            if ("created_on".equals(order.getProperty())) {
                stream = stream.sorted(order.getDirection() == Sort.Direction.DESC ? descending : ascending)
                        .skip(offset)
                        .limit(size);
                sortedStatuses = stream.toList();
            } else {
                throw new IllegalStateException("Sorting other than created_on is not support at this moment.");
            }
        } else {
            sortedStatuses = stream.skip(offset).limit(size).toList();
        }
        return new PageImpl<>(sortedStatuses, pageable, statuses.size());
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
