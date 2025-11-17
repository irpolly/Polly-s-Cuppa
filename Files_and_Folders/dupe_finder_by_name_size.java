// Dupe Find By Name & Size
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class DupFindByNameAndSize {
    public static void main(String[] args) throws Exception {
        Path start = args.length > 0 ? Path.of(args[0]) : Path.of(".");

        record FileInfo(String name, long size, Path path) {}
        
        Map<String, Map<Long, List<Path>>> groups = new HashMap<>();

        Files.walk(start)
             .filter(Files::isRegularFile)
             .forEach(p -> {
                 try {
                     String name = p.getFileName().toString();
                     long size = Files.size(p);
                     groups.computeIfAbsent(name, k -> new HashMap<>())
                           .computeIfAbsent(size, k -> new ArrayList<>())
                           .add(p);
                 } catch (Exception ignored) {}
             });

        boolean found = false;
        for (var byName : groups.entrySet()) {
            for (var bySize : byName.getValue().entrySet()) {
                List<Path> duplicates = bySize.getValue();
                if (duplicates.size() > 1) {
                    found = true;
                    System.out.println("Duplicate '" + byName.getKey() + "' (" + bySize.getKey() + " bytes):");
                    duplicates.forEach(p -> System.out.println("   " + p));
                    System.out.println();
                }
            }
        }
        if (!found) System.out.println("No duplicates found by name + size.");
    }
}