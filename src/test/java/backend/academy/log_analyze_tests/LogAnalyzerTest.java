package backend.academy.log_analyze_tests;

import backend.academy.log_analyzer.analyzer.LogAnalyzer;
import backend.academy.log_analyzer.data.LogRecord;
import backend.academy.log_analyzer.data.LogReport;
import backend.academy.log_analyzer.parser.LogParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Locale;

public class LogAnalyzerTest {
    private LogAnalyzer logAnalyzer;
    private LogParser logParser;

    @BeforeEach
    void setUp() {
        logParser = new LogParser();
        logAnalyzer = new LogAnalyzer();
    }

    @Test
    void testAnalyzeLogsWithFilter() {
        //Arrange
        Stream<LogRecord> logRecords = Stream.of(
            new LogRecord("127.0.0.1", "user1", ZonedDateTime.now(), "GET", "/index.html", 200, 1000, "Mozilla/5.0"),
            new LogRecord("127.0.0.2", "user2", ZonedDateTime.now(), "POST", "/upload", 404, 500, "Chrome")
        );

        //Act
        LogReport logReport = logAnalyzer.analyze(logRecords, "method", "GET", null, null);

        //Assert
        assertNotNull(logReport, "Log report should not be null");
        assertEquals(1, logReport.totalRequests(), "Total requests should match filtered requests");
    }

    @Test
    void testAnalyzeLogsWithoutFilter() {
        // Arrange
        Stream<LogRecord> logRecords = Stream.of(
            new LogRecord("127.0.0.1", "user1", ZonedDateTime.now(), "GET", "/index.html", 200, 1000, "Mozilla/5.0"),
            new LogRecord("127.0.0.2", "user2", ZonedDateTime.now(), "POST", "/upload", 404, 500, "Chrome")
        );

        // Act
        LogReport logReport = logAnalyzer.analyze(logRecords, null, null, null, null);

        // Assert
        assertNotNull(logReport, "Log report should not be null");
        assertEquals(2, logReport.totalRequests(), "Total requests should match all logs");
    }

    @Test
    void testAverageResponseSize() {
        // Arrange
        Stream<String> logStream = createLogStream();
        Stream<LogRecord> logRecords = logStream.map(logLine -> {
            try {
                return logParser.parse(logLine);
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка при парсинге строки лога: " + e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull);

        // Act
        LogReport logReport = logAnalyzer.analyze(logRecords, null, null, null, null);
        double averageSize = logReport.averageResponseSize();

        // Assert
        assertThat(averageSize).isEqualTo(793.6); // (512 + 1024 + 256 + 2048 + 128) / 5
    }

    @Test
    void test95thPercentileResponseSize() {
        // Arrange
        Stream<String> logStream = createLogStream();
        Stream<LogRecord> logRecords = logStream.map(logLine -> {
            try {
                return logParser.parse(logLine);
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка при парсинге строки лога: " + e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull);

        // Act
        LogReport logReport = logAnalyzer.analyze(logRecords, null, null, null, null);
        double percentile95 = logReport.percentile95ResponseSize();

        // Assert
        assertThat(percentile95).isEqualTo(2048);  // 95-й перцентиль для значений [128, 256, 512, 1024, 2048]
    }

    @Test
    void testMostRequestedResources() {
        // Arrange
        Stream<String> logStream = createLogStream();
        Stream<LogRecord> logRecords = logStream.map(logLine -> {
            try {
                return logParser.parse(logLine);
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка при парсинге строки лога: " + e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull);

        // Act
        LogReport logReport = logAnalyzer.analyze(logRecords, null, null, null, null);
        Map<String, Long> mostRequestedResources = logReport.mostRequestedResources();

        // Assert
        assertThat(mostRequestedResources).isEqualTo(Map.of(
            "/downloads/product_1 HTTP/1.1", 1L,
            "/upload/file HTTP/1.1", 1L,
            "/downloads/product_2 HTTP/1.1", 1L,
            "/update/info HTTP/1.1", 1L,
            "/delete/item HTTP/1.1", 1L
        ));
    }

    @Test
    void testMostCommonResponseCodes() {
        // Arrange
        Stream<String> logStream = createLogStream();
        Stream<LogRecord> logRecords = logStream.map(logLine -> {
            try {
                return logParser.parse(logLine);
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка при парсинге строки лога: " + e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull);

        // Act
        LogReport logReport = logAnalyzer.analyze(logRecords, null, null, null, null);
        Map<Integer, Long> mostCommonResponseCodes = logReport.mostCommonResponseCodes();

        // Assert
        assertThat(mostCommonResponseCodes).isEqualTo(Map.of(
            200, 1L,
            404, 1L,
            500, 1L,
            201, 1L,
            403, 1L
        ));
    }

    @Test
    void testFilterByDate() {
        // Arrange
        Stream<String> logStream = createLogStream();
        Stream<LogRecord> logRecords = logStream.map(logLine -> {
            try {
                return logParser.parse(logLine);
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка при парсинге строки лога: " + e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
        // Act
        LogReport logReport = logAnalyzer.analyze(logRecords, null, null,
            ZonedDateTime.parse("25/Oct/2024:14:50:00 +0000", dateTimeFormatter),
            ZonedDateTime.parse("25/Oct/2024:14:56:00 +0000", dateTimeFormatter)
        );

        Long filteredLogsSize = logReport.totalRequests();

        // Assert
        assertThat(filteredLogsSize).isEqualTo(3L);
    }

    private Stream<String> createLogStream() {
        List<String> logLines = List.of(
            "192.168.0.1 - user1 [25/Oct/2024:14:48:00 +0000] \"GET /downloads/product_1 HTTP/1.1\" 200 512 \"http://example.com\" \"Mozilla/5.0\"",
            "192.168.0.2 - user2 [25/Oct/2024:14:50:00 +0000] \"POST /upload/file HTTP/1.1\" 404 1024 \"http://example.com/upload\" \"Mozilla/5.0\"",
            "192.168.0.3 - user3 [25/Oct/2024:14:52:00 +0000] \"GET /downloads/product_2 HTTP/1.1\" 500 256 \"http://example.com\" \"Mozilla/5.0\"",
            "192.168.0.4 - user4 [25/Oct/2024:14:55:00 +0000] \"PUT /update/info HTTP/1.1\" 201 2048 \"http://example.com/update\" \"Mozilla/5.0\"",
            "192.168.0.5 - user5 [25/Oct/2024:15:00:00 +0000] \"DELETE /delete/item HTTP/1.1\" 403 128 \"http://example.com/delete\" \"Mozilla/5.0\""
        );

        return logLines.stream();
    }
}
