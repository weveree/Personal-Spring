package Core.Controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import Core.Response.ResponseEntity;
import Core.Routes.Route;

public class ControllerManager {
    public static ControllerManager Instance = new ControllerManager();
    Map<String, Function<Object, ResponseEntity>> routes;

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
                String path = prefix + route.path();

                Function fun = (request)-> {
                    try {
                        return method.invoke(controller,request);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };
                routes.put(path,fun);
                System.out.println("Path "+path+" added");
            }
        }
    }

    public ResponseEntity executeRoute(String route) {
        if(routes.get(route) == null) return null;
        return routes.get(route).apply(null);
    }

}
