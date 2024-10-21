package backend.academy.log_analyzer.data;

import java.time.LocalDateTime;

public record LogRecord(
    String ipAddress,
    String user,
    LocalDateTime dateTime,
    String request,
    int status,
    long responseSize,
    String referer,
    String userAgent
) {
}
