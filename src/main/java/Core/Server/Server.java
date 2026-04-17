package Core.Server;

import Core.Connector.ConnectorFactory;
import Core.Controllers.ControllerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import Core.Server.ServerInitSteps.DBConnectionInitStepImpl;
import Core.Server.ServerInitSteps.IStepInit;
import Core.Utils.Logger;

public class Server {
    ServerSocket socket;
    public static AtomicInteger clients = new AtomicInteger(0);
    ExecutorService pool;
    public ControllerManager Controllers;
    int port;
    List<IStepInit> initSteps=new ArrayList<IStepInit>();

    private Server(){
        initSteps.add(new DBConnectionInitStepImpl());
        pool = Executors.newCachedThreadPool();
        this.Controllers = ControllerManager.Instance;
    }
    public static Server createServer() throws IOException, SQLException {
        Server server = new Server();
        server.Init();
        return server;
    }


    private void Init() throws IOException {
        for(IStepInit step: initSteps)
        {
            if(step instanceof DBConnectionInitStepImpl)
            {
                port = ((DBConnectionInitStepImpl) step).run();
            }
            else
                step.run();
        }
        socket = new ServerSocket(port);
        Logger.Log("Server is running on : localhost:" + port);
        handleShutdown();
    }

    private void handleShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Logger.Log("\n[SIGNAL] Shutdown signal received (Ctrl+C)");
            pool.shutdownNow();
            try {
                socket.close();
                ConnectorFactory.connection.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, "shutdown-hook"));
    }

    public void run() throws IOException{
        while (!socket.isClosed()) {
            Socket listener = socket.accept();
            clients.incrementAndGet();
            pool.execute(new ServerThread(listener));
        }
        Logger.Log("Server is closing.");
    }

}
