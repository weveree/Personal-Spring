package Core.Connector;

import Core.Exception.UnknownDatabaseTypeException;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnector {
    Connection connect(String databaseType,String host,int port,String db,String user,String password) throws SQLException, UnknownDatabaseTypeException;
}
