package Core.Response;

public enum ResponseEntityFormat {
    JSON("application/json"), TEXT("plain/text"),HTML("text/html; charset=UTF-8");
    String value;

    ResponseEntityFormat(String contentType) {
        value=contentType;
    }

    @Override
    public String toString() {
        return value;
    }
}
