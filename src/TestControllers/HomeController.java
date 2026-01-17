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
    HomeService homeService;

    public HomeController() {
        homeService=new HomeService();
    }

    @Route("/")
    public ResponseEntity home(Object o) {
        return new ResponseEntity(200, ResponseEntityFormat.JSON, Map.of("message",homeService.getAll()));
    }

    @Route("/exit")
    public ResponseEntity exit(Object o) {
        return new ResponseEntity(200, ResponseEntityFormat.JSON, Map.of("message","bye"));
    }
}
