package com.baklavatiramisu.learn.springjpa.status;

import com.baklavatiramisu.learn.springjpa.DataSourceConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(DataSourceConfiguration.class)
public class StatusServiceTests {
    @Autowired
    private StatusService statusService;

    @Test
    @DisplayName("Test getStatusById to return correct status")
    void testGetStatusByIdToReturnCorrectStatus() {
        StatusEntity status = statusService.getStatusById(1L);
        Assertions.assertEquals(1L, status.getId());
        Assertions.assertEquals("Hello, World!", status.getStatus());
    }
}
