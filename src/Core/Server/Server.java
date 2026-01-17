package Core.Server;

import Core.Connector.Connector;
import Core.Controllers.ControllerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import Core.Utils.Logger;

public class Server {
    ServerSocket socket;
    public static AtomicInteger clients = new AtomicInteger(0);
    ExecutorService pool;
    public ControllerManager Controllers;
    int port;
    Properties properties;

    private Server() throws IOException {

        pool = Executors.newCachedThreadPool();
        this.Controllers = ControllerManager.Instance;
    }
    public static Server createServer() throws IOException {
        return new Server();
    }

    public void LoadProperties() throws IOException, SQLException {
        properties = new Properties();
        Path path_to_env = Paths.get(".env");
        properties.load(Files.newInputStream(path_to_env));
        Logger.Log("Configurations loaded.");

        port = Integer.parseInt((String) properties.get("SERVER.PORT"));

        Connector.postgres((String) properties.get("DATABASE.HOST"), Integer.parseInt((String) properties.get("DATABASE.PORT")), (String) properties.get("DATABASE.NAME"), (String) properties.get("DATABASE.USER"), (String) properties.get("DATABASE.PASSWORD"));
    }

    public void run() throws IOException, SQLException {
        LoadProperties();
        socket = new ServerSocket(port);
        Logger.Log("Server is running on : localhost:" + port);

        while (!socket.isClosed()) {
            Socket listener = socket.accept();
            clients.incrementAndGet();
            pool.execute(new ServerThread(listener));
        }
        Logger.Log("Server is closing.");
    }

}
