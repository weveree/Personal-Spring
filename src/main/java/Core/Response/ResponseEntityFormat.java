package Core.Response;

import java.util.Arrays;

public enum ResponseEntityFormat {
    JSON("application/json"),
    TEXT("text/plain"),
    HTML("text/html; charset=UTF-8"),
    JS("application/javascript"),
    CSS("text/css"),
    PNG("image/png"),
    JPEG("image/jpeg"),
    JPG("image/jpeg"),
    GIF("image/gif"),
    SVG("image/svg+xml"),
    WEBP("image/webp"),
    ICO("image/x-icon"),
    BMP("image/bmp"),
    TIFF("image/tiff");

    String value;

    ResponseEntityFormat(String contentType) {
        value = contentType;
    }

    public static ResponseEntityFormat fromContentType(String contentType) {
        return Arrays.stream(values())
                .filter(f -> f.value.equalsIgnoreCase(contentType))
                .findFirst()
                .orElse(TEXT);
    }
    @Override
    public String toString() {
        return value;
    }
}
