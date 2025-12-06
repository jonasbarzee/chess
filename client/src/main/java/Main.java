import chess.*;
import ui.ChessClient;

public class Main {

    public static void main(String[] args) {
        try {
            ChessClient client = new ChessClient("http://localhost:8080");
            client.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Couldn't start client.");
        }

    }
}