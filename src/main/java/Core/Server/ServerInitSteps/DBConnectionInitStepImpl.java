package Core.Server.ServerInitSteps;

import Core.Connector.ConnectorFactory;
import Core.Exception.UnknownDatabaseTypeException;
import Core.Utils.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionInitStepImpl implements IStepInit<Integer>{
    public Integer LoadProperties() throws IOException, SQLException, UnknownDatabaseTypeException {
        Properties properties = new Properties();
        Path path_to_env = Paths.get(".env");
        properties.load(Files.newInputStream(path_to_env));
        Logger.Log("Configurations loaded.");

        int port = Integer.parseInt((String) properties.get("SERVER.PORT"));

        ConnectorFactory.connect((String) properties.get("DATABASE.TYPE"),(String) properties.get("DATABASE.HOST"), Integer.parseInt((String) properties.get("DATABASE.PORT")), (String) properties.get("DATABASE.NAME"), (String) properties.get("DATABASE.USER"), (String) properties.get("DATABASE.PASSWORD"));
        return port;
    }
    @Override
    public Integer run() {
        try {
            return LoadProperties();
        } catch (IOException | SQLException | UnknownDatabaseTypeException e) {
            throw new RuntimeException(e);
        }
    }
}
