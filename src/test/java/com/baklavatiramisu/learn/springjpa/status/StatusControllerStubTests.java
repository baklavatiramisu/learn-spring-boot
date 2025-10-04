package com.baklavatiramisu.learn.springjpa.status;

import com.baklavatiramisu.learn.springjpa.status.controller.StatusController;
import com.baklavatiramisu.learn.springjpa.status.controller.StatusRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.format.DateTimeFormatter;

@WebMvcTest(StatusController.class)
@Import(StubStatusService.class)
@DisplayName("StatusController tests with stub implementation of StatusService dependency")
public class StatusControllerStubTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StubStatusService statusService;

    @Test
    @DisplayName("Test GET /users/{userId}/statuses/{statusId} will fetch the correct status that belongs to the user")
    void testGetStatus() throws Exception {
        final long userId = 1L;
        final long statusId = 1L;
        final StatusEntity status = statusService.getStatusById(userId, statusId);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/statuses/{statusId}", userId, statusId))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(status.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status.getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdOn").value(status.getCreatedOn().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedOn").value(status.getUpdatedOn().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
    }

    @Test
    @DisplayName("Test POST /users/{userId}/statuses will create a status associated to the user")
    @DirtiesContext
    void testCreateStatus() throws Exception {
        final long userId = 1L;
        final long statusId = 2L;
        final String status = "Hello, World!";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/{userId}/statuses", userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StatusRequest(status)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isCreated(),
                MockMvcResultMatchers.header().string("location", Matchers.endsWith(String.format("/users/%d/statuses/%d", userId, statusId)))
        );
        final StatusEntity entity = statusService.getStatusById(userId, statusId);
        Assertions.assertEquals(userId, entity.getUser().getId());
        Assertions.assertEquals(statusId, entity.getId());
        Assertions.assertEquals(status, entity.getStatus());
    }

    @Test
    @DisplayName("Test PUT /users/{userId}/statuses/{statusId} will update the status associated to the user")
    @DirtiesContext
    void testUpdateStatus() throws Exception {
        final long userId = 1L;
        final long statusId = 1L;
        final String status = "Hello, World!";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/{userId}/statuses/{statusId}", userId, statusId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StatusRequest(status)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        final StatusEntity entity = statusService.findStatusById(statusId);
        Assertions.assertEquals(statusId, entity.getId());
        Assertions.assertEquals(userId, entity.getUser().getId());
        Assertions.assertEquals(status, entity.getStatus());
    }

    @Test
    @DisplayName("Test DELETE /users/{userId}/statuses/{statusId} will mark the status as deleted")
    @DirtiesContext
    void testDeleteStatus() throws Exception {
        final long userId = 1L;
        final long statusId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}/statuses/{statusId}", userId, statusId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        final StatusEntity entity = statusService.findStatusById(statusId);
        Assertions.assertEquals(statusId, entity.getId());
        Assertions.assertEquals(userId, entity.getUser().getId());
        Assertions.assertNotNull(entity.getDeletedOn());
    }
}
