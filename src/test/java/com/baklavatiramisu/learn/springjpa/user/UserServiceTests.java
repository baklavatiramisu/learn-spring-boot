package com.baklavatiramisu.learn.springjpa.user;

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
import java.time.format.DateTimeFormatter;

@SpringBootTest
@Import(EmbeddedDataSourceConfiguration.class)
@DisplayName("UserService tests with H2 embedded datasource")
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("Test getUserById will return correct user from database")
    void testGetUserByIdMethodWillReturnCorrectUserFromDatabase() {
        UserEntity user = userService.getUserById(1);
        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals("baklava tiramisu", user.getName());
        Assertions.assertEquals("baklavatiramisu", user.getHandle());
    }

    @Test
    @DisplayName("Test createUser will insert a user record in the database")
    @DirtiesContext
    void testCreateUserMethodWillInsertAUserEntryInDatabase() {
        final String userName = "test user";
        final String userHandle = "testuser100";
        userService.createUser(userName, userHandle);
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT id, name, handle FROM users WHERE handle = ?");
            statement.setString(1, userHandle);
            final ResultSet result = statement.executeQuery();
            Assertions.assertTrue(result.next());
            final long obtainedId = result.getLong("id");
            final String obtainedName = result.getString("name");
            final String obtainedHandle = result.getString("handle");

            Assertions.assertEquals(userName, obtainedName);
            Assertions.assertEquals(userHandle, obtainedHandle);
            Assertions.assertTrue(obtainedId > 0L);
        } catch (SQLException e) {
            Assertions.fail("Failed to open connection to the database");
        }
    }

    @Test
    @DirtiesContext
    @DisplayName("Test updateUser method will alter the existing record in the database")
    void testUpdateUserMethodWillAlterTheRecordInTheDatabase() {
        final String userName = "Baklava Tiramisu";
        final String userHandle = "bt1000";
        userService.updateUser(1L, userName, userHandle);
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT id, name, handle FROM users WHERE handle = ?");
            statement.setString(1, userHandle);
            final ResultSet result = statement.executeQuery();
            Assertions.assertTrue(result.next());
            final long obtainedId = result.getLong("id");
            final String obtainedName = result.getString("name");
            final String obtainedHandle = result.getString("handle");

            Assertions.assertEquals(userName, obtainedName);
            Assertions.assertEquals(userHandle, obtainedHandle);
            Assertions.assertEquals(1L, obtainedId);
        } catch (SQLException e) {
            Assertions.fail("Failed to open connection to the database");
        }
    }

    @Test
    @DirtiesContext
    @DisplayName("Test deleteUser method will mark the record as deleted, but still keeps in the database")
    void testDeleteUserMethodWillMarkTheRecordAsDeletedInDatabase() {
        try (final Connection connection = dataSource.getConnection()) {
            // insert a user
            final String expectedName = "John Doe";
            final String expectedHandle = "johndoe007";
            final String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            final PreparedStatement insertionStatement = connection.prepareStatement("INSERT INTO users (name, handle, created_on, updated_on) VALUES (?, ?, ?, ?);");
            insertionStatement.setString(1, expectedName);
            insertionStatement.setString(2, expectedHandle);
            insertionStatement.setString(3, timestamp);
            insertionStatement.setString(4, timestamp);
            insertionStatement.executeUpdate();

            // get the ID of inserted user
            final PreparedStatement idQuery = connection.prepareStatement("SELECT id FROM users WHERE handle = ?");
            idQuery.setString(1, expectedHandle);
            final ResultSet idResult = idQuery.executeQuery();
            idResult.next();
            long insertedId = idResult.getLong("id");

            // delete the user
            userService.deleteUser(insertedId);

            // check if the user is marked as deleted
            final PreparedStatement deletionQuery = connection.prepareStatement("SELECT id, deleted_on FROM users WHERE id = ?");
            deletionQuery.setLong(1, insertedId);
            final ResultSet deletionResult = deletionQuery.executeQuery();
            Assertions.assertTrue(deletionResult.next());
            final long obtainedId = deletionResult.getLong("id");
            Assertions.assertEquals(insertedId, obtainedId);
            final Timestamp deletedOn = deletionResult.getTimestamp("deleted_on");
            Assertions.assertNotNull(deletedOn);
        } catch (SQLException e) {
            Assertions.fail("Database operation failed", e);
        }
    }
}
