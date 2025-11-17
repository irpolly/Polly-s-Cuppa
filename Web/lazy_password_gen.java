import java.security.SecureRandom;
import java.util.stream.*;
public class PassGen {
    public static void main(String[] args) {
        int len = args.length > 0 ? Integer.parseInt(args[0]) : 20;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom r = new SecureRandom();
        String pw = r.ints(len, 0, chars.length()).mapToObj(i -> "" + chars.charAt(i)).collect(Collectors.joining());
        System.out.println(pw);
    }
}