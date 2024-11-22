package backend.academy.log_analyzer.reader;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public interface LogReader {
    Stream<String> readLogs() throws IOException, InterruptedException;

    static LogReader getReader(String path) {
        if (isUrl(path)) {
            return new UrlLogReader(path);
        }
        return new LocalPathLogReader(parseLocalPath(path));
    }

    private static String parseLocalPath(String pathParam) {
        String path = pathParam;
        final String GLOB_ROOT = "**/";
        if (!path.startsWith(GLOB_ROOT)) {
            path = GLOB_ROOT + path;
        }
        return path;
    }

    private static boolean isUrl(String path) {
        String urlRegex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        Pattern urlPattern = Pattern.compile(urlRegex);
        return urlPattern.matcher(path).matches();
    }
}
