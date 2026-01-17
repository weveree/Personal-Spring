import Core.Connector.Connector;
import Core.Controllers.ControllerManager;
import TestControllers.HomeController;
import Core.Server.Server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws IOException, SQLException {
        Server server = new Server(9999);
        Connector.postgres("192.168.1.45",5431,"postgres","wawa","wawa");
        server.Controllers.RegisterController(new HomeController());
        server.run();
    }
}
