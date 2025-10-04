package com.baklavatiramisu.learn.springjpa.status;

import com.baklavatiramisu.learn.springjpa.EmbeddedDataSourceConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@Import(EmbeddedDataSourceConfiguration.class)
@DisplayName("StatusService tests with H2 embedded database")
public class StatusServiceTests {
    @Autowired
    private StatusService statusService;

    @Autowired
    private DataSource dataSource;

    @Test
    @DirtiesContext
    @DisplayName("Test createStatus method and expect it to create a record in status_updates table")
    void testCreateStatus() {
        final long userId = 1L;
        final String status = "Hello from JUnit!";
        final StatusEntity entity = statusService.createStatus(userId, status);

        try (final Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM status_updates WHERE id = ?;");
            statement.setLong(1, entity.getId());
            ResultSet resultSet = statement.executeQuery();
            Assertions.assertTrue(resultSet.next());
            final long actualStatusId = resultSet.getLong("id");
            final long actualUserId = resultSet.getLong("user_id");
            final String actualStatus = resultSet.getString("status");
            final Timestamp actualCreatedOn = resultSet.getTimestamp("created_on");
            final Timestamp actualUpdatedOn = resultSet.getTimestamp("updated_on");
            final Timestamp actualDeletedOn = resultSet.getTimestamp("deleted_on");

            Assertions.assertEquals(userId, actualUserId);
            Assertions.assertEquals(status, actualStatus);
            Assertions.assertEquals(entity.getId(), actualStatusId);
            Assertions.assertEquals(
                    actualCreatedOn.toInstant().getEpochSecond(),
                    entity.getCreatedOn().toEpochSecond()
            );
            Assertions.assertEquals(
                    actualUpdatedOn.toInstant().getEpochSecond(),
                    entity.getUpdatedOn().toEpochSecond()
            );
            Assertions.assertNull(actualDeletedOn);
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DisplayName("Test getStatusById to query the status_updates table in the database and return correct status")
    void testGetStatus() {
        final long userId = 1L;
        final long statusId = 1L;
        StatusEntity statusEntity = statusService.getStatusById(userId, statusId);

        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM status_updates WHERE id = ?");
            statement.setLong(1, statusId);
            final ResultSet resultSet = statement.executeQuery();
            Assertions.assertTrue(resultSet.next());
            final long actualStatusId = resultSet.getLong("id");
            final long actualUserId = resultSet.getLong("user_id");
            final String actualStatus = resultSet.getString("status");
            final Timestamp createdOn = resultSet.getTimestamp("created_on");
            final Timestamp updatedOn = resultSet.getTimestamp("updated_on");
            final Timestamp deletedOn = resultSet.getTimestamp("deleted_on");

            Assertions.assertEquals(statusId, actualStatusId);
            Assertions.assertEquals(userId, actualUserId);
            Assertions.assertEquals(statusEntity.getStatus(), actualStatus);

            Assertions.assertEquals(
                    statusEntity.getCreatedOn().toEpochSecond(),
                    createdOn.toInstant().getEpochSecond()
            );
            Assertions.assertEquals(
                    statusEntity.getUpdatedOn().toEpochSecond(),
                    updatedOn.toInstant().getEpochSecond()
            );
            Assertions.assertNull(deletedOn);
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DirtiesContext
    @DisplayName("Test updateStatus to check if it communicates the database to alter the status record in status_updates table")
    void testUpdateStatus() {
        final long userId = 1L;
        final long statusId = 1L;
        final String status = "Testing updated status from JUnit tests";

        statusService.updateStatus(userId, statusId, status);

        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT id, status FROM status_updates WHERE id = ?");
            statement.setLong(1, statusId);
            final ResultSet resultSet = statement.executeQuery();
            Assertions.assertTrue(resultSet.next());
            final long actualId = resultSet.getLong("id");
            final String actualStatus = resultSet.getString("status");
            Assertions.assertEquals(statusId, actualId);
            Assertions.assertEquals(status, actualStatus);
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DirtiesContext
    @DisplayName("Test deleteStatus to update deleted_on field of the status record in status_updates table")
    void testDeleteStatus() {
        final long userId = 1L;
        final long statusId = 1L;
        statusService.deleteStatus(userId, statusId);

        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT deleted_on FROM status_updates WHERE id = ?");
            statement.setLong(1, statusId);
            final ResultSet resultSet = statement.executeQuery();
            Assertions.assertTrue(resultSet.next());
            final Timestamp deletedOn = resultSet.getTimestamp("deleted_on");
            Assertions.assertNotNull(deletedOn);
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }
}
