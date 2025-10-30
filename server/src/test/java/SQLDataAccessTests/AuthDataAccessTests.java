package SQLDataAccessTests;

import dataaccess.AuthDataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SQLAuthDataAccess;
import dataaccess.SQLDataAccess;
import model.AuthData;
import org.junit.jupiter.api.*;

public class AuthDataAccessTests {

    private static SQLDataAccess sqlDataAccess;
    private static SQLAuthDataAccess sqlAuthDataAccess;

//    @BeforeAll
//    public static void init() throws Exception {
//    }

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
        try {
            sqlAuthDataAccess.create("user1");
            sqlAuthDataAccess.create("user2");
            sqlAuthDataAccess.create("user3");
            sqlAuthDataAccess.create("user1");
        } catch (
                AuthDataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Test
    public void createFailureBadInput() {
        Assertions.assertThrows(AuthDataAccessException.class, () -> {
            sqlAuthDataAccess.create(null);
        });
    }

    @Test
    public void getSuccess() {
        try {
            AuthData authdata = sqlAuthDataAccess.create("user1");
            sqlAuthDataAccess.get(authdata.authToken());
        } catch (AuthDataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
