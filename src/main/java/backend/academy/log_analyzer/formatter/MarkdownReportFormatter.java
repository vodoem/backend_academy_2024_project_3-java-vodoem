package backend.academy.log_analyzer.formatter;

import backend.academy.log_analyzer.data.HttpStatus;
import backend.academy.log_analyzer.data.LogReport;

public class MarkdownReportFormatter implements ReportFormatter {
    private static final String METRIC_VALUE_FORMAT =
        "|        Метрика        |     Значение |" + System.lineSeparator();
    private static final String TABLE_HEADER = "|:---------------------:|-------------:|" + System.lineSeparator();

    @Override
    public String formatReport(LogReport logReport) {
        StringBuilder report = new StringBuilder();

        // Общая информация
        report.append("### Общая информация").append(System.lineSeparator()).append(System.lineSeparator());
        report.append(METRIC_VALUE_FORMAT);
        report.append(TABLE_HEADER);
        report.append(
            String.format("|  Количество запросов  |       %d |" + System.lineSeparator(), logReport.totalRequests()));
        report.append(String.format("| Средний размер ответа |     %.2f байт |" + System.lineSeparator(),
            logReport.averageResponseSize()));
        report.append(String.format("| 95-й перцентиль размера ответа | %d байт |" + System.lineSeparator(),
            logReport.percentile95ResponseSize()));

        // Дополнительные статистики
        report.append(System.lineSeparator()).append("### Дополнительные статистики").append(System.lineSeparator())
            .append(System.lineSeparator());
        report.append(METRIC_VALUE_FORMAT);
        report.append(TABLE_HEADER);
        report.append(String.format("| Количество уникальных IP-адресов | %d |" + System.lineSeparator(),
            logReport.uniqueIpAddresses()));
        report.append(String.format("| Количество ошибочных запросов | %d |" + System.lineSeparator(),
            logReport.failedRequestsCount()));

        // Запрашиваемые ресурсы
        report.append(System.lineSeparator()).append("### Запрашиваемые ресурсы").append(System.lineSeparator())
            .append(System.lineSeparator());
        report.append("| Ресурс | Количество запросов |").append(System.lineSeparator());
        report.append("|--------|---------------------|").append(System.lineSeparator());
        logReport.mostRequestedResources().forEach((resource, count) ->
            report.append(String.format("| %s | %d |" + System.lineSeparator(), resource, count))
        );

        // Коды ответа
        report.append(System.lineSeparator()).append("### Коды ответа").append(System.lineSeparator())
            .append(System.lineSeparator());
        report.append("| Код ответа | Имя          | Количество |").append(System.lineSeparator());
        report.append("|------------|--------------|------------|").append(System.lineSeparator());
        logReport.mostCommonResponseCodes().forEach((code, count) ->
            report.append(
                String.format("| %d | %s | %d |" + System.lineSeparator(), code, HttpStatus.getStatusName(code), count))
        );

        return report.toString();
    }
}
