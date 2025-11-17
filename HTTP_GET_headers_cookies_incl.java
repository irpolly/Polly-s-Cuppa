
import java.net.http.*;
import java.net.URI;
public class Curl {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) return;
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        HttpRequest req = HttpRequest.newBuilder(URI.create(args[0])).build();
        System.out.println(client.send(req, HttpResponse.BodyHandlers.ofString()).body());
    }
}