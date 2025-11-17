// Lazy nodemon (Watch a directory and run a command on change)
import java.nio.file.*;
public class WatchRun {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) { System.out.println("Usage: WatchRun <dir> <command...>"); return; }
        Path dir = Path.of(args[0]);
        WatchService ws = dir.getFileSystem().newWatchService();
        dir.register(ws, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);
        new ProcessBuilder(java.util.List.of(java.util.Arrays.copyOfRange(args, 1, args.length))).inheritIO().start().waitFor();
        while (true) {
            WatchKey key = ws.take();
            if (!key.pollEvents().isEmpty()) {
                System.out.println("\n--- Change detected, restarting ---");
                new ProcessBuilder(java.util.List.of(java.util.Arrays.copyOfRange(args, 1, args.length))).inheritIO().start();
            }
            key.reset();
        }
    }
}