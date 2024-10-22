package backend.academy.log_analyzer.analyzer;

import backend.academy.log_analyzer.data.LogRecord;
import backend.academy.log_analyzer.data.LogReport;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogAnalyzer {
    public LogReport analyze(Stream<LogRecord> logRecords) {
        // Создаём объект для хранения итогового отчета
        return new LogReport(
            countTotalRequests(logRecords),
            findMostRequestedResources(logRecords),
            findMostCommonResponseCodes(logRecords),
            calculateAverageResponseSize(logRecords),
            calculate95PercentileResponseSize(logRecords),
            countUniqueIpAddresses(logRecords),
            countFailedRequests(logRecords)
        );
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
