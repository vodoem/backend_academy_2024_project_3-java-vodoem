package backend.academy.log_analyzer.parser;

import backend.academy.log_analyzer.data.LogRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    private static final Pattern LOG_PATTERN = Pattern.compile(
        "^(\\S+) - (\\S+) \\[(.+?)\\] \"(.+?)\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\"$"
    );
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");

    public LogRecord parse(String logLine) throws IllegalArgumentException {
        Matcher matcher = LOG_PATTERN.matcher(logLine);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Неверный формат лог строки");
        }
        String ipAddress = matcher.group(1);
        String user = matcher.group(2);
        LocalDateTime dateTime = LocalDateTime.parse(matcher.group(3), DATE_FORMATTER);
        String request = matcher.group(4);
        int status = Integer.parseInt(matcher.group(5));
        long responseSize = Long.parseLong(matcher.group(6));
        String referer = matcher.group(7);
        String userAgent = matcher.group(8);
        return new LogRecord(ipAddress, user, dateTime, request, status, responseSize, referer, userAgent);
    }
}
