package service;

import chess.ChessGame;
import chess.request.CreateGameRequest;
import chess.request.JoinGameRequest;
import chess.request.ListGamesRequest;
import chess.result.CreateGameResult;
import chess.result.JoinGameResult;
import chess.result.ListGamesResult;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.GameDataAccessException;
import dataaccess.UserDataAccess;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameService {

    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;
    private Integer gameID = 0;

    public GameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess, UserDataAccess userDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws UnauthorizedException, BadRequestException {

        if (createGameRequest.gameName() == null) {
            throw new BadRequestException("gameName is null");
        }

        if (authDataAccess.isAuthorized(createGameRequest.authToken())) {
            gameID += 1;
            GameData gameData = new GameData(gameID, null, null, createGameRequest.gameName(), new ChessGame());
            gameDataAccess.createGameData(gameData);
            return new CreateGameResult(gameData.gameID());
        }
        throw new UnauthorizedException("Unauthorized");
    }


    private boolean canJoinAsColor(String color, Integer gameID) throws GameDataAccessException {
        try {
            GameData gameData = gameDataAccess.getGame(gameID);

            switch (color.toLowerCase()) {
                case ("white"): {
                    if (gameData.whiteUsername() == null) {
                        return true;
                    }
                    return false;
                }
                case ("black"): {
                    if (gameData.blackUsername() == null) {
                        return true;
                    }
                    return false;
                }
            }

        } catch (GameDataAccessException ex) {
            throw new GameDataAccessException("Game doesn't exist.");
        }
        return false;
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws UnauthorizedException, AlreadyTakenException, GameDataAccessException {
        String authToken = joinGameRequest.authToken();
        Integer gameID = joinGameRequest.gameID();

        if (authDataAccess.isAuthorized(authToken)) {
            try {
                GameData gameData = gameDataAccess.getGame(gameID);
                String joinColor = joinGameRequest.playerColor();

                if (canJoinAsColor(joinColor, gameID)) {
                    String username = authDataAccess.getUsername(authToken);

                    switch (joinColor.toLowerCase()) {
                        case ("white"):
                            GameData gameDataToUpdateWhite = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
                            gameDataAccess.updateGameData(gameDataToUpdateWhite);
                            break;
                        case ("black"):
                            GameData gameDataToUpdateBlack = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
                            gameDataAccess.updateGameData(gameDataToUpdateBlack);
                            break;
                    }
                    return new JoinGameResult();
                }
                throw new AlreadyTakenException("Cannot join with given player color.");

            } catch (Exception ex) {
                throw new GameDataAccessException("No game with the given gameID.");
            }
        }
        throw new UnauthorizedException("Unauthorized.");
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws UnauthorizedException {
        String authToken = listGamesRequest.authToken();

        if (authDataAccess.isAuthorized(authToken)) {
            Collection<GameData> games = gameDataAccess.getGames();
            return new ListGamesResult(games);
        }
        throw new UnauthorizedException("Unauthorized.");
    }
}
