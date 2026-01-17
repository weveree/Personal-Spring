package Core.Exception;

public class RouteAlreadyExistsException extends RuntimeException {
    public RouteAlreadyExistsException() {
      super("Core.Routes.Route already exists");
    }
}
