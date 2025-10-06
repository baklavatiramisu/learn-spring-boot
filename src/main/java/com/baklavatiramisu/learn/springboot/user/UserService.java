package com.baklavatiramisu.learn.springboot.user;

public interface UserService {
    UserEntity createUser(String name, String handle);

    UserEntity getUserById(long id);

    void updateUser(long id, String name, String handle);

    void deleteUser(long id);
}
