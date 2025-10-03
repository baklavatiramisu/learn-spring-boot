package com.baklavatiramisu.learn.springjpa.user;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class JpaUserService implements UserService {

    private final UserRepository userRepository;

    public JpaUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity createUser(final String name, final String handle) {
        UserEntity entity = new UserEntity();
        entity.setName(name);
        entity.setHandle(handle);
        entity.setCreatedOn(OffsetDateTime.now());
        entity.setUpdatedOn(OffsetDateTime.now());
        entity = userRepository.save(entity);
        return entity;
    }

    @Override
    public UserEntity getUserById(long id) {
        return userRepository.findExistingUserById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public void updateUser(long id, String name, String handle) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setName(name);
        user.setHandle(handle);
        user.setUpdatedOn(OffsetDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setDeletedOn(OffsetDateTime.now());
        userRepository.save(user);
    }
}
