package com.baklavatiramisu.learn.springjpa.status;

import com.baklavatiramisu.learn.springjpa.status.controller.StatusController;
import com.baklavatiramisu.learn.springjpa.status.controller.StatusRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(StatusController.class)
@Import(StubStatusService.class)
@DisplayName("StatusController tests with stub implementation of StatusService dependency")
public class StatusControllerStubTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test GET /users/{userId}/statuses/{statusId} will fetch the correct status that belongs to the user")
    void testGetStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/statuses/{statusId}", 1L, 1L))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Hello Stubbed World!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdOn").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedOn").exists());
    }

    @Test
    @DisplayName("Test POST /users/{userId}/statuses will create a status associated to the user")
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
    }

    @Test
    @DisplayName("Test PUT /users/{userId}/statuses/{statusId} will update the status associated to the user")
    void testUpdateStatus() throws Exception {
        final long userId = 1L;
        final long statusId = 2L;
        final String status = "Hello, World!";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/{userId}/statuses/{statusId}", userId, statusId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StatusRequest(status)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test DELETE /users/{userId}/statuses/{statusId} will mark the status as deleted")
    void testDeleteStatus() throws Exception {
        final long userId = 10001L;
        final long statusId = 20001L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}/statuses/{statusId}", userId, statusId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
