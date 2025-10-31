package sql.tests;

import dataaccess.exceptions.AuthDataAccessException;
import dataaccess.sqldao.DatabaseManager;
import dataaccess.sqldao.SQLAuthDataAccess;
import dataaccess.sqldao.SQLDataAccess;
import model.AuthData;
import service.UserService;
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
            AuthData authData1 = new AuthData(UserService.generateToken(), "user1");
            AuthData authData2 = new AuthData(UserService.generateToken(), "user2");
            AuthData authData3 = new AuthData(UserService.generateToken(), "user3");
            AuthData authData4 = new AuthData(UserService.generateToken(), "user1");

            sqlAuthDataAccess.create(authData1);
            sqlAuthDataAccess.create(authData2);
            sqlAuthDataAccess.create(authData3);
            sqlAuthDataAccess.create(authData4);
        });
    }

    @Test
    public void createFailureBadInput() {
        Assertions.assertThrows(AuthDataAccessException.class, () -> {
            AuthData authData = new AuthData(null, null);
            sqlAuthDataAccess.create(authData);
        });
    }

    @Test
    public void getSuccess() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData authData = new AuthData(UserService.generateToken(), "user1");
            sqlAuthDataAccess.create(authData);
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

            AuthData authData1 = new AuthData(UserService.generateToken(), "user1");
            AuthData authData2 = new AuthData(UserService.generateToken(), "user2");
            AuthData authData3 = new AuthData(UserService.generateToken(), "user3");
            AuthData authData4 = new AuthData(UserService.generateToken(), "user1");

            sqlAuthDataAccess.create(authData1);
            sqlAuthDataAccess.create(authData2);
            sqlAuthDataAccess.create(authData3);
            sqlAuthDataAccess.create(authData4);

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
