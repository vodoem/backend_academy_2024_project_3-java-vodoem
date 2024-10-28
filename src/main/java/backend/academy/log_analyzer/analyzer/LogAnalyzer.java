package backend.academy.log_analyzer.analyzer;

import backend.academy.log_analyzer.data.LogRecord;
import backend.academy.log_analyzer.data.LogReport;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogAnalyzer {
    private static final double RESPONSE_SIZE_PERCENTILE = 0.95;
    private static final int HTTP_STATUS_OK_MIN = 200;
    private static final int HTTP_STATUS_OK_MAX = 300;

    public LogReport analyze(
        Stream<LogRecord> logRecords,
        String filterField,
        String filterValue,
        ZonedDateTime fromDate,
        ZonedDateTime toDate
    ) {

        List<LogRecord> filteredRecords = logRecords
            .filter(r -> isWithinDateRange(r, fromDate, toDate))
            .filter(r -> matchesField(r, filterField, filterValue))
            .collect(Collectors.toList());

        // Создаём объект для хранения итогового отчета
        return new LogReport(
            countTotalRequests(filteredRecords),
            findMostRequestedResources(filteredRecords),
            findMostCommonResponseCodes(filteredRecords),
            calculateAverageResponseSize(filteredRecords),
            calculate95PercentileResponseSize(filteredRecords),
            countUniqueIpAddresses(filteredRecords),
            countFailedRequests(filteredRecords)
        );
    }

    // Метод для фильтрации по дате
    private boolean isWithinDateRange(LogRecord logRecord, ZonedDateTime fromDate, ZonedDateTime toDate) {
        ZonedDateTime recordDate = logRecord.dateTime();
        return (fromDate == null || !recordDate.isBefore(fromDate))
            && (toDate == null || !recordDate.isAfter(toDate));
    }

    // Метод для фильтрации по полю
    private boolean matchesField(LogRecord logRecord, String filterField, String filterValue) {
        if (filterField == null) {
            return true;
        }

        boolean matches = true; // По умолчанию, предполагаем, что запись подходит

        switch (filterField.toLowerCase()) {
            case "method":
                matches = logRecord.method().startsWith(filterValue); // Фильтрация по методу HTTP-запроса
                break;
            case "agent":
                matches = logRecord.userAgent().matches(filterValue.replace("*", ".*")); // Фильтрация по user-agent
                break;
            case "ip":
                matches = logRecord.ipAddress().equals(filterValue); // Фильтрация по IP-адресу
                break;
            case "status":
                try {
                    int status = Integer.parseInt(filterValue);
                    matches = logRecord.status() == status; // Фильтрация по HTTP-статусу
                } catch (NumberFormatException e) {
                    matches = false; // Если статус не числовой, фильтрация не пройдена
                }
                break;
            case "response-size":
                try {
                    long size = Long.parseLong(filterValue);
                    matches = logRecord.responseSize() == size; // Фильтрация по размеру ответа
                } catch (NumberFormatException e) {
                    matches = false; // Если размер не числовой, фильтрация не пройдена
                }
                break;
            default:
                // Если не указано или не поддерживается поле, не фильтруем
                break;
        }

        return matches; // Возвращаем результат один раз
    }

    private long countTotalRequests(List<LogRecord> logRecords) {
        return logRecords.size();
    }

    private Map<String, Long> findMostRequestedResources(List<LogRecord> logRecords) {
        return logRecords.stream().collect(Collectors.groupingBy(LogRecord::resource, Collectors.counting()));
    }

    private Map<Integer, Long> findMostCommonResponseCodes(List<LogRecord> logRecords) {
        return logRecords.stream().collect(Collectors.groupingBy(LogRecord::status, Collectors.counting()));
    }

    private double calculateAverageResponseSize(List<LogRecord> logRecords) {
        return logRecords.stream().mapToLong(LogRecord::responseSize).average().orElse(0);
    }

    private long calculate95PercentileResponseSize(List<LogRecord> logRecords) {
        List<Long> sortedSizes = logRecords.stream().map(LogRecord::responseSize).sorted().toList();
        int index95Percentile = (int) Math.ceil(sortedSizes.size() * RESPONSE_SIZE_PERCENTILE) - 1;
        if (sortedSizes.isEmpty()) {
            return 0;
        }
        return sortedSizes.get(index95Percentile);
    }

    private long countUniqueIpAddresses(List<LogRecord> logRecords) {
        return logRecords.stream()
            .map(LogRecord::ipAddress)
            .distinct()
            .count();
    }

    private long countFailedRequests(List<LogRecord> logRecords) {
        return logRecords.stream()
            .filter(logRecord -> logRecord.status() < HTTP_STATUS_OK_MIN || logRecord.status() >= HTTP_STATUS_OK_MAX)
            .count();
    }
}
