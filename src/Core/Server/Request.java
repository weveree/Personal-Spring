package Core.Server;

import java.util.Arrays;
import java.util.List;

public class Request {
    private String method;
    private String path;
    private String content;
    private List<String> origins;
    private String body;

    public Request(String method, String path, String content, List<String> origins, String body) {
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

    public List<String> getOrigins() {
        return origins;
    }

    public String getBody() {
        return body;
    }

    public static String[] extractOrigins(String acceptHeader) {
        if (acceptHeader == null || acceptHeader.isEmpty()) {
            return new String[0];
        }

        return Arrays.stream(acceptHeader.split(","))
                .map(String::trim)
                .map(type -> type.split(";")[0].trim())
                .toArray(String[]::new);
    }
    public static Request parse(List<String> line, String body)
    {

        String[] headers = line.get(0).split(" ");
        String Accept =  line.stream().filter(obj->obj.contains("Accept:")).findFirst().orElseThrow();
        String[] origins = extractOrigins(Accept.substring(8));

        return new Request(headers[0],headers[1],"", Arrays.stream(origins).toList(), body);
    }
}
