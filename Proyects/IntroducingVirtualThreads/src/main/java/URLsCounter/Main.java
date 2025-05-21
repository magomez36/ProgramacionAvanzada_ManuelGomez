package URLsCounter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String urlsFile = "urls_parcial1-1.txt";
        String resultsFile = "resultados.csv";

        List<String> urls = FileUtils.readUrlsFromFile(urlsFile);
        Map<String, Integer> results = new ConcurrentHashMap<>();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>();

            for (String url : urls) {
                futures.add(executor.submit(() -> {
                    int count = UrlProcessor.countInternalUrls(url);
                    results.put(url, count);
                }));
            }

            for (Future<?> future : futures) {
                try {
                    future.get(); // Esperar que termine cada tarea
                } catch (ExecutionException e) {
                    System.err.println("Error procesando una URL: " + e.getMessage());
                }
            }
        }

        FileUtils.writeResultsToCsv(resultsFile, results);
        System.out.println("Proceso completado. Resultados guardados en: " + resultsFile);
    }
}
