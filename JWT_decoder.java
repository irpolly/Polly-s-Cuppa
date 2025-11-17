import java.util.Base64;
public class JwtDecode {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) return;
        String[] parts = args[0].split("\\.");
        System.out.println("Header:  " + new String(Base64.getUrlDecoder().decode(parts[0])));
        System.out.println("Payload: " + new String(Base64.getUrlDecoder().decode(parts[1])));
    }
}