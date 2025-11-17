import java.nio.file.*;
import com.google.gson.*;
public class Csv2Json {
    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Path.of(args.length > 0 ? args[0] : "input.csv"));
        var headers = lines.get(0).split(",");
        var array = new JsonArray();
        for (int i = 1; i < lines.size(); i++) {
            var values = lines.get(i).split(",");
            var obj = new JsonObject();
            for (int j = 0; j < headers.length; j++) obj.addProperty(headers[j].trim(), j < values.length ? values[j].trim() : "");
            array.add(obj);
        }
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(array));
    }
}