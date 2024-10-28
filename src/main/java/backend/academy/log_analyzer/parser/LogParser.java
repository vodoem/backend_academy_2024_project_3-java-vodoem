package backend.academy.log_analyzer.parser;

import backend.academy.log_analyzer.data.LogRecord;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    private static final int IP_ADDRESS_GROUP_INDEX = 1;
    private static final int USER_GROUP_INDEX = 2;
    private static final int DATE_TIME_GROUP_INDEX = 3;
    private static final int METHOD_GROUP_INDEX = 4; // HTTP-метод
    private static final int RESOURCE_GROUP_INDEX = 5; // URL и версия HTTP
    private static final int STATUS_GROUP_INDEX = 6; // HTTP статус
    private static final int RESPONSE_SIZE_GROUP_INDEX = 7; // Размер ответа
    private static final int USER_AGENT_GROUP_INDEX = 9; // User-Agent
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "^(\\S+) - (\\S+) \\[(.+?)] \"(\\S+) (.+?)\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\"$"
    );
    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);

    public LogRecord parse(String logLine) throws IllegalArgumentException {
        Matcher matcher = LOG_PATTERN.matcher(logLine);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Неверный формат лог строки");
        }
        String ipAddress = matcher.group(IP_ADDRESS_GROUP_INDEX);
        String user = matcher.group(USER_GROUP_INDEX);
        ZonedDateTime dateTime = ZonedDateTime.parse(matcher.group(DATE_TIME_GROUP_INDEX), DATE_FORMATTER);

        String method = matcher.group(METHOD_GROUP_INDEX);  // Получаем HTTP-метод (например, GET)
        String resource = matcher.group(RESOURCE_GROUP_INDEX); // Получаем URL и версию HTTP

        int status = Integer.parseInt(matcher.group(STATUS_GROUP_INDEX));
        long responseSize = Long.parseLong(matcher.group(RESPONSE_SIZE_GROUP_INDEX));
        String userAgent = matcher.group(USER_AGENT_GROUP_INDEX);
        return new LogRecord(ipAddress, user, dateTime, method, resource, status, responseSize, userAgent);
    }
}
