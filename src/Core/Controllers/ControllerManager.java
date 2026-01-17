package Core.Controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import Core.Response.ResponseEntity;
import Core.Routes.Route;
import Core.Server.Request;

public class ControllerManager {
    public static ControllerManager Instance = new ControllerManager();
    Map<String, Map<String,Function<Request, ResponseEntity>>> routes;

    public ControllerManager() {
        routes = new HashMap<>();
    }

    public void RegisterController(Object controller) {
        Class<?> _class = controller.getClass();


        for(Method method : _class.getDeclaredMethods())
        {
            if(method.isAnnotationPresent(Route.class))
            {
                Route route = method.getAnnotation(Route.class);
                String prefix="";
                if(_class.getAnnotation(Controller.class) != null)
                {
                    prefix = _class.getAnnotation(Controller.class).value();
                }
                String path = prefix + route.value();

                Function fun = (request)-> {
                    try {
                        return method.invoke(controller,request);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };
                routes.put(route.method(),Map.of(path,fun));
                System.out.println("Path "+path+" added");
            }
        }
    }

    public ResponseEntity executeRoute(String route, String method, Request param) {
        if(routes.get(method) == null) return null;
        if(routes.get(method).get(route) == null) return null;
        return routes.get(method).get(route).apply(param);
    }

}
