import com.sun.net.httpserver.*;
import java.nio.file.*;
import java.io.*;
public class QuickServe {
    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(port), 0);
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";
            Path file = Path.of("." + path);
            if (Files.isDirectory(file)) {
                String html = "<html><body><ul>" + Files.list(file).map(p -> "<li><a href='" + p.getFileName() + "'>" + p.getFileName() + "</a></li>").reduce("", (a,b)->a+b) + "</ul></body></html>";
                byte[] bytes = html.getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
            } else if (Files.exists(file)) {
                exchange.sendResponseHeaders(200, Files.size(file));
                Files.copy(file, exchange.getResponseBody());
            } else {
                exchange.sendResponseHeaders(404, 0);
            }
            exchange.close();
        });
        server.start();
        System.out.println("Serving http://localhost:" + port + "/");
    }
}