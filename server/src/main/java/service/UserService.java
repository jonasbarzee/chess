package service;

import chess.request.LoginRequest;
import chess.request.LogoutRequest;
import chess.request.RegisterRequest;
import chess.result.LoginResult;
import chess.result.LogoutResult;
import chess.result.RegisterResult;
import dataaccess.interfaces.AuthDataAccess;
import dataaccess.interfaces.UserDataAccess;
import dataaccess.exceptions.DataAccessException;
import dataaccess.model.AuthData;
import dataaccess.model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    public UserService(UserDataAccess memUserDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = memUserDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ChessServerException {

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
        } catch (DataAccessException e) {
            throw ServiceExceptionMapper.map(e);
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws ChessServerException {
        String username = loginRequest.username();
        String password = loginRequest.password();

        try {
            if (username == null) {
                throw new BadRequestException("username is null");
            } else if (!userDataAccess.userExists(username)) {
                throw new NoUserException("Given username is not registered.");
            }

            UserData userData = userDataAccess.getUser(username);
            String encrypted = userData.password();

            if (loginRequest.password() == null) {
                throw new BadRequestException("password is null");
            } else if (!checkPassword(encrypted, password)) {
                throw new WrongPasswordException("Given password was incorrect.");
            }

            AuthData authData = authDataAccess.update(username);
            return new LoginResult(authData.authToken(), authData.username());

        } catch (DataAccessException e) {
            throw ServiceExceptionMapper.map(e);
        }

    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws ChessServerException {
        String authToken = logoutRequest.authToken();

        try {
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
        } catch (DataAccessException e) {
            throw ServiceExceptionMapper.map(e);
        }

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