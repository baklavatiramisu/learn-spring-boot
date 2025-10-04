package com.baklavatiramisu.learn.springjpa.user;

import org.springframework.boot.test.context.TestComponent;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestComponent
public class StubUserService implements UserService {
    private final List<UserEntity> userEntityList = new ArrayList<>();
    private long id = 1L;

    public StubUserService() {
        super();
        createUser("Test Stub User", "teststubuser");
    }

    public Optional<UserEntity> findUserById(long id) {
        return userEntityList.stream().filter(u -> u.getId() == id).findFirst();
    }

    @Override
    public UserEntity createUser(String name, String handle) {
        UserEntity entity = new UserEntity();
        entity.setId(id++);
        entity.setName(name);
        entity.setHandle(handle);
        entity.setCreatedOn(OffsetDateTime.now());
        entity.setUpdatedOn(OffsetDateTime.now());
        userEntityList.add(entity);
        return entity;
    }

    @Override
    public UserEntity getUserById(long id) {
        return userEntityList.stream()
                .filter(a -> id == a.getId() && a.getDeletedOn() == null)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public void updateUser(long id, String name, String handle) {
        UserEntity user = getUserById(id);
        user.setName(name);
        user.setHandle(handle);
        user.setUpdatedOn(OffsetDateTime.now());
    }

    @Override
    public void deleteUser(long id) {
        UserEntity user = userEntityList.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setDeletedOn(OffsetDateTime.now());
    }
}
