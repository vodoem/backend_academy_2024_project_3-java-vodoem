package backend.academy.log_analyzer.parser;

import backend.academy.log_analyzer.data.LogRecord;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    private static final Pattern LOG_PATTERN = Pattern.compile(
        "^(\\S+) - (\\S+) \\[(.+?)\\] \"(\\S+) (.+?)\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\"$"
    );
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);

    public LogRecord parse(String logLine) throws IllegalArgumentException {
        Matcher matcher = LOG_PATTERN.matcher(logLine);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Неверный формат лог строки");
        }
        String ipAddress = matcher.group(1);
        String user = matcher.group(2);
        ZonedDateTime dateTime = ZonedDateTime.parse(matcher.group(3), DATE_FORMATTER);

        String method = matcher.group(4);  // Получаем HTTP-метод (например, GET)
        String resource = matcher.group(5); // Получаем URL и версию HTTP (например, /downloads/product_1 HTTP/1.1)

        int status = Integer.parseInt(matcher.group(6));
        long responseSize = Long.parseLong(matcher.group(7));
        String referer = matcher.group(8);
        String userAgent = matcher.group(9);
        return new LogRecord(ipAddress, user, dateTime, method, resource, status, responseSize, referer, userAgent);
    }
}
