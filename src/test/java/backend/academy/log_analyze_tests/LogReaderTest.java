package backend.academy.log_analyze_tests;

import backend.academy.log_analyzer.reader.LogReader;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogReaderTest {
    @Test
    void testReadFromLocalPath() throws IOException, InterruptedException {
        // Arrange
        LogReader logReader = LogReader.getReader("log_files/test_logs.txt");

        // Act
        Stream<String> logLines = logReader.readLogs();

        // Assert
        assertNotNull(logLines, "Log lines should not be null");
        assertEquals(5, logLines.count(), "Should read 3 lines from log file");
    }

    @Test
    void testReadFromLocalPathWithTemplate() throws IOException, InterruptedException {
        // Arrange
        LogReader logReader = LogReader.getReader("log_files/test*");

        // Act
        Stream<String> logLines = logReader.readLogs();

        // Assert
        assertNotNull(logLines, "Log lines should not be null");
        assertEquals(5, logLines.count(), "Should read 3 lines from log file");
    }

    @Test
    void testReadFromUrl() throws IOException, InterruptedException {
        // Arrange
        LogReader logReader = LogReader.getReader("https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs");

        // Act
        Stream<String> logLines = logReader.readLogs();

        // Assert
        assertNotNull(logLines, "Log lines should not be null");
        assertTrue(logLines.findAny().isPresent(), "Should read lines from URL");
    }
}
