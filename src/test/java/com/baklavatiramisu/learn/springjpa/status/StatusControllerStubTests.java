package com.baklavatiramisu.learn.springjpa.status;

import com.baklavatiramisu.learn.springjpa.status.controller.StatusController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(StatusController.class)
@Import(StubStatusService.class)
@DisplayName("StatusController tests with stub implementation of StatusService dependency")
public class StatusControllerStubTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test /users/{userId}/statuses/{statusId} will contact StubStatusService for a StatusEntity and returns it")
    void foo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/statuses/{statusId}", 1L, 1L))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Hello Stubbed World!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdOn").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedOn").exists());
    }
}
