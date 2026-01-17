package Core.Response;

import Core.Utils.JsonConverter;

public class ResponseEntity {
    int statusCode;
    ResponseEntityFormat contentType;
    Object body;

    public ResponseEntity(int statusCode, ResponseEntityFormat contentType, Object body) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        switch (contentType)
        {
            case JSON:
                this.body = JsonConverter.convert(body);
                break;
            default:
            case TEXT:
                this.body = body;
                break;
        }
    }

    @Override
    public String toString() {
        return "HTTP/1.1 " + statusCode + " " + getStatusText() + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + body.toString().length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body;
    }

    private String getStatusText() {
        switch (statusCode) {
            case 200: return "OK";
            case 400: return "Bad Request";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            default: return "Unknown";
        }
    }
}