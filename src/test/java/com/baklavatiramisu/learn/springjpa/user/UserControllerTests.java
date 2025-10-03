package com.baklavatiramisu.learn.springjpa.user;

import com.baklavatiramisu.learn.springjpa.user.controller.UserController;
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

@WebMvcTest(UserController.class)
@DisplayName("UserController tests with mocked UserService dependency")
public class UserControllerTests {
    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test /users/{id} will return contact UserService for a UserEntity and returns it")
    void testGetUserByIdMethodWillReturnCorrectUser() throws Exception {
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
}
