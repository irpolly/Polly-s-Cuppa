import java.nio.file.*; import java.io.*; import com.google.gson.*;
public class PrettyJson {
    public static void main(String[] args) throws Exception {
        String input = args.length > 0 ? Files.readString(Path.of(args[0])) : new String(System.in.readAllBytes());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(input);
        System.out.println(gson.toJson(je));
    }
}