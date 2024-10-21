package backend.academy.log_analyzer.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class LogReader {

    public Stream<String> readLogs(String path) throws IOException {
        if (isUrl(path)){
            return readFromUrl(path);
        }
        return readFromLocalPath(path);
    }

    // Метод для чтения логов с локального пути
    private Stream<String> readFromLocalPath(String filePath) throws IOException {
        return Files.lines(Path.of(filePath));
    }

    // Метод для чтения логов из URL
    private Stream<String> readFromUrl(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        return reader.lines();
    }

    private boolean isUrl(String path) {
        try {
            new URL(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
