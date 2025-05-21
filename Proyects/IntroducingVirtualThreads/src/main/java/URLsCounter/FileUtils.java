package URLsCounter;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileUtils {

    public static List<String> readUrlsFromFile(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath))
                .stream()
                .filter(line -> !line.isBlank())
                .toList();
    }

    public static void writeResultsToCsv(String filePath, Map<String, Integer> results) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("URL,Cantidad de URLs internas");
            writer.newLine();
            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                writer.write("\"" + entry.getKey() + "\"," + entry.getValue());
                writer.newLine();
            }
        }
    }
}
