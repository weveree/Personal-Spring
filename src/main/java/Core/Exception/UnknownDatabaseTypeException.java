package Core.Exception;

public class UnknownDatabaseTypeException extends Exception {
    public UnknownDatabaseTypeException(String type) {
        super(type);
    }
}
