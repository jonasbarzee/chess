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
        Assertions.assertThrows(AuthDataAccessException.class, () -> {
            sqlAuthDataAccess.create(null);
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
}
