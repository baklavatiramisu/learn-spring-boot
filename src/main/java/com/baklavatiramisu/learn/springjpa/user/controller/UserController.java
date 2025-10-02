package com.baklavatiramisu.learn.springjpa.user.controller;

import com.baklavatiramisu.learn.springjpa.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        throw new IllegalStateException("Not implemented");
    }
}
