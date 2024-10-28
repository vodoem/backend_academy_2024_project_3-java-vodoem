package backend.academy.log_analyzer.formatter;

import backend.academy.log_analyzer.data.HttpStatus;
import backend.academy.log_analyzer.data.LogReport;

public class AdocReportFormatter implements ReportFormatter {
    private static final String HEADER = "|===";
    private static final String HEADER_DIVIDER = HEADER + System.lineSeparator();
    private static final String METRIC_HEADER = "| Метрика | Значение" + System.lineSeparator();
    private static final String DOUBLE_HEADER_DIVIDER = HEADER + System.lineSeparator() + System.lineSeparator();

    @Override
    public String formatReport(LogReport logReport) {
        StringBuilder report = new StringBuilder();

        // Общая информация
        report.append("=== Общая информация").append(System.lineSeparator()).append(System.lineSeparator());
        report.append(HEADER_DIVIDER);
        report.append(METRIC_HEADER);
        report.append(String.format("| Количество запросов | %d" + System.lineSeparator(), logReport.totalRequests()));
        report.append(String.format("| Средний размер ответа | %.2f байт" + System.lineSeparator(),
            logReport.averageResponseSize()));
        report.append(String.format("| 95-й перцентиль размера ответа | %d байт" + System.lineSeparator(),
            logReport.percentile95ResponseSize()));
        report.append(DOUBLE_HEADER_DIVIDER);

        // Дополнительные статистики
        report.append("=== Дополнительные статистики").append(System.lineSeparator()).append(System.lineSeparator());
        report.append(HEADER_DIVIDER);
        report.append(METRIC_HEADER);
        report.append(String.format("| Количество уникальных IP-адресов | %d" + System.lineSeparator(),
            logReport.uniqueIpAddresses()));
        report.append(String.format("| Количество ошибочных запросов | %d" + System.lineSeparator(),
            logReport.failedRequestsCount()));
        report.append(DOUBLE_HEADER_DIVIDER);

        // Запрашиваемые ресурсы
        report.append("=== Запрашиваемые ресурсы").append(System.lineSeparator()).append(System.lineSeparator());
        report.append(HEADER_DIVIDER);
        report.append("| Ресурс | Количество запросов").append(System.lineSeparator());
        logReport.mostRequestedResources().forEach((resource, count) ->
            report.append(String.format("| %s | %d" + System.lineSeparator(), resource, count))
        );
        report.append(DOUBLE_HEADER_DIVIDER);

        // Коды ответа
        report.append("=== Коды ответа").append(System.lineSeparator()).append(System.lineSeparator());
        report.append(HEADER_DIVIDER);
        report.append("| Код ответа | Имя | Количество").append(System.lineSeparator());
        logReport.mostCommonResponseCodes().forEach((code, count) ->
            report.append(
                String.format("| %d | %s | %d" + System.lineSeparator(), code, HttpStatus.getStatusName(code), count))
        );
        report.append(HEADER_DIVIDER);

        return report.toString();
    }
}
