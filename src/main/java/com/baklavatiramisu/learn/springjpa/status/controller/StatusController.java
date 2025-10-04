package com.baklavatiramisu.learn.springjpa.status.controller;

import com.baklavatiramisu.learn.springjpa.status.StatusEntity;
import com.baklavatiramisu.learn.springjpa.status.StatusService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    // create a status
    @PostMapping("/users/{userId}/statuses")
    public ResponseEntity<Void> createStatus(@PathVariable("userId") final long userId, @Valid @RequestBody final StatusRequest request) {
        final StatusEntity status = statusService.createStatus(userId, request.status());
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{statusId}").build(status.getId());
        return ResponseEntity.created(location).build();
    }

    // get status by id
    @GetMapping("/users/{userId}/statuses/{statusId}")
    public StatusResponse getStatusById(@PathVariable("userId") final long userId, @PathVariable("statusId") final long statusId) {
        final StatusEntity entity = statusService.getStatusById(userId, statusId);
        return StatusResponse.fromStatusEntity(entity);
    }

    // get all status of a user
    @GetMapping("/users/{userId}/statuses")
    public List<StatusResponse> getAllStatuses(@PathVariable("userId") final long userId) {
        return statusService.getAllStatus(userId).stream().map(StatusResponse::fromStatusEntity).toList();
    }

    // update
    @PutMapping("/users/{userId}/statuses/{statusId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable("userId") final long userId, @PathVariable("statusId") final long statusId, @RequestBody final StatusRequest request) {
        statusService.updateStatus(userId, statusId, request.status());
    }

    // delete
    @DeleteMapping("/users/{userId}/statuses/{statusId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStatus(@PathVariable("userId") final long userId, @PathVariable("statusId") final long statusId) {
        statusService.deleteStatus(userId, statusId);
    }
}
