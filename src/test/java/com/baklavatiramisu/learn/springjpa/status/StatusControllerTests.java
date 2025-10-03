package com.baklavatiramisu.learn.springjpa.status;

import com.baklavatiramisu.learn.springjpa.status.controller.StatusController;
import com.baklavatiramisu.learn.springjpa.user.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@WebMvcTest(StatusController.class)
@DisplayName("StatusController tests with mocked StatusService dependency")
public class StatusControllerTests {
    @MockitoBean
    private StatusService statusService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test /users/{userId}/statuses/{statusesId} with existing user ID and status ID will return correct data")
    void testSingleStatusEndpointWillReturnCorrectStatus() throws Exception {
        final UserEntity user = new UserEntity();
        user.setId(10001L);
        user.setName("Mock User");
        user.setHandle("mockuser");
        user.setCreatedOn(OffsetDateTime.now());
        user.setUpdatedOn(OffsetDateTime.now());
        final StatusEntity status = new StatusEntity();
        status.setId(10001L);
        status.setUser(user);
        status.setStatus("Hello, Mocked World!");
        status.setCreatedOn(OffsetDateTime.now());
        status.setUpdatedOn(OffsetDateTime.now());
        BDDMockito.given(statusService.getStatusById(1L, 1L)).willReturn(status);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/statuses/{statusId}", 1L, 1L))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(status.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status.getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdOn").value(status.getCreatedOn().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedOn").value(status.getUpdatedOn().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
        BDDMockito.verify(statusService).getStatusById(1L, 1L);
    }
}
