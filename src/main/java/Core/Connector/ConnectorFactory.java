package Core.Connector;

import Core.Exception.UnknownDatabaseTypeException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectorFactory implements IConnector {
    public static Connection connection;

    public static Connection postgres(String host,int port,String db,String user,String password) throws SQLException {
        String url = String.format("jdbc:postgresql://%s:%d/%s", host, port, db);
        connection=DriverManager.getConnection(url,user,password);
        return connection;
    }

    @Override
    public Connection connect(String type, String host, int port, String db, String user, String password) throws SQLException, UnknownDatabaseTypeException {
        DatabaseType dbType;
        try {
            dbType = DatabaseType.valueOf(type);
        }
        catch (IllegalArgumentException e)
        {
            throw new UnknownDatabaseTypeException(type);
        }
        switch (dbType)
        {
            case POSTGRES -> postgres(host, port, db, user, password);
        }
        return null;
    }
}
