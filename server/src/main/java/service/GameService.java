package service;

import chess.ChessGame;
import chess.request.CreateGameRequest;
import chess.request.JoinGameRequest;
import chess.request.ListGamesRequest;
import chess.result.CreateGameResult;
import chess.result.JoinGameResult;
import chess.result.ListGamesResult;
import chess.result.ListGamesResultBuilder;
import dataaccess.*;
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


    private boolean canJoinAsColor(String color, Integer gameID) throws BadRequestException {
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
            throw new BadRequestException("Bad Request.");
        }
        return false;
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws UnauthorizedException, AlreadyTakenException, GameDataAccessException, BadRequestException {
        String authToken = joinGameRequest.authToken();
        Integer gameID = joinGameRequest.gameID();
        String playerColor = joinGameRequest.playerColor();

        if (gameID == null || authToken == null || playerColor == null) {
            throw new BadRequestException("Bad Request.");
        }
        playerColor = playerColor.toLowerCase();
        if (playerColor.isEmpty() || !playerColor.equals("white") && !playerColor.equals("black")) {
            throw new BadRequestException("Bad request.");
        }

        String username;
        try {
            username = authDataAccess.getUsername(authToken);
        } catch (DataAccessException ex) {
            throw new UnauthorizedException("Bad Request.");
        }

        if (authDataAccess.isAuthorized(authToken)) {
            GameData gameData;
            try {
                gameData = gameDataAccess.getGame(gameID);
            } catch (GameDataAccessException ex) {
                throw new GameDataAccessException("No game with the given gameID.");
            }
            if (canJoinAsColor(playerColor, gameID)) {

                switch (playerColor) {
                    case ("white"):
                        GameData gameDataToUpdateWhite = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
                        gameDataAccess.updateGameData(gameDataToUpdateWhite);
                        break;
                    case ("black"):
                        GameData gameDataToUpdateBlack = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
                        gameDataAccess.updateGameData(gameDataToUpdateBlack);
                        break;
                    default:
                        throw new BadRequestException("Bad Request.");
                }
                return new JoinGameResult();
            }
            throw new AlreadyTakenException("Cannot join with given player color.");
        }
        throw new UnauthorizedException("Unauthorized.");
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws UnauthorizedException {
        String authToken = listGamesRequest.authToken();

        if (authDataAccess.isAuthorized(authToken)) {
            Collection<GameData> gamesData = gameDataAccess.getGames();
            Collection<ListGamesResultBuilder> listForResult = new ArrayList<>();
            for (GameData gameData : gamesData) {
                ListGamesResultBuilder listGamesResultBuilder = new ListGamesResultBuilder(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName());
                listForResult.add(listGamesResultBuilder);
            }
            return new ListGamesResult(listForResult);
        }
        throw new UnauthorizedException("Unauthorized.");
    }
}
