package Core.Security;

import Core.Exception.FilterException;
import Core.Server.Request;

public interface IFilter {
    void Intercept(Request req) throws FilterException;
}
