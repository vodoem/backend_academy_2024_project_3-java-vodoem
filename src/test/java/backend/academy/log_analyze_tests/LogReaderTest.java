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
    void testReadFromLocalPath() throws IOException {
        LogReader logReader = new LogReader();
        Stream<String> logLines = logReader.readLogs("log_files/test_logs.txt");
        assertNotNull(logLines, "Log lines should not be null");
        assertEquals(5, logLines.count(), "Should read 3 lines from log file");
    }

    @Test
    void testReadFromUrl() throws IOException {
        LogReader logReader = new LogReader();
        Stream<String> logLines = logReader.readLogs("https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs");
        assertNotNull(logLines, "Log lines should not be null");
        assertTrue(logLines.findAny().isPresent(), "Should read lines from URL");
    }
}
