package Core.Server;

import Core.Utils.JsonConverter;
import com.google.gson.Gson;

import java.util.List;

public class Request {
    private String method;
    private String path;
    private String content;
    private String origins;
    private String body;

    public Request(String method, String path, String content, String origins,String body) {
        this.method = method;
        this.path = path;
        this.content = content;
        this.origins = origins;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrigins() {
        return origins;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setOrigins(String origins) {
        this.origins = origins;
    }

    public static Request parse(List<String> line, String body)
    {

        String[] headers = line.get(0).split(" ");
        String Accept =  line.stream().filter(obj->obj.contains("Accept:")).findFirst().orElseThrow();
        String[] origins = Accept.substring(8).split(" ");

        return new Request(headers[0],headers[1],"",origins[0], body);
    }
}
