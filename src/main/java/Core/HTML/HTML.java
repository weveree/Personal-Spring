package Core.HTML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTML {
    public static final String TEMPLATE_PATH = "src/main/java/Web/Templates/";
    public static HTML Instance = new HTML();

    public HTML() {
        this.blocks = new HashMap<>();
    }

    Map<String,String> blocks;

    public static Map<String, String> extractAllBlocks(String template) {
        Map<String, String> blocks = new HashMap<>();

        String pattern = "\\{%\\s*block\\s+['\"]?([\\w]+)['\"]?\\s*%\\}(.*?)\\{%\\s*endblock\\s+['\"]?\\1['\"]?\\s*%\\}";
        Pattern regex = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher matcher = regex.matcher(template);

        while (matcher.find()) {
            String blockName = matcher.group(1);
            String content = matcher.group(2).trim();
            blocks.put(blockName, content);
        }

        return blocks;
    }
    public static String replaceBlocks(String template, Map<String, String> blockValues) {
        String result = template;

        for (Map.Entry<String, String> entry : blockValues.entrySet()) {
            String blockName = entry.getKey();
            String newContent = entry.getValue();

            String pattern = "\\{%\\s*block\\s+['\"]?" + blockName + "['\"]?\\s*%\\}.*?\\{%\\s*endblock\\s+['\"]?" + blockName + "['\"]?\\s*%\\}";

            result = result.replaceAll("(?s)" + pattern, newContent);
        }
        return result;
    }
    String ReadInheritance(String childContent) throws IOException {
        // Chercher {% extends 'filename' %}
        Pattern extendsPattern = Pattern.compile("\\{%\\s*extends\\s+['\"]([^'\"]+)['\"]\\s*%\\}");
        Matcher matcher = extendsPattern.matcher(childContent);

        if (matcher.find()) {
            String parentFile = TEMPLATE_PATH + matcher.group(1);

            // lire le template parent
            String parentContent = Read(Paths.get(parentFile));

            // supp la ligne {% extends %} du contenu enfant
            String childWithoutExtends = matcher.replaceFirst("");

            // extract les blocs de l'enfant
            Map<String, String> childBlocks = extractAllBlocks(childWithoutExtends);

            // replace les blocs dans le parent
            String result = replaceBlocks(parentContent, childBlocks);

            return result;
        }

        return childContent;
    }
    String Read(Path path) throws IOException {
        return Files.readString(path);
    }

    public String Render(String path, Map<String,Object> toInject) throws IOException {
        path=TEMPLATE_PATH+path;
        String content = Read(Paths.get(path));
        for (Map.Entry<String,Object> k : toInject.entrySet())
        {
            content = content.replace((CharSequence) ("{{"+k.getKey()+"}}"), (CharSequence) k.getValue());
        }
        content = ReadInheritance(content);
        blocks = extractAllBlocks(content);
        content = replaceBlocks(content,blocks);
        return content;
    }
}
