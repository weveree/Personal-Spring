package Core.Security;

import Core.Exception.CORSException;
import Core.Exception.FilterException;
import Core.Server.Request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SecurityManager {
    public static SecurityManager Instance = new SecurityManager();
    public List<IFilter> filters;

    private Set<String> accepted_origins;

    public SecurityManager() {
        accepted_origins = new HashSet<>();
        accepted_origins.add("*/*");

        filters = new ArrayList<>();
    }
    public void RegisterFilter(IFilter filter)
    {
        this.filters.add(filter);
    }
    public boolean isOriginAllowed(String origin){
        return accepted_origins.contains(origin);
    }

    public Request Execute(Request request) throws FilterException,CORSException {
        for (IFilter filter:this.filters)
        {
            filter.Intercept(request);
        }
        if(!isOriginAllowed(request.getOrigins()))
            throw new CORSException("CORS");
        return request;
    }
}
