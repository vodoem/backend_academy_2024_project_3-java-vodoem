package backend.academy.log_analyzer.reader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class UrlLogReader implements LogReader {

    private static final int OK = 200;

    private final String path;

    public UrlLogReader(String path) {
        this.path = path;
    }

    @Override
    public Stream<String> readLogs() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(this.path))
            .GET()
            .build();

        HttpResponse<String> response =
            httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() != OK) {
            throw new IOException(
                "Не удалось получить данные с URL: " + this.path + ". Статус код: " + response.statusCode());
        }

        return response.body().lines();
    }
}
