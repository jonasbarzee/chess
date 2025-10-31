package SQLDataAccessTests;

import dataaccess.exceptions.AuthDataAccessException;
import dataaccess.sqldao.DatabaseManager;
import dataaccess.sqldao.SQLAuthDataAccess;
import dataaccess.sqldao.SQLDataAccess;
import model.AuthData;
import org.junit.jupiter.api.*;

public class AuthDataAccessTests {

    private static SQLAuthDataAccess sqlAuthDataAccess;

    @BeforeAll
    public static void init() throws Exception {
        SQLDataAccess.configureDatabase();
    }

    @BeforeEach
    public void setup() throws Exception {
        try {
            sqlAuthDataAccess = new SQLAuthDataAccess();
        } catch (AuthDataAccessException e) {
            throw new Exception(String.format("Failed test setup, %s", e.getMessage()));
        }
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
            AuthData authdata = sqlAuthDataAccess.create("user1");
            sqlAuthDataAccess.get(authdata.authToken());
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

            int result1 = sqlAuthDataAccess.delete(authData1.authToken());
            int result2 = sqlAuthDataAccess.delete(authData2.authToken());
            int result3 = sqlAuthDataAccess.delete(authData3.authToken());
            int result4 = sqlAuthDataAccess.delete(authData4.authToken());

            assert result1 == 1;
            assert result2 == 1;
            assert result3 == 1;
            assert result4 == 1;
        });
    }

    @Test
    public void deleteFailureBadToken() {
        Assertions.assertDoesNotThrow(() -> {
            int result1 = sqlAuthDataAccess.delete("authToken");

            assert result1 == 0;
        });

    }
}
