package com.baklavatiramisu.learn.springjpa.controller.user;

import com.baklavatiramisu.learn.springjpa.dto.User;
import com.baklavatiramisu.learn.springjpa.repository.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UsersRepository repository;

    public UserController(UsersRepository repository) {
        this.repository = repository;
    }

    // Get all users
    @GetMapping
    public List<UserResponse> allUsers() {
        return repository.findAll().stream().map(UserResponse::fromUser).toList();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public UserResponse userById(@PathVariable("id") final Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserResponse.fromUser(user);
    }

    // Create user
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid final CreateUserRequest request, final UriComponentsBuilder uriComponentsBuilder) {
        User user = new User();
        user.setName(request.name());
        user.setHandle(request.handle());
        user.setCreatedOn(OffsetDateTime.now());
        user.setUpdatedOn(OffsetDateTime.now());
        user = repository.save(user);
        final URI location = uriComponentsBuilder.path("/users/{id}").build(user.getId());
        return ResponseEntity.created(location).build();
    }

    // Update user
    @PutMapping("/{id}")
    public void updateUser(@PathVariable("id") final Long id, @RequestBody final UpdateUserRequest request) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        boolean updated = false;
        if (null != request.name()) {
            user.setName(request.name());
            updated = true;
        }
        if (null != request.handle()) {
            user.setHandle(request.handle());
            updated = true;
        }
        if (updated) {
            user.setUpdatedOn(OffsetDateTime.now());
            repository.save(user);
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") final Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        repository.delete(user);
    }
}
