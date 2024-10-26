package backend.academy.log_analyzer.analyzer;

import backend.academy.log_analyzer.data.LogRecord;
import backend.academy.log_analyzer.data.LogReport;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogAnalyzer {
    public LogReport analyze(Stream<LogRecord> logRecords, String filterField, String filterValue, LocalDateTime fromDate, LocalDateTime toDate) {
        // Фильтруем записи по дате
        Stream<LogRecord> filteredByDate = logRecords.filter(record -> isWithinDateRange(record, fromDate, toDate));

        // Фильтруем записи по указанному полю
        Stream<LogRecord> filteredByField = filteredByDate.filter(record -> matchesField(record, filterField, filterValue));


        // Создаём объект для хранения итогового отчета
        return new LogReport(
            countTotalRequests(filteredByField),
            findMostRequestedResources(filteredByField),
            findMostCommonResponseCodes(filteredByField),
            calculateAverageResponseSize(filteredByField),
            calculate95PercentileResponseSize(filteredByField),
            countUniqueIpAddresses(filteredByField),
            countFailedRequests(filteredByField)
        );
    }

    // Метод для фильтрации по дате
    private boolean isWithinDateRange(LogRecord record, LocalDateTime fromDate, LocalDateTime toDate) {
        LocalDateTime recordDate = record.dateTime();
        return (fromDate == null || !recordDate.isBefore(fromDate)) &&
            (toDate == null || !recordDate.isAfter(toDate));
    }

    // Метод для фильтрации по полю
    private boolean matchesField(LogRecord record, String filterField, String filterValue) {
        switch (filterField.toLowerCase()) {
            case "method":
                return record.request().startsWith(filterValue); // Фильтрация по методу HTTP-запроса
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

    private long countTotalRequests(Stream<LogRecord> logRecords) {
        return logRecords.count();
    }

    private Map<String, Long> findMostRequestedResources(Stream<LogRecord> logRecords) {
        return logRecords.collect(Collectors.groupingBy(LogRecord::request, Collectors.counting()));
    }

    private Map<Integer, Long> findMostCommonResponseCodes(Stream<LogRecord> logRecords) {
        return logRecords.collect(Collectors.groupingBy(LogRecord::status, Collectors.counting()));
    }

    private double calculateAverageResponseSize(Stream<LogRecord> logRecords) {
        return logRecords.mapToLong(LogRecord::responseSize).average().orElse(0);
    }

    private long calculate95PercentileResponseSize(Stream<LogRecord> logRecords) {
        List<Long> sortedSizes = logRecords.map(LogRecord::responseSize).sorted().toList();
        int index95Percentile = (int) Math.ceil(sortedSizes.size() * 0.95) - 1;
        if (sortedSizes.isEmpty()) {
            return 0;
        }
        return sortedSizes.get(index95Percentile);
    }

    private long countUniqueIpAddresses(Stream<LogRecord> logRecords) {
        return logRecords
            .map(LogRecord::ipAddress)
            .distinct()
            .count();
    }

    private long countFailedRequests(Stream<LogRecord> logRecords) {
        return logRecords
            .filter(logRecord -> logRecord.status() < 200 || logRecord.status() >= 300)
            .count();
    }
}
