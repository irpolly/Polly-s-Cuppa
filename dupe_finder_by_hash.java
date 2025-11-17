import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
public class DupFind {
    public static void main(String[] args) throws Exception {
        Path start = args.length > 0 ? Path.of(args[0]) : Path.of(".");
        Map<String, Path> map = new HashMap<>();
        Files.walk(start).filter(Files::isRegularFile).forEach(p -> {
            try {
                String hash = base16(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(p)));
                Path existing = map.putIfAbsent(hash, p);
                if (existing != null) System.out.println("Duplicate: " + existing + " == " + p);
            } catch (Exception e) {}
        });
    }
    static String base16(byte[] b) { return javax.xml.bind.DatatypeConverter.printHexBinary(b).toLowerCase(); }
}