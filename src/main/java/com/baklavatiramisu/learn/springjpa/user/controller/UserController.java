package com.baklavatiramisu.learn.springjpa.user.controller;

import com.baklavatiramisu.learn.springjpa.user.UserEntity;
import com.baklavatiramisu.learn.springjpa.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // create
    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserEntity user = userService.createUser(request.name(), request.handle());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(user.getId());
        return ResponseEntity.created(location).build();
    }

    // get by id
    @GetMapping("/users/{userId}")
    public UserResponse getUserById(@PathVariable("userId") final long userId) {
        UserEntity entity = userService.getUserById(userId);
        return UserResponse.fromUserEntity(entity);
    }

    // update
    @PutMapping("/users/{userId}")
    public ResponseEntity<Void> updateUserById(@PathVariable("userId") final long userId, @Valid @RequestBody UpdateUserRequest request) {
        userService.updateUser(userId, request.name(), request.handle());
        return ResponseEntity.noContent().build();
    }

    // delete
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("userId") final long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
