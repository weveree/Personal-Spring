import TestControllers.HomeController;
import Core.Server.Server;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        Server server = new Server(9999);
        server.Controllers.RegisterController(new HomeController());
        server.run();
    }
}
