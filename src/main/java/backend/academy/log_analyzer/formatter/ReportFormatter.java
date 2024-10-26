package backend.academy.log_analyzer.formatter;

import backend.academy.log_analyzer.data.HttpStatus;
import backend.academy.log_analyzer.data.LogReport;


public class ReportFormatter {
    private final LogReport logReport;

    // Конструктор принимает готовые результаты анализа в виде LogReport
    public ReportFormatter(LogReport logReport) {
        this.logReport = logReport;
    }

    // Форматирование отчета
    public String formatReport(String format) {
        StringBuilder report = new StringBuilder();

        // Общее количество запросов
        report.append("### Общая информация\n\n");
        report.append("|        Метрика        |     Значение |\n");
        report.append("|:---------------------:|-------------:|\n");
        report.append(String.format("|  Количество запросов  |       %d |\n", logReport.totalRequests()));

        // Количество ошибочных запросов
        report.append(String.format("|  Количество ошибочных запросов  |       %d |\n", logReport.failedRequestsCount()));

        // Наиболее часто запрашиваемые ресурсы
        report.append("\n### Запрашиваемые ресурсы\n\n");
        report.append("| Ресурс | Количество запросов |\n");
        report.append("|--------|---------------------|\n");
        logReport.mostRequestedResources().forEach((resource, count) ->
            report.append(String.format("| %s | %d |\n", resource, count))
        );

        // Наиболее часто встречающиеся коды ответа
        report.append("\n### Коды ответа\n\n");
        report.append("| Код ответа | Имя          | Количество |\n");
        report.append("|------------|--------------|------------|\n");
        logReport.mostCommonResponseCodes().forEach((code, count) ->
            report.append(String.format("| %d | %s | %d |\n", code, HttpStatus.getStatusName(code), count))
        );

        // Средний размер ответа сервера
        report.append("\n### Средний размер ответа сервера\n\n");
        report.append(String.format("| Средний размер ответа | %.2f байт |\n", logReport.averageResponseSize()));

        // 95% перцентиль размера ответа сервера
        report.append(String.format("| 95-й перцентиль размера ответа | %d байт |\n", logReport.percentile95ResponseSize()));

        // Количество уникальных IP-адресов
        report.append("\n### Дополнительные статистики\n\n");
        report.append(String.format("| Количество уникальных IP-адресов | %d |\n", logReport.uniqueIpAddresses()));

        // Вывод в формате Markdown или AsciiDoc
        if (format.equalsIgnoreCase("adoc")) {
            return convertMarkdownToAdoc(report.toString());
        }

        return report.toString();
    }

    // Конвертация Markdown в AsciiDoc
    private String convertMarkdownToAdoc(String markdown) {
        String adoc = markdown;

        // Конвертация заголовков
        adoc = adoc.replaceAll("### ", "=== ");

        // Конвертация таблиц
        adoc = adoc.replace("|--------|---------------------|", "|===\n| Ресурс | Количество запросов |\n|--------|---------------------|\n");
        adoc = adoc.replace("|------------|--------------|------------|", "|===\n| Код ответа | Имя | Количество |\n|------------|--------------|------------|\n");
        adoc = adoc.replace("| Средний размер ответа |", "|===\n| Средний размер ответа |\n");
        adoc = adoc.replace("| 95-й перцентиль размера ответа |", "|===\n| 95-й перцентиль размера ответа |\n");
        adoc = adoc.replace("| Количество уникальных IP-адресов |", "|===\n| Количество уникальных IP-адресов |\n");

        // Вставка закрывающего тега для таблиц
        adoc = adoc.replace("|", "|==");

        return adoc;
    }
}

