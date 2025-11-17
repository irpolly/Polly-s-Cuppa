import java.util.Base64;
public class B64 {
    public static void main(String[] args) {
        if (args.length == 0) { System.err.println("Usage: encode|decode <text>"); return; }
        String mode = args[0];
        String data = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        if ("encode".equals(mode)) System.out.println(Base64.getEncoder().encodeToString(data.getBytes()));
        else if ("decode".equals(mode)) System.out.println(new String(Base64.getDecoder().decode(data)));
    }
}