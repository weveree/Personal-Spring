package Core.Response;

public enum ResponseEntityFormat {
    JSON("application/json"), TEXT("plain/text");
    String value;

    ResponseEntityFormat(String contentType) {
        value=contentType;
    }

    @Override
    public String toString() {
        return value;
    }
}
