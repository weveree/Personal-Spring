import Core.Controllers.StaticFileController;
import TestControllers.HomeController;
import Core.Server.Server;

import java.io.IOException;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws IOException, SQLException {
        Server server = Server.createServer();
        server.Controllers.RegisterController(new HomeController());
        server.Controllers.RegisterController(new StaticFileController());
        server.run();
    }
}
