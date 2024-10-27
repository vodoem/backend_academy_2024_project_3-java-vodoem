package backend.academy.log_analyzer.formatter;

import backend.academy.log_analyzer.data.HttpStatus;
import backend.academy.log_analyzer.data.LogReport;

public class AdocReportFormatter implements ReportFormatter {
    @Override
    public String formatReport(LogReport logReport) {
        StringBuilder report = new StringBuilder();

        // Общая информация
        report.append("=== Общая информация\n\n");
        report.append("|===\n");
        report.append("| Метрика | Значение\n");
        report.append(String.format("| Количество запросов | %d\n", logReport.totalRequests()));
        report.append(String.format("| Средний размер ответа | %.2f байт\n", logReport.averageResponseSize()));
        report.append(String.format("| 95-й перцентиль размера ответа | %d байт\n", logReport.percentile95ResponseSize()));
        report.append("|===\n\n");

        // Дополнительные статистики
        report.append("=== Дополнительные статистики\n\n");
        report.append("|===\n");
        report.append("| Метрика | Значение\n");
        report.append(String.format("| Количество уникальных IP-адресов | %d\n", logReport.uniqueIpAddresses()));
        report.append(String.format("| Количество ошибочных запросов | %d\n", logReport.failedRequestsCount()));
        report.append("|===\n\n");

        // Запрашиваемые ресурсы
        report.append("=== Запрашиваемые ресурсы\n\n");
        report.append("|===\n");
        report.append("| Ресурс | Количество запросов\n");
        logReport.mostRequestedResources().forEach((resource, count) ->
            report.append(String.format("| %s | %d\n", resource, count))
        );
        report.append("|===\n\n");

        // Коды ответа
        report.append("=== Коды ответа\n\n");
        report.append("|===\n");
        report.append("| Код ответа | Имя | Количество\n");
        logReport.mostCommonResponseCodes().forEach((code, count) ->
            report.append(String.format("| %d | %s | %d\n", code, HttpStatus.getStatusName(code), count))
        );
        report.append("|===\n");

        return report.toString();
    }
}
