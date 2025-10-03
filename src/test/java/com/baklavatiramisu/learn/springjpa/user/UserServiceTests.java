package com.baklavatiramisu.learn.springjpa.user;

import com.baklavatiramisu.learn.springjpa.EmbeddedDataSourceConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(EmbeddedDataSourceConfiguration.class)
@DisplayName("UserService tests with H2 embedded datasource")
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Test getUserById will return correct user from database")
    void testGetUserByIdMethodWillReturnCorrectUserFromDatabase() {
        UserEntity user = userService.getUserById(1);
        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals("baklava tiramisu", user.getName());
        Assertions.assertEquals("baklavatiramisu", user.getHandle());
    }
}
