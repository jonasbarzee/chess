package service;

import chess.request.LoginRequest;
import chess.request.LogoutRequest;
import chess.request.RegisterRequest;
import chess.result.LoginResult;
import chess.result.LogoutResult;
import chess.result.RegisterResult;
import dataaccess.AuthDataAccess;
import dataaccess.UserDataAccess;
import dataaccess.DataAccessException;
import dataaccess.SQLDataAccessException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    public UserService(UserDataAccess memUserDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = memUserDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException, BadRequestException, SQLDataAccessException {

        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();

        if (username == null || password == null || email == null) {
            throw new BadRequestException("Bad request");
        }

        String encryptedPassword = encryptPassword(password);

        UserData userData = new UserData(username, encryptedPassword, email);

        try {
            userDataAccess.createUser(userData);
            AuthData authData = authDataAccess.create(userData.username());
            return new RegisterResult(authData.username(), authData.authToken());
        } catch (DataAccessException ex) {
            throw new AlreadyTakenException("Username is already taken.");
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws NoUserException, WrongPasswordException, BadRequestException {
        String username = loginRequest.username();
        String password = loginRequest.password();

        if (username == null) {
            throw new BadRequestException("username is null");
        } else if (!userDataAccess.userExists(username)) {
            throw new NoUserException("Given username is not registered.");
        }
        UserData userData = userDataAccess.getUser(username);
        System.out.println(password);
        String encrypted = userData.password();
        System.out.println(encrypted);
        System.out.println(checkPassword(encrypted, password));

        System.out.println("clearPassword: " + password);
        System.out.println("storedHash: " + encrypted);

        if (encrypted == null) {
            System.out.println("storedHash is null!");
        } else if (!encrypted.startsWith("$2")) {
            System.out.println("storedHash looks invalid!");
        }

        if (loginRequest.password() == null) {
            throw new BadRequestException("password is null");
        } else if (!checkPassword(encrypted, password)) {
            throw new WrongPasswordException("Given password was incorrect.");
        }

        AuthData authData = authDataAccess.update(username);
        return new LoginResult(authData.authToken(), authData.username());

    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws UnauthorizedException {
        String authToken = logoutRequest.authToken();

        if (!authDataAccess.isAuthorized(authToken)) {
            throw new UnauthorizedException("user is not authorized");
        }

        if (authToken == null) {
            throw new UnauthorizedException("authToken is null");
        } else if (!authDataAccess.isAuthorized(authToken)) {
            throw new UnauthorizedException("authToken is bad");
        }
        if (authDataAccess.isAuthorized(authToken)) {
            authDataAccess.delete(authToken);
        }
        return new LogoutResult();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    private static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private static boolean checkPassword(String encryptedPass, String clearPass) {
        return BCrypt.checkpw(clearPass, encryptedPass);
    }
}