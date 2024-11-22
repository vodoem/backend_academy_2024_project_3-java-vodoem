package backend.academy.log_analyzer.reader;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class LocalPathLogReader implements LogReader {

    private final String path;

    public LocalPathLogReader(String path) {
        this.path = path;
    }

    @Override
    public Stream<String> readLogs() throws IOException {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + this.path);
        Path startPath = Paths.get(".");

        try (Stream<Path> paths = Files.walk(startPath)) {
            List<Path> foundFiles = paths.toList();
            return foundFiles.stream()
                .filter(Files::isRegularFile)
                .filter(matcher::matches)
                .flatMap(path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        return Stream.empty();
                    }
                });
        }
    }
}
