package com.baklavatiramisu.learn.springjpa.status;

import com.baklavatiramisu.learn.springjpa.ApplicationSecurityConfig;
import com.baklavatiramisu.learn.springjpa.status.controller.StatusController;
import com.baklavatiramisu.learn.springjpa.status.controller.StatusRequest;
import com.baklavatiramisu.learn.springjpa.user.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebMvcTest(StatusController.class)
@DisplayName("StatusController tests with mocked StatusService dependency")
@Import(ApplicationSecurityConfig.class)
public class StatusControllerTests {
    @MockitoBean
    private StatusService statusService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test GET /users/{userId}/statuses/{statusId} will fetch the correct status that belongs to the user")
    @WithMockUser(roles = "status:read")
    void testGetStatus() throws Exception {
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

    @Test
    @DisplayName("Test GET /users/{userId}/statuses will fetch all statuses by the user")
    @WithMockUser(roles = "status:read")
    void testGetAllStatuses() throws Exception {
        final long userId = 10001L;
        final long statusId = 10001L;
        final String searchQuery = "foo";
        final UserEntity user = new UserEntity();
        user.setId(userId);
        user.setName("Mock User");
        user.setHandle("mockuser");
        user.setCreatedOn(OffsetDateTime.now());
        user.setUpdatedOn(OffsetDateTime.now());
        final StatusEntity status = new StatusEntity();
        status.setId(statusId);
        status.setUser(user);
        status.setStatus("Hello, Mocked World!");
        status.setCreatedOn(OffsetDateTime.now());
        status.setUpdatedOn(OffsetDateTime.now());
        Pageable paginationRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "created_on"));
        Page<StatusEntity> response = new PageImpl<>(List.of(status), paginationRequest, 1L);

        BDDMockito.given(statusService.getAllStatus(userId, searchQuery, paginationRequest)).willReturn(response);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/{userId}/statuses", userId)
                                .queryParam("size", "10")
                                .queryParam("page", "0")
                                .queryParam("sort", "created_on,desc")
                                .queryParam("query", "foo")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.size").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.number").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalPages").value(1))
                .andDo(MockMvcResultHandlers.print());

        BDDMockito.verify(statusService).getAllStatus(userId, searchQuery, paginationRequest);
    }

    @Test
    @DisplayName("Test POST /users/{userId}/statuses will create a status associated to the user")
    @WithMockUser(roles = "status:write")
    void testCreateStatus() throws Exception {
        final long userId = 10001L;
        final long statusId = 20001L;
        final String status = "Hello, World!";

        final UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName("Mock User");
        userEntity.setHandle("mockuser");
        userEntity.setCreatedOn(OffsetDateTime.now());
        userEntity.setUpdatedOn(OffsetDateTime.now());

        final StatusEntity statusEntity = new StatusEntity();
        statusEntity.setId(statusId);
        statusEntity.setUser(userEntity);
        statusEntity.setStatus("Hello, Mocked World!");
        statusEntity.setCreatedOn(OffsetDateTime.now());
        statusEntity.setUpdatedOn(OffsetDateTime.now());

        BDDMockito.given(statusService.createStatus(userId, status)).willReturn(statusEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/{userId}/statuses", userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StatusRequest(status)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                MockMvcResultMatchers.status().isCreated(),
                MockMvcResultMatchers.header().string("location", Matchers.endsWith(String.format("/users/%d/statuses/%d", userId, statusId)))
        );

        BDDMockito.verify(statusService).createStatus(userId, status);
    }

    @Test
    @DisplayName("Test PUT /users/{userId}/statuses/{statusId} will update the status associated to the user")
    @WithMockUser(roles = "status:write")
    void testUpdateStatus() throws Exception {
        final long userId = 10001L;
        final long statusId = 20001L;
        final String status = "Hello, World!";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/{userId}/statuses/{statusId}", userId, statusId)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StatusRequest(status)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.verify(statusService).updateStatus(userId, statusId, status);
    }

    @Test
    @DisplayName("Test DELETE /users/{userId}/statuses/{statusId} will mark the status as deleted")
    @WithMockUser(roles = "status:write")
    void testDeleteStatus() throws Exception {
        final long userId = 10001L;
        final long statusId = 20001L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}/statuses/{statusId}", userId, statusId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.verify(statusService).deleteStatus(userId, statusId);
    }
}
