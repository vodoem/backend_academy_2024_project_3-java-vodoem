package backend.academy.log_analyzer.formatter;

import backend.academy.log_analyzer.data.LogReport;

public class LogReportFormatter {

    private final LogReport logReport;

    public LogReportFormatter(LogReport logReport){
        this.logReport = logReport;
    }

    public String formatReport(String format) {
        ReportFormatter formatter;

        // Определение формата
        if ("markdown".equalsIgnoreCase(format)) {
            formatter = new MarkdownReportFormatter();
        } else if ("adoc".equalsIgnoreCase(format)) {
            formatter = new AdocReportFormatter();
        } else {
            throw new IllegalArgumentException("Неизвестный формат: " + format);
        }

        return formatter.formatReport(logReport);
    }
}
