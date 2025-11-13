package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.request.*;
import chess.result.*;
import exception.ResponseException;
import serverfacade.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {

    private State state = State.LOGGEDOUT;
    private String username;
    private String password;
    private Session session;
    private final ServerFacade server;

    public ChessClient(String serverUrl) {
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
                case "logout" -> logout();
                case "list" -> listGames();
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
        CreateGameResult createGameResult;
        if (params.length == 1) {
            String gameName = params[0];
            try {
                createGameResult = server.createGame(new CreateGameRequest(gameName, session.authToken()));
            } catch (ResponseException e) {
                return ExceptionHandler.handle(e);
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected <NAME>");
        }
        return String.format("Created game with id %s", createGameResult.gameID());
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
        for (ListGamesResultBuilder game : currentGames.games()) {
            String gameStr = String.format("Game %s | ID %d | White Player %s | Black Player %s\n",
                    game.gameName(),
                    game.gameID(),
                    game.whiteUsername(),
                    game.blackUsername());
            allGames.append(gameStr);
        }
        return (allGames.toString().isEmpty()) ? "No games, try creating one." : allGames.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertLoggedIn();
        String gameID;
        String playerColor;

        if (params.length == 2) {
            gameID = params[0];
            playerColor = params[1].toLowerCase();
            Integer gameIdInt = parseGameId(gameID);

            try {
                JoinGameResult joinGameResult = server.joinGame(new JoinGameRequest(session.authToken(), playerColor, gameIdInt));
            } catch (ResponseException e) {
                return ExceptionHandler.handle(e);
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected <ID> [WHITE | BLACK]");
        }
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        BoardPrinter.printBoard(chessBoard, playerColor.equals("white"));
        return String.format("Joined game with id %s", gameID);
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





