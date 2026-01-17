package Core.Controllers;

import Core.Response.ResponseEntity;
import Core.Response.ResponseEntityFormat;
import Core.Routes.Route;
import Core.Server.Request;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Controller("/Static")
public class StaticFileController {

    @Route("/css/*")
    public ResponseEntity serveCss(Request request) {
        return serveStaticFile(request.getPath(), ResponseEntityFormat.CSS);
    }

    @Route("/js/*")
    public ResponseEntity serveJs(Request request) {
        return serveStaticFile(request.getPath(), ResponseEntityFormat.JS);
    }

    @Route("/images/*")
    public ResponseEntity serveImage(Request request) {
        String path = request.getPath();
        String contentType = getImageContentType(path);
        return serveStaticFile(path, ResponseEntityFormat.valueOf(contentType));
    }

    private ResponseEntity serveStaticFile(String requestPath, ResponseEntityFormat contentType) {
        try {
            String filePath = requestPath.replace("/static/", "");

            Path path = Paths.get("src/static/" + filePath);

            if (!Files.exists(path)) {
                return new ResponseEntity(404, ResponseEntityFormat.JSON, Map.of("error","File not found"));
            }
            String content = Files.readString(path);

            return new ResponseEntity(200, contentType, content);

        } catch (IOException e) {
            return new ResponseEntity(500, ResponseEntityFormat.TEXT, "Error reading file");
        }
    }

    private String getImageContentType(String path) {
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }
}