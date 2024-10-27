package backend.academy.log_analyzer.formatter;

import backend.academy.log_analyzer.data.LogReport;

public interface ReportFormatter {
    String formatReport(LogReport logReport);
}
