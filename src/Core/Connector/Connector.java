package Core.Connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    public static Connection postgres(String host,int port,String db,String user,String password) throws SQLException {
        String url = String.format("jdbc:postgresql://%s:%d/%s", host, port, db);
        return DriverManager.getConnection(url,user,password);
    }
}
