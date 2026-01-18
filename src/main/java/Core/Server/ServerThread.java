package Core.Server;

import Core.Controllers.ControllerManager;
import Core.Exception.CORSException;
import Core.Exception.FilterException;
import Core.Response.ResponseEntity;
import Core.Response.ResponseEntityFormat;
import Core.Security.SecurityManager;
import Core.Utils.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerThread implements Runnable {
    Socket listener;
    AtomicInteger clientNo;

    public ServerThread(Socket listener) {
        clientNo = Server.clients;
        this.listener = listener;
        try {
            listener.setSoTimeout(30000); // 30 seconds
            listener.setKeepAlive(false);

        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            action();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void action() throws Exception {

        ResponseEntity response = handleRequest(listener.getInputStream());
        sendResponse(response);
        cleanup();
    }

    private void sendResponse(ResponseEntity response) throws IOException {
        OutputStream out = listener.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true); // auto-flush
        if (response == null)
            response = new ResponseEntity(404, ResponseEntityFormat.JSON, Map.of("error","Requested resource not found."));
        writer.print(response);
        writer.flush();
        writer.close();
    }
    private ResponseEntity serveStaticFile(String path) {
        try {
            // "/static/css/style.css" -> "src/static/css/style.css"
            String filePath = path.replace("/api/v1","").replace("/Static/", "src/main/java/Web/Static/");
            Path file = Paths.get(filePath);

            if (!Files.exists(file)) {
                return new ResponseEntity(404, ResponseEntityFormat.TEXT, "File not found");
            }

            String content = Files.readString(file);
            String contentType = getContentType(path);

            return new ResponseEntity(200, ResponseEntityFormat.fromContentType(contentType), content);
        } catch (IOException e) {
            return new ResponseEntity(500, ResponseEntityFormat.TEXT, "Error reading file");
        }
    }
    private String getContentType(String path) {
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg")) return "image/jpeg";
        return "text/plain";
    }
    private ResponseEntity handleRequest(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();

        if (line == null || line.isEmpty()) {
            return null;
        }

        List<String> request_builder = new ArrayList<>();
        while (line != null && !line.isEmpty()) {
            request_builder.add(line);
            line = reader.readLine();
        }
        String body = null;
        int length = 0;
        if (request_builder.stream().filter(obj->obj.contains("Content-Length")).findAny().isPresent())
            if (request_builder.get(8).startsWith("Content-Length"))
                length = Integer.parseInt(request_builder.get(8).substring(16));
        if (length > 0) {
            char[] bodyChars = new char[length];
            reader.read(bodyChars, 0, length);
            body = new String(bodyChars);
        }
        Request request = Request.parse(request_builder, body);

        try {
            SecurityManager.Instance.Execute(request);
        } catch (FilterException | CORSException e) {
            return new ResponseEntity(403, ResponseEntityFormat.JSON, Map.of("error", e.getMessage()));
        }

        // INTERCEPT STATIC FILES
        if (request.getPath().contains("/Static/")) {
            return serveStaticFile(request.getPath());
        }

        return ControllerManager.Instance.executeRoute(request.getPath(), request.getMethod(),request);
    }

    private void cleanup() {
        try {
            if (listener != null && !listener.isClosed()) {
                listener.close();
            }
        } catch (IOException e) {
            Logger.Log("Error closing socket: " + e.getMessage());
        } finally {
            // Decrement client count
            Server.clients.decrementAndGet();
        }
    }
}
