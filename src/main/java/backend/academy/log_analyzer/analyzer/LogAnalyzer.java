package backend.academy.log_analyzer.analyzer;

import backend.academy.log_analyzer.data.LogRecord;
import backend.academy.log_analyzer.data.LogReport;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogAnalyzer {
    public LogReport analyze(Stream<LogRecord> logRecords, String filterField, String filterValue, ZonedDateTime fromDate, ZonedDateTime toDate) {

        List<LogRecord> filteredRecords = logRecords
            .filter(record -> isWithinDateRange(record, fromDate, toDate))
            .filter(record -> matchesField(record, filterField, filterValue))
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
    private boolean isWithinDateRange(LogRecord record, ZonedDateTime fromDate, ZonedDateTime toDate) {
        ZonedDateTime recordDate = record.dateTime();
        return (fromDate == null || !recordDate.isBefore(fromDate)) &&
            (toDate == null || !recordDate.isAfter(toDate));
    }

    // Метод для фильтрации по полю
    private boolean matchesField(LogRecord record, String filterField, String filterValue) {
        if (filterField == null){
            return true;
        }
        switch (filterField.toLowerCase()) {
            case "method":
                return record.method().startsWith(filterValue); // Фильтрация по методу HTTP-запроса
            case "agent":
                return record.userAgent().contains(filterValue); // Фильтрация по user-agent
            case "ip":
                return record.ipAddress().equals(filterValue); // Фильтрация по IP-адресу
            case "status":
                try {
                    int status = Integer.parseInt(filterValue);
                    return record.status() == status; // Фильтрация по HTTP-статусу
                } catch (NumberFormatException e) {
                    return false; // Если статус не числовой, фильтрация не пройдена
                }
            case "response-size":
                try {
                    long size = Long.parseLong(filterValue);
                    return record.responseSize() == size; // Фильтрация по размеру ответа
                } catch (NumberFormatException e) {
                    return false; // Если размер не числовой, фильтрация не пройдена
                }
            default:
                return true; // Если не указано или не поддерживается поле, не фильтруем
        }
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
        int index95Percentile = (int) Math.ceil(sortedSizes.size() * 0.95) - 1;
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
            .filter(logRecord -> logRecord.status() < 200 || logRecord.status() >= 300)
            .count();
    }
}
