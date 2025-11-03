package service;

import chess.ChessGame;
import chess.request.CreateGameRequest;
import chess.request.JoinGameRequest;
import chess.request.ListGamesRequest;
import chess.result.CreateGameResult;
import chess.result.JoinGameResult;
import chess.result.ListGamesResult;
import chess.result.ListGamesResultBuilder;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameService {

    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;
    private Integer gameID;

    public GameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
        gameID = 0;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ChessServerException {

        if (createGameRequest.gameName() == null) {
            throw new BadRequestException("gameName is null");
        }

        try {
            if (!authDataAccess.isAuthorized(createGameRequest.authToken())) {
                throw new UnauthorizedException("Unauthorized");
            }
            // game id is a placeholder right now, the real game id comes back from the database and is returned in the create game result
            gameID += 1;
            GameData gameData = new GameData(gameID, null, null, createGameRequest.gameName(), new ChessGame());
            Integer gameIDToReturn = gameDataAccess.createGameData(gameData);
            return new CreateGameResult(gameIDToReturn);
        } catch (DataAccessException e) {
            throw ServiceExceptionMapper.map(e);
        }

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

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws ChessServerException {
        String authToken = joinGameRequest.authToken();
        Integer gameID = joinGameRequest.gameID();
        String playerColor = joinGameRequest.playerColor();

        if (gameID == null || authToken == null || playerColor == null) {
            throw new BadRequestException("Missing required field.");
        }
        playerColor = playerColor.toLowerCase();
        if (!playerColor.equals("white") && !playerColor.equals("black")) {
            throw new BadRequestException("Invalid player color.");
        }

        // Authorization check before updating through DAO
        String username;
        try {
            username = authDataAccess.getUsername(authToken);
            System.out.println(username);
            if (username == null || !authDataAccess.isAuthorized(authToken)) {
                throw new UnauthorizedException("Unauthorized.");
            }

        } catch (DataAccessException e) {
//            throw new UnauthorizedException("Bad Request.");
            throw ServiceExceptionMapper.map(e);
        }

        GameData gameData;
        try {
            gameData = gameDataAccess.getGame(gameID);
            if (gameData == null) {
                throw new BadRequestException("Game not found.");
            }
        } catch (DataAccessException e) {
            throw new BadRequestException("Game not found.");
        }

        if (!canJoinAsColor(playerColor, gameID)) {
            throw new AlreadyTakenException("Cannot join with given player color.");
        }

        try {
            switch (playerColor) {
                case "white" -> {
                    GameData updateForWhite = new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(), gameData.game());
                    gameDataAccess.updateGameData(updateForWhite);
                }
                case "black" -> {
                    GameData updateForBlack = new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
                    gameDataAccess.updateGameData(updateForBlack);
                }
            }
        } catch (DataAccessException e) {
            throw new UnauthorizedException("error");
        }
        return new JoinGameResult();
    }


    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws ChessServerException {
        String authToken = listGamesRequest.authToken();
        Collection<ListGamesResultBuilder> listForResult = new ArrayList<>();

        try {
            if (!authDataAccess.isAuthorized(authToken)) {
                throw new UnauthorizedException("Unauthorized");
            }

            Collection<GameData> gamesData = gameDataAccess.getGames();
            for (GameData gameData : gamesData) {
                Integer gameID = gameData.gameID();
                String whiteUsername = gameData.whiteUsername();
                String blackUsername = gameData.blackUsername();
                String gameName = gameData.gameName();
                ListGamesResultBuilder listGamesResultBuilder = new ListGamesResultBuilder(gameID, whiteUsername, blackUsername, gameName);
                listForResult.add(listGamesResultBuilder);
            }
            return new ListGamesResult(listForResult);

        } catch (DataAccessException e) {
            throw ServiceExceptionMapper.map(e);
        }

    }
}
