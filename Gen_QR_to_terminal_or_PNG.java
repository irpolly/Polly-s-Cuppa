import com.google.zxing.*; import com.google.zxing.client.j2se.*;
import java.nio.file.*;
public class Qr {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) return;
        String text = String.join(" ", args);
        if (args[0].endsWith(".png")) {
            MatrixToImageWriter.writeToPath(BitMatrixEx.encode(text, 300), "PNG", Path.of(args[0]));
        } else {
            System.out.println(BitMatrixEx.encode(text, 2)); // ASCII art QR
        }
    }
}