package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

public class UserDataAccessTests {

    private static SQLUserDataAccess sqlUserDataAccess;

    @BeforeAll
    public static void init() throws Exception {
        SQLDataAccess.configureDatabase();
    }

    @BeforeEach
    public void setup() {
        sqlUserDataAccess = new SQLUserDataAccess();
    }

    @AfterEach
    public void reset() throws Exception {
        DatabaseManager.clearDatabase();
    }

    @Test
    public void createSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            UserData userData1 = new UserData("user1", "password", "email1@email.com");
            UserData userData2 = new UserData("user2", "password", "email2@email.com");
            UserData userData3 = new UserData("user3", "password", "email3@email.com");

            sqlUserDataAccess.createUser(userData1);
            sqlUserDataAccess.createUser(userData2);
            sqlUserDataAccess.createUser(userData3);
        });
    }

    @Test
    public void createFailureBadInput() {
        Assertions.assertThrows(UserDataAccessException.class, () -> {
            UserData userData = new UserData(null, null, null);
            sqlUserDataAccess.createUser(userData);
        });
    }

    @Test
    public void getSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            UserData userData = new UserData("user1", "password", "email@email.com");
            sqlUserDataAccess.createUser(userData);
            sqlUserDataAccess.getUser(userData.username());
        });
    }

    @Test
    public void getFailureNoUser() {
        Assertions.assertDoesNotThrow(() -> {
            UserData result = sqlUserDataAccess.getUser("user1");
            Assertions.assertNull(result);
        });
    }

    @Test
    public void updateSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            UserData userData = new UserData("user1", "password", "email@email.com");
            UserData userDataToUpdate = new UserData(userData.username(), "new", "new@email.com");
            sqlUserDataAccess.createUser(userData);
            int result = sqlUserDataAccess.update(userDataToUpdate);
            UserData userDataResult = sqlUserDataAccess.getUser(userData.username());

            Assertions.assertNotNull(userDataResult);
            assert userDataResult.username().equals(userDataToUpdate.username());
            assert userDataResult.password().equals(userDataToUpdate.password());
            assert userDataResult.email().equals(userDataToUpdate.email());
            assert result == 1;
        });
    }

    @Test
    public void updateFailureBadInput() {
        Assertions.assertThrows(UserDataAccessException.class, () -> {
            UserData userData = new UserData("user1", "password", "email@email.com");
            sqlUserDataAccess.createUser(userData);
            UserData userDataToUpdate = new UserData("user1", null, null);
            sqlUserDataAccess.update(userDataToUpdate);
        });
    }

    @Test
    public void deleteSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            UserData userData1 = new UserData("user1", "password", "email1@email.com");
            UserData userData2 = new UserData("user2", "password", "email2@email.com");
            UserData userData3 = new UserData("user3", "password", "email3@email.com");

            sqlUserDataAccess.createUser(userData1);
            sqlUserDataAccess.createUser(userData2);
            sqlUserDataAccess.createUser(userData3);

            int result1 = sqlUserDataAccess.delete(userData1.username());
            int result2 = sqlUserDataAccess.delete(userData2.username());
            int result3 = sqlUserDataAccess.delete(userData3.username());

            assert result1 == 1;
            assert result2 == 1;
            assert result3 == 1;
        });
    }

    @Test
    public void deleteFailureBadToken() {
        Assertions.assertDoesNotThrow(() -> {
            int result = sqlUserDataAccess.delete("username");

            assert result == 0;
        });

    }
}
