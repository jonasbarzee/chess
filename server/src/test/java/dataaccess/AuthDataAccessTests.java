package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;

public class AuthDataAccessTests {

    private static SQLAuthDataAccess sqlAuthDataAccess;

    @BeforeAll
    public static void init() throws Exception {
        SQLDataAccess.configureDatabase();
    }

    @BeforeEach
    public void setup() {
        sqlAuthDataAccess = new SQLAuthDataAccess();
    }

    @AfterEach
    public void reset() throws Exception {
        DatabaseManager.clearDatabase();
    }

    @Test
    public void createSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            sqlAuthDataAccess.create("user1");
            sqlAuthDataAccess.create("user2");
            sqlAuthDataAccess.create("user3");
            sqlAuthDataAccess.create("user1");
        });
    }

    @Test
    public void createFailureBadInput() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlAuthDataAccess.create(null);
        });
    }

    @Test
    public void updateSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            sqlAuthDataAccess.create("user1");
            sqlAuthDataAccess.update("user1");
        });
    }

    @Test
    public void updateFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlAuthDataAccess.update(null);
        });
    }

    @Test
    public void getSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData authData = sqlAuthDataAccess.create("user1");
            sqlAuthDataAccess.get(authData.authToken());
        });
    }

    @Test
    public void getFailureNoUser() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData result = sqlAuthDataAccess.get("user1");
            Assertions.assertNull(result);
        });
    }

    @Test
    public void deleteSuccess() {
        Assertions.assertDoesNotThrow(() -> {


            AuthData authData1 = sqlAuthDataAccess.create("user1");
            AuthData authData2 = sqlAuthDataAccess.create("user2");
            AuthData authData3 = sqlAuthDataAccess.create("user3");
            AuthData authData4 = sqlAuthDataAccess.create("user1");

            sqlAuthDataAccess.delete(authData1.authToken());
            sqlAuthDataAccess.delete(authData2.authToken());
            sqlAuthDataAccess.delete(authData3.authToken());
            sqlAuthDataAccess.delete(authData4.authToken());
        });
    }

    @Test
    public void deleteFailureBadToken() {
        Assertions.assertDoesNotThrow(() -> {
            sqlAuthDataAccess.delete("authToken");
        });

    }

    @Test
    public void deleteAllAuthDataSuccess() {
        Assertions.assertDoesNotThrow(() -> {

            sqlAuthDataAccess.create("user1");
            sqlAuthDataAccess.create("user2");
            sqlAuthDataAccess.create("user3");
            sqlAuthDataAccess.create("user1");

            sqlAuthDataAccess.deleteAllAuthData();
        });

    }

    @Test
    public void deleteAllauthDataNegative() {
        Assertions.assertDoesNotThrow(() -> {
            sqlAuthDataAccess.deleteAllAuthData();
        });

    }

    @Test
    public void isAuthorizedSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData authData = sqlAuthDataAccess.create("user1");
            assert sqlAuthDataAccess.isAuthorized(authData.authToken());
        });
    }

    @Test
    public void isAuthorizedNegative() {
        Assertions.assertDoesNotThrow(() -> {
            assert !sqlAuthDataAccess.isAuthorized("fakeToken");
        });
    }

    @Test
    public void getUsernameSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData authData = sqlAuthDataAccess.create("user1");
            String username = sqlAuthDataAccess.getUsername(authData.authToken());
            Assertions.assertNotNull(username);
        });
    }

    @Test
    public void getUsernameNegative() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlAuthDataAccess.getUsername("fakeToken");
        });
    }
}
