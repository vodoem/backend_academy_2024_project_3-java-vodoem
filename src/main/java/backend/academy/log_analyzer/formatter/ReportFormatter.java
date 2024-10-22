package backend.academy.log_analyzer.formatter;

import java.util.Map;

public class ReportFormatter {
    private final long totalRequests;
    private final Map<String, Long> mostRequestedResources;
    private final Map<Integer, Long> responseCodeCount;
    private final double averageResponseSize;
    private final long percentile95ResponseSize;
    private final double averageResponseTime; // Дополнительная статистика
    private final long uniqueIpCount; // Дополнительная статистика

    // Конструктор принимает готовые результаты анализа
    public ReportFormatter(
        long totalRequests,
        Map<String, Long> mostRequestedResources,
        Map<Integer, Long> responseCodeCount,
        double averageResponseSize,
        long percentile95ResponseSize,
        double averageResponseTime,
        long uniqueIpCount) {
        this.totalRequests = totalRequests;
        this.mostRequestedResources = mostRequestedResources;
        this.responseCodeCount = responseCodeCount;
        this.averageResponseSize = averageResponseSize;
        this.percentile95ResponseSize = percentile95ResponseSize;
        this.averageResponseTime = averageResponseTime;
        this.uniqueIpCount = uniqueIpCount;
    }

    // Форматирование отчета
    public String formatReport(String format) {
        StringBuilder report = new StringBuilder();

        // Общее количество запросов
        report.append("### Общая информация\n\n");
        report.append("|        Метрика        |     Значение |\n");
        report.append("|:---------------------:|-------------:|\n");
        report.append(String.format("|  Количество запросов  |       %d |\n", totalRequests));

        // Наиболее часто запрашиваемые ресурсы
        report.append("\n### Запрашиваемые ресурсы\n\n");
        report.append("| Ресурс | Количество запросов |\n");
        report.append("|--------|---------------------|\n");
        mostRequestedResources.forEach((resource, count) ->
            report.append(String.format("| %s | %d |\n", resource, count))
        );

        // Наиболее часто встречающиеся коды ответа
        report.append("\n### Коды ответа\n\n");
        report.append("| Код ответа | Количество |\n");
        report.append("|------------|------------|\n");
        responseCodeCount.forEach((code, count) ->
            report.append(String.format("| %d | %d |\n", code, count))
        );

        // Средний размер ответа сервера
        report.append("\n### Средний размер ответа сервера\n\n");
        report.append(String.format("| Средний размер ответа | %.2f байт |\n", averageResponseSize));

        // 95% перцентиль размера ответа сервера
        report.append(String.format("| 95-й перцентиль размера ответа | %d байт |\n", percentile95ResponseSize));

        // Дополнительная статистика 1: Среднее время обработки запроса
        report.append("\n### Дополнительные статистики\n\n");
        report.append(String.format("| Среднее время обработки запроса | %.2f мс |\n", averageResponseTime));

        // Дополнительная статистика 2: Количество уникальных IP-адресов
        report.append(String.format("| Количество уникальных IP-адресов | %d |\n", uniqueIpCount));

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
        adoc = adoc.replace("|------------|------------|", "|===\n| Код ответа | Количество |\n|------------|------------|\n");
        adoc = adoc.replace("| Средний размер ответа |", "|===\n| Средний размер ответа |\n");
        adoc = adoc.replace("| 95-й перцентиль размера ответа |", "|===\n| 95-й перцентиль размера ответа |\n");
        adoc = adoc.replace("| Среднее время обработки запроса |", "|===\n| Среднее время обработки запроса |\n");
        adoc = adoc.replace("| Количество уникальных IP-адресов |", "|===\n| Количество уникальных IP-адресов |\n");

        // Вставка закрывающего тега для таблиц
        adoc = adoc.replace("|", "|==");

        return adoc;
    }
}

