package Core.Server;

import Core.Controllers.ControllerManager;
import Core.Exception.CORSException;
import Core.Exception.FilterException;
import Core.Response.ResponseEntity;
import Core.Response.ResponseEntityFormat;
import Core.Security.SecurityManager;

import java.io.*;
import java.net.Socket;
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
        listener.close();
    }

    private void sendResponse(ResponseEntity response) throws IOException {
        OutputStream out = listener.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true); // auto-flush
        if(response == null)
            response = new ResponseEntity(404, ResponseEntityFormat.TEXT,"not found");
        writer.print(response);
        writer.flush();
        writer.close();
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
        Request request = Request.parse(request_builder);

        try {
            SecurityManager.Instance.Execute(request);
        } catch (FilterException | CORSException e) {
            return new ResponseEntity(403,ResponseEntityFormat.JSON, Map.of("error",e.getMessage()));
        }
        return ControllerManager.Instance.executeRoute(request.getPath());
    }
}
