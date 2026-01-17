package Core.Server;

import Core.Controllers.ControllerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import Core.Controllers.ControllerManager;
import Core.Utils.Logger;

public class Server {
    ServerSocket socket;
    public static AtomicInteger clients = new AtomicInteger(0);
    ExecutorService pool;
    public ControllerManager Controllers;
    int port;

    public Server(int port) throws IOException {
        socket = new ServerSocket(port);
        this.port=port;
        pool = Executors.newCachedThreadPool();
        this.Controllers = ControllerManager.Instance;
    }

    public void run() throws IOException {
        Logger.Log("Server is running on : localhost:"+port);
        while (!socket.isClosed())
        {
            Socket listener = socket.accept();
            clients.incrementAndGet();
            pool.execute(new ServerThread(listener));
        }
        Logger.Log("Server is closing.");
    }

}
