package backend.academy.log_analyze_tests;

import backend.academy.log_analyzer.data.LogRecord;
import backend.academy.log_analyzer.parser.LogParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LogParserTest {
    @Test
    void testParseValidLogLine() {
        LogParser logParser = new LogParser();
        String logLine = "127.0.0.1 - user [10/Oct/2023:13:55:36 +0000] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0\"";
        LogRecord record = logParser.parse(logLine);
        assertNotNull(record, "Log record should not be null");
        assertEquals("127.0.0.1", record.ipAddress());
        assertEquals("user", record.user());
        assertEquals("GET", record.method());
        assertEquals("/index.html HTTP/1.1", record.resource());
        assertEquals(200, record.status());
        assertEquals(1234, record.responseSize());
        assertEquals("Mozilla/5.0", record.userAgent());
    }

    @Test
    void testParseInvalidLogLine() {
        LogParser logParser = new LogParser();
        String invalidLogLine = "Invalid log format";
        assertThrows(IllegalArgumentException.class, () -> logParser.parse(invalidLogLine));
    }
}
