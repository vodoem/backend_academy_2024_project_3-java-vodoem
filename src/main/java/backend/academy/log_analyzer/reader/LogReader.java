package backend.academy.log_analyzer.reader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LogReader {

    private static final int OK = 200;

    private static final String URL_REGEX =
        "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    public Stream<String> readLogs(String path) throws IOException, InterruptedException {
        if (isUrl(path)) {
            return readFromUrl(path);
        }
        return readFromLocalPath(path);
    }

    // Метод для чтения логов с локального пути
    private Stream<String> readFromLocalPath(String filePath) throws IOException {
        return Files.lines(Path.of(filePath));
    }

    // Метод для чтения логов из URL
    private Stream<String> readFromUrl(String urlPath) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        // Создаем запрос к заданному URI
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(urlPath))
            .GET()
            .build();

        // Отправляем запрос и получаем ответ как строку
        HttpResponse<String> response =
            httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        // Проверка на успешный ответ
        if (response.statusCode() != OK) {
            throw new IOException(
                "Не удалось получить данные с URL: " + urlPath + ". Статус код: " + response.statusCode());
        }

        return response.body().lines();
    }

    private boolean isUrl(String path) {
        return URL_PATTERN.matcher(path).matches();
    }

}
