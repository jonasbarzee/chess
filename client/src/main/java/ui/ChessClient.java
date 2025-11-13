package ui;

import chess.request.CreateGameRequest;
import chess.request.LoginRequest;
import chess.request.LogoutRequest;
import chess.request.RegisterRequest;
import chess.result.CreateGameResult;
import chess.result.LoginResult;
import chess.result.LogoutResult;
import chess.result.RegisterResult;
import exception.ResponseException;
import serverfacade.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {

    private State state = State.LOGGEDOUT;
    private String username;
    private String password;
    private String email;
    private String gameName;
    private Session session;
    private Integer gameID;
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
    }

    public String evalPreLogin(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> create(params);
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
            email = params[2];
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
        if (params.length == 1) {
            gameName = params[0];
            try {
                CreateGameResult createGameResult = server.createGame(new CreateGameRequest(gameName, session.authToken()));
                gameID = createGameResult.gameID();
            } catch (ResponseException e) {
                return ExceptionHandler.handle(e);
            }
        } else {
            throw new ResponseException(ResponseException.Code.ClientError, "Expected <NAME>");
        }
        return String.format("Created game with id %d", gameID);
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





