package backend.academy.log_analyze_tests;

import backend.academy.log_analyzer.data.LogReport;
import backend.academy.log_analyzer.formatter.LogReportFormatter;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogReportFormatterTest {
    @Test
    void testMarkdownReportFormatting() {
        LogReport logReport = new LogReport(2, Map.of("/index.html", 1L), Map.of(200, 1L), 1000.0, 500, 2, 0);
        LogReportFormatter formatter = new LogReportFormatter(logReport);
        String formattedReport = formatter.formatReport("markdown");

        assertTrue(formattedReport.contains("### Общая информация"), "Report should be in Markdown format");
    }

    @Test
    void testAdocReportFormatting() {
        LogReport logReport = new LogReport(2, Map.of("/index.html", 1L), Map.of(200, 1L), 1000.0, 500, 2, 0);
        LogReportFormatter formatter = new LogReportFormatter(logReport);
        String formattedReport = formatter.formatReport("adoc");

        assertTrue(formattedReport.contains("=== Общая информация"), "Report should be in Asciidoc format");
    }
}
