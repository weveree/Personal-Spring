package TestControllers;

import Core.Controllers.Controller;
import Core.Response.ResponseEntity;
import Core.Response.ResponseEntityFormat;
import Core.Routes.Route;

import java.util.Map;

@Controller("/api/v1/home")
public class HomeController{

    @Route(path = "/", method = "GET")
    public ResponseEntity home(Object o) {
        return new ResponseEntity(200, ResponseEntityFormat.JSON, Map.of("message","hi"));
    }
    @Route(path = "/exit", method = "GET")
    public ResponseEntity exit(Object o) {
        return new ResponseEntity(200, ResponseEntityFormat.JSON, Map.of("message","bye"));
    }
}
