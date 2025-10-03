package com.baklavatiramisu.learn.springjpa.user;

import com.baklavatiramisu.learn.springjpa.user.controller.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
@Import(StubUserService.class)
@DisplayName("UserController tests with stub implementation of UserService dependency")
public class UserControllerStubTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test /users/{id} will contact StubUserService for a UserEntity and returns it")
    public void testFoo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.handle").value("teststubuser"));
    }
}
