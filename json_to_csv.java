import java.nio.file.*;
import java.util.*;
import com.google.gson.*;
import java.io.*;

public class JsonToCsv {
    public static void main(String[] args) throws Exception {
        String json = args.length > 0 ? Files.readString(Path.of(args[0])) : new String(System.in.readAllBytes());
        JsonElement el = JsonParser.parseString(json);

        StringBuilder csv = new StringBuilder();
        List<String> headers = new LinkedHashSet<>().stream().toList(); // will be filled below

        if (el.isJsonArray()) {
            JsonArray array = el.getAsJsonArray();
            if (array.size() == 0) { System.out.println("Empty array"); return; }

            // Collect all keys in order of appearance
            Set<String> keySet = new LinkedHashSet<>();
            for (JsonElement e : array) {
                if (e.isJsonObject()) e.getAsJsonObject().entrySet().forEach(entry -> keySet.add(entry.getKey()));
            }
            headers = new ArrayList<>(keySet);

            // Header line
            csv.append(String.join(",", escape(headers))).append("\n");

            // Rows
            for (JsonElement e : array) {
                if (!e.isJsonObject()) continue;
                JsonObject obj = e.getAsJsonObject();
                List<String> row = new ArrayList<>();
                for (String h : headers) {
                    JsonElement val = obj.get(h);
                    row.add(val == null || val.isJsonNull() ? "" : escape(val.getAsString()));
                }
                csv.append(String.join(",", row)).append("\n");
            }
        } else if (el.isJsonObject()) {
            // Single object → one row
            JsonObject obj = el.getAsJsonObject();
            headers = new ArrayList<>(obj.keySet());
            csv.append(String.join(",", escape(headers))).append("\n");
            csv.append(String.join(",", headers.stream().map(k -> escape(obj.get(k).getAsString())).toList())).append("\n");
        } else {
            System.out.println("Not a JSON object or array");
            return;
        }

        System.out.print(csv);
    }

    private static List<String> escape(List<String> list) {
        return list.stream().map(JsonToCsv::escape).toList();
    }
    private static String escape(String s) {
        if (s == null) return "";
        boolean needsQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        s = s.replace("\"", "\"\"");
        return needsQuotes ? "\"" + s + "\"" : s;
    }
}