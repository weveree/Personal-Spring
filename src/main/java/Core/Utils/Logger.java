package Core.Utils;

import java.time.LocalDateTime;

public class Logger {
    public static void Log(String content)
    {
        String message = String.format("================ %s -- %s", LocalDateTime.now().toString(),content);
        System.out.println(message);
    }
}
