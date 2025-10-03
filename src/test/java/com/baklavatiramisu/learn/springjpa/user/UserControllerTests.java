package com.baklavatiramisu.learn.springjpa.user;

import com.baklavatiramisu.learn.springjpa.user.controller.CreateUserRequest;
import com.baklavatiramisu.learn.springjpa.user.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@WebMvcTest(UserController.class)
@DisplayName("UserController tests with mocked UserService dependency")
public class UserControllerTests {
    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test GET /users/{id} will return contact UserService for a UserEntity and returns it")
    void testQueryUserUseCase() throws Exception {
        UserEntity testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setHandle("testuser");
        testUser.setCreatedOn(OffsetDateTime.now());
        testUser.setUpdatedOn(OffsetDateTime.now());
        BDDMockito.given(userService.getUserById(1L)).willReturn(testUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", testUser.getId()))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.handle").value(testUser.getHandle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testUser.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdOn").value(testUser.getCreatedOn().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedOn").value(testUser.getUpdatedOn().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));

        BDDMockito.verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("Test POST /users will call createUser in UserService")
    void testCreateUserUseCase() throws Exception {
        final String handle = "testuser";
        final String name = "Test User";
        final long id = 1L;
        UserEntity testUser = new UserEntity();
        testUser.setId(id);
        testUser.setName(name);
        testUser.setHandle(handle);
        testUser.setCreatedOn(OffsetDateTime.now());
        testUser.setUpdatedOn(OffsetDateTime.now());
        BDDMockito.given(userService.createUser(name, handle)).willReturn(testUser);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new CreateUserRequest(name, handle)))
                )
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.header().string("location", Matchers.endsWith("/users/1")));

        BDDMockito.verify(userService).createUser(name, handle);
    }

    @Test
    @DisplayName("Test PUT /users/{id} endpoint will correctly call updateUser method in UserService")
    void testUpdateUserUseCase() throws Exception {
        final String handle = "testuser";
        final String name = "Test User";
        final long id = 1L;
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/users/{id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new CreateUserRequest(name, handle)))
                )
                .andExpect(MockMvcResultMatchers.status().is(204));

        BDDMockito.verify(userService).updateUser(id, name, handle);
    }

    @Test
    @DisplayName("Test DELETE /users/{id} endpoint will correctly call deleteUser method in UserService")
    void testDeleteUserUseCase() throws Exception {
        final long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", id)).andExpect(MockMvcResultMatchers.status().is(204));
        BDDMockito.verify(userService).deleteUser(id);
    }
}
