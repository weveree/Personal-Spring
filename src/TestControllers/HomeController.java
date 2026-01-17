package TestControllers;

import Core.Connector.Connector;
import Core.Controllers.Controller;
import Core.Persistency.PostgresPersist;
import Core.Response.ResponseEntity;
import Core.Response.ResponseEntityFormat;
import Core.Routes.Route;

import java.util.Map;

@Controller("/api/v1/home")
public class HomeController{
    TestRepository repo;
    public HomeController() {
        this.repo = new TestRepository(new PostgresPersist(Connector.connection, TestModel.class));
    }

    @Route(path = "/", method = "GET")
    public ResponseEntity home(Object o) {
        return new ResponseEntity(200, ResponseEntityFormat.JSON, Map.of("message",repo.findById("bob").get()));
    }
    @Route(path = "/exit", method = "GET")
    public ResponseEntity exit(Object o) {
        return new ResponseEntity(200, ResponseEntityFormat.JSON, Map.of("message","bye"));
    }
}
