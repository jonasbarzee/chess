package service;

import chess.ChessGame;
import chess.request.CreateGameRequest;
import chess.request.JoinGameRequest;
import chess.request.ListGamesRequest;
import chess.result.CreateGameResult;
import chess.result.JoinGameResult;
import chess.result.ListGamesResult;
import chess.result.ListGamesResultBuilder;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.GameDataAccessException;
import dataaccess.memdao.MemAuthDataAccess;
import dataaccess.memdao.MemGameDataAccess;
import dataaccess.memdao.MemUserDataAccess;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameService {

    private final MemGameDataAccess memGameDataAccess;
    private final MemAuthDataAccess memAuthDataAccess;
    private Integer gameID = 0;

    public GameService(MemGameDataAccess memGameDataAccess, MemAuthDataAccess memAuthDataAccess, MemUserDataAccess memUserDataAccess) {
        this.memGameDataAccess = memGameDataAccess;
        this.memAuthDataAccess = memAuthDataAccess;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws UnauthorizedException, BadRequestException {

        if (createGameRequest.gameName() == null) {
            throw new BadRequestException("gameName is null");
        }

        if (memAuthDataAccess.isAuthorized(createGameRequest.authToken())) {
            gameID += 1;
            GameData gameData = new GameData(gameID, null, null, createGameRequest.gameName(), new ChessGame());
            memGameDataAccess.createGameData(gameData);
            return new CreateGameResult(gameData.gameID());
        }
        throw new UnauthorizedException("Unauthorized");
    }


    private boolean canJoinAsColor(String color, Integer gameID) throws BadRequestException {
        try {
            GameData gameData = memGameDataAccess.getGame(gameID);

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
            username = memAuthDataAccess.getUsername(authToken);
        } catch (DataAccessException ex) {
            throw new UnauthorizedException("Bad Request.");
        }

        if (memAuthDataAccess.isAuthorized(authToken)) {
            GameData gameData;
            try {
                gameData = memGameDataAccess.getGame(gameID);
            } catch (GameDataAccessException ex) {
                throw new GameDataAccessException("No game with the given gameID.");
            }
            if (canJoinAsColor(playerColor, gameID)) {

                switch (playerColor) {
                    case ("white"):
                        GameData gameDataToUpdateWhite = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
                        memGameDataAccess.updateGameData(gameDataToUpdateWhite);
                        break;
                    case ("black"):
                        GameData gameDataToUpdateBlack = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
                        memGameDataAccess.updateGameData(gameDataToUpdateBlack);
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

        if (memAuthDataAccess.isAuthorized(authToken)) {
            Collection<GameData> gamesData = memGameDataAccess.getGames();
            Collection<ListGamesResultBuilder> listForResult = new ArrayList<>();
            for (GameData gameData : gamesData) {
                Integer gameID = gameData.gameID();
                String whiteUsername = gameData.whiteUsername();
                String blackUsername = gameData.blackUsername();
                String gameName = gameData.gameName();
                ListGamesResultBuilder listGamesResultBuilder = new ListGamesResultBuilder(gameID, whiteUsername, blackUsername, gameName);
                listForResult.add(listGamesResultBuilder);
            }
            return new ListGamesResult(listForResult);
        }
        throw new UnauthorizedException("Unauthorized.");
    }
}
