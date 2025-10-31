import dataaccess.exceptions.SQLDataAccessException;
import dataaccess.sqldao.SQLDataAccess;
import org.junit.jupiter.api.BeforeAll;
import server.Server;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.run(8080);

        System.out.println("♕ 240 Chess Server");


//        try {
//            SQLDataAccess.configureDatabase();
//        } catch (SQLDataAccessException e) {
//            throw new Exception("Error");
//        }

    }
} 