package ui;

import chess.request.LoginRequest;
import chess.request.RegisterRequest;
import exception.ResponseException;
import serverfacade.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {

    private State state = State.LOGGEDOUT;
    private String username;
    private String password;
    private String email;
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
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "exit" -> "exit";
            default -> help();
        };
    }

    public String register(String... params) {
        if (params.length == 3) {
            state = State.LOGGEDOUT;
            username = params[0];
            password = params[1];
            email = params[2];
            try {
                server.register(new RegisterRequest(username, password, email));
            } catch (ResponseException e) {
                return ExceptionHandler.handle(e);
            }
        } else {
            return "Expected <USERNAME> <PASSWORD> <EMAIL>";
        }
        return "You are now registered.";
    }

    public String login(String... params) {
        if (params.length == 2) {
            state = State.LOGGEDIN;
            username = params[0];
            password = params[1];
            try {
                server.login(new LoginRequest(username, password));
            } catch (ResponseException e) {
                return ExceptionHandler.handle(e);
            }
        } else {
            return "Expected <USERNAME> <PASSWORD>";
        }
        return "You are now logged in.";
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
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must log in");
        }
    }
}





