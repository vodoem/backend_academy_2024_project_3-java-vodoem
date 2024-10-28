package backend.academy.log_analyzer.data;

import java.time.ZonedDateTime;

public record LogRecord(
    String ipAddress,
    String user,
    ZonedDateTime dateTime,
    String method,
    String resource,
    int status,
    long responseSize,
    String userAgent
) {
}
