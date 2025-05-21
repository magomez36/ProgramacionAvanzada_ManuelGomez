package URLsCounter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class UrlProcessor {

    public static int countInternalUrls(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Java URL Processor");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String html = reader.lines().collect(Collectors.joining());

                Pattern pattern = Pattern.compile("href=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(html);

                int count = 0;
                while (matcher.find()) {
                    String link = matcher.group(1);
                    try {
                        URL foundUrl = new URL(url, link);
                        if (foundUrl.getHost().equalsIgnoreCase(host)) {
                            count++;
                        }
                    } catch (MalformedURLException e) {
                        // ignorar enlaces inválidos
                    }
                }
                return count;
            }

        } catch (Exception e) {
            System.err.println("Error procesando: " + urlString + " - " + e.getMessage());
            return 0;
        }
    }
}

