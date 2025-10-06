package com.baklavatiramisu.learn.springboot.user;

import com.baklavatiramisu.learn.springboot.ApplicationSecurityConfig;
import com.baklavatiramisu.learn.springboot.user.controller.CreateUserRequest;
import com.baklavatiramisu.learn.springboot.user.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@WebMvcTest(UserController.class)
@Import({StubUserService.class, ApplicationSecurityConfig.class})
@DisplayName("UserController tests with stub implementation of UserService dependency")
public class UserControllerStubTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StubUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test /users/{id} will contact StubUserService for a UserEntity and returns it")
    @WithMockUser(roles = "user:read")
    public void testQueryUserUseCase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.handle").value("teststubuser"));
    }

    @Test
    @DisplayName("Test POST /users will call createUser in StubUserService")
    @DirtiesContext
    @WithMockUser(roles = "admin")
    void testCreateUserUseCase() throws Exception {
        final String handle = "testuser";
        final String name = "Test User";

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new CreateUserRequest(name, handle)))
                )
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.header().string("location", Matchers.endsWith("/users/2")));

        final Optional<UserEntity> user = userService.findUserById(2L);
        Assertions.assertTrue(user.isPresent());
        final UserEntity userEntity = user.get();
        Assertions.assertEquals(handle, userEntity.getHandle());
        Assertions.assertEquals(name, userEntity.getName());
    }

    @Test
    @DisplayName("Test PUT /users/{id} endpoint will correctly call updateUser method in StubUserService")
    @WithMockUser(roles = "user:write")
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

        final Optional<UserEntity> userOptional = userService.findUserById(id);
        Assertions.assertTrue(userOptional.isPresent());
        final UserEntity user = userOptional.get();
        Assertions.assertEquals(id, user.getId());
        Assertions.assertEquals(name, user.getName());
        Assertions.assertEquals(handle, user.getHandle());
    }

    @Test
    @DisplayName("Test DELETE /users/{id} endpoint will correctly call deleteUser method in StubUserService")
    @WithMockUser(roles = "admin")
    void testDeleteUserUseCase() throws Exception {
        final long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", id)).andExpect(MockMvcResultMatchers.status().is(204));
        Optional<UserEntity> userOptional = userService.findUserById(id);
        Assertions.assertTrue(userOptional.isPresent());
        UserEntity user = userOptional.get();
        Assertions.assertNotNull(user.getDeletedOn());
    }
}
