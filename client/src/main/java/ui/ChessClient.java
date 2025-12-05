package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.request.*;
import chess.result.*;
import com.google.gson.Gson;
import exception.ResponseException;
import gamecatalog.GameCatalog;
import model.Session;
import serverfacade.ServerFacade;
import websocket.ClientMessageHandler;
import websocket.WebSocketClient;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.util.*;

public class ChessClient {

    private State state = State.LOGGEDOUT;
    private String username;
    private String password;
    private Session session;
    private String playerColor;
    private Integer gameId;
    private ChessGame currentGame;
    private final GameCatalog gameCatalog = new GameCatalog();
    private final ServerFacade server;
    private final String serverUrl;
    private final Gson gson = new Gson();

    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
   }

    public void run() {
        System.out.println("Welcome to Jonas' CS 240 Chess Client. Login or Register to start. Type Help for commands.");
        System.out.println(help());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("exit")) {
            printPrompt();
            String line = scanner.nextLine();

            result = evalPreLogin(line);
            System.out.print(result);
        }
        System.out.println();
        session = null;
    }

    public String evalPreLogin(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> create(params);
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "list" -> listGames();
                case "move" -> makeMove(params);
                case "exit" -> "exit";
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            username = params[0];
            password = params[1];
            String email = params[2];
            try {
                RegisterResult registerResult = server.register(new RegisterRequest(username, password, email));
                session = new Session(registerResult.username(), registerResult.authToken());
                state = State.LOGGEDIN;
            } catch (ResponseException e) {
                return ExceptionHandler.handle(e);
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected <USERNAME> <PASSWORD> <EMAIL>");
        }
        return "You are now registered.";
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            username = params[0];
            password = params[1];
            try {
                LoginResult loginResult = server.login(new LoginRequest(username, password));
                session = new Session(loginResult.username(), loginResult.authToken());
                state = State.LOGGEDIN;
            } catch (ResponseException e) {
                return ExceptionHandler.handle(e);
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected <USERNAME> <PASSWORD>");
        }
        return "You are now logged in.";
    }

    public String logout() throws ResponseException {
        assertLoggedIn();
        try {
            server.logout(new LogoutRequest(session.authToken()));
            session = null;
            state = State.LOGGEDOUT;
        } catch (ResponseException e) {
            return ExceptionHandler.handle(e);
        }
        return "You are now logged out.";
    }

    public String create(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length != 1) {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected <NAME>");
        }

        String gameName = params[0];
        try {
            server.createGame(new CreateGameRequest(gameName, session.authToken()));
        } catch (ResponseException e) {
            return ExceptionHandler.handle(e);
        }

        return "Created game.";
    }

    public String listGames() throws ResponseException {
        assertLoggedIn();
        ListGamesResult currentGames;
        try {
            currentGames = server.listGames(new ListGamesRequest(session.authToken()));
        } catch (ResponseException e) {
            return ExceptionHandler.handle(e);
        }
        StringBuilder allGames = new StringBuilder();
        gameCatalog.updateGameCatalog(currentGames);
        Collection<ListGamesResultBuilder> games = gameCatalog.getGames();
        for (ListGamesResultBuilder game : games) {
            String gameStr = String.format("Game %s | ID %d | White Player %s | Black Player %s\n",
                    game.gameName(),
                    gameCatalog.getClientIdFromServerId(game.gameID()),
                    game.whiteUsername(),
                    game.blackUsername());
            allGames.append(gameStr);
        }
        return (allGames.toString().isEmpty()) ? "No games, try creating one." : allGames.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length != 2) {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected <ID> [WHITE | BLACK]");
        }

        String gameId = params[0];
        String playerColor = params[1].toLowerCase();
        Integer gameIdInt = parseGameId(gameId);

        Integer serverId = gameCatalog.getServerIdFromClientId(gameIdInt);
        if (serverId == null) {
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid game ID: not a valid game.");
        }

        try {
             server.joinGame(new JoinGameRequest(session.authToken(), playerColor, serverId));
        } catch (ResponseException e) {
            return ExceptionHandler.handle(e);
        }
        this.playerColor = playerColor;
        this.gameId = gameIdInt;

        // open connection and build the command
        WebSocketClient webSocketClient = new WebSocketClient(serverUrl, new ClientMessageHandler(this));
        UserGameCommand joinGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, session.authToken(), gameIdInt);
        webSocketClient.send(gson.toJson(joinGameCommand));

        return String.format("Joined game with id %s", gameIdInt);
    }

    public String makeMove(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length != 2) {
           throw new ResponseException(ResponseException.Code.ClientError, "Expected <start pos> <end pos>");
        }

        String start = params[0];
        String end = params[1];
        ChessMove move = parseMove(start, end);

        UserGameCommand makeMoveCommand = new MakeMoveCommand(session.authToken(), gameId, move);
        WebSocketClient webSocketClient = new WebSocketClient(serverUrl, new ClientMessageHandler(this));
        webSocketClient.send(gson.toJson(makeMoveCommand));
        return "Move: " + move + "submitted";
    }

    public String observeGame(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length != 1) {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected <ID>");
        }

        Integer gameId = parseGameId(params[0]);
        Integer serverIdFromClientId = gameCatalog.getServerIdFromClientId(gameId);

        if (serverIdFromClientId == null) {
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid game ID: not a valid game.");
        }

        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        BoardPrinter.printBoard(chessBoard, true);
        return String.format("Observing game with id %d", gameId);
    }

    public void drawBoard(ChessBoard chessBoard) {
        boolean whitePerspective = switch (playerColor) {
            case "white" -> true;
            case "black" -> false;
            default -> true;
        };
        BoardPrinter.printBoard(chessBoard, whitePerspective);
    }

    public void setCurrentGame(ChessGame chessGame) {
        this.currentGame = chessGame;
    }

    private ChessMove parseMove(String start, String end) throws ResponseException {
        try {
            ChessPosition startPos = parsePosition(start);
            ChessPosition endPos = parsePosition(end);
            return new ChessMove(startPos, endPos, null);
        } catch (ResponseException e) {
            ExceptionHandler.handle(e);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Couldn't parse move");
   }

    private ChessPosition parsePosition(String position) throws ResponseException {
        if (position.length() != 2) {
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid coordinates for move");
        }
        char fileChar = position.charAt(0);
        char rankChar = position.charAt(1);

        int col = fileChar - 'a' + 1;
        int row = rankChar - '0';

        if (row < 1 || row > 8 || col < 1 || col > 8) {
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid coordinates for move");
        }

        return new ChessPosition(row, col);
    }

    private Integer parseGameId(String string) throws ResponseException {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid game ID: must be an integer.");
        }
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> ");
    }

    private String help() {
        switch (state) {
            case LOGGEDOUT -> {
                return """
                        register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                        login <USERNAME> <PASSWORD> - to login to the chess server
                        exit - exit the Chess Client
                        help - print possible commands
                        """;

            }
            case LOGGEDIN -> {
                return """
                        create <NAME> - create a game with the given name
                        list - list current games
                        join <ID> [WHITE | BLACK] - join a game with the given ID and player color
                        observe <ID> - observe a game with the given ID
                        logout - logout of chess server
                        exit - exit the Chess Client
                        help - print possible commands
                        """;
            }
            case INGAME -> {
                return """
                        exit - exit the joined chess game
                        """;
            }
            default -> {
                return """
                        """;
            }
        }
    }

    private void assertLoggedIn() throws ResponseException {
        if (state == State.LOGGEDOUT || session == null) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must log in");
        }
    }
}





