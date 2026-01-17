package TestControllers;

import Core.Controllers.Controller;
import Core.HTML.HTML;
import Core.Response.ResponseEntity;
import Core.Response.ResponseEntityFormat;
import Core.Routes.Route;
import Core.Server.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    @Route(value = "/",method = "POST")
    public ResponseEntity home_post(Request o) {
        System.out.println(o.getBody());
        return new ResponseEntity(200, ResponseEntityFormat.JSON, Map.of("message",homeService.getAll()));
    }
    @Route("/html")
    public ResponseEntity htmlTest(Object o) throws IOException {
        return new ResponseEntity(200, ResponseEntityFormat.HTML,
                HTML.Instance.Render("src/Templates/testTemplate.template.html",Map.of("title","Salut comment vas-tu ?  ")));
    }
    @Route("/exit")
    public ResponseEntity exit(Object o) {
        return new ResponseEntity(200, ResponseEntityFormat.JSON, Map.of("message","bye"));
    }
}
