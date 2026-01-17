package Core.Utils;

import com.google.gson.Gson;

public class JsonConverter {
    public static String convert(Object value)
    {
        Gson gson = new Gson();
        return gson.toJson(value);
    }
}
