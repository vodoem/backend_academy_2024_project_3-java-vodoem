package backend.academy.log_analyzer;

import backend.academy.log_analyzer.analyzer.LogAnalyzer;
import backend.academy.log_analyzer.data.LogRecord;
import backend.academy.log_analyzer.data.LogReport;
import backend.academy.log_analyzer.file_writer.AdocReportFileWriter;
import backend.academy.log_analyzer.file_writer.MarkdownReportFileWriter;
import backend.academy.log_analyzer.file_writer.ReportFileWriter;
import backend.academy.log_analyzer.formatter.LogReportFormatter;
import backend.academy.log_analyzer.parser.CommandLineParser;
import backend.academy.log_analyzer.parser.LogParser;
import backend.academy.log_analyzer.reader.LogReader;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {

    public static void main(String[] args) {

        CommandLineParser clp = new CommandLineParser(args);

        if (clp.parse()) {

            try (Stream<String> stream = LogReader.getReader(clp.path()).readLogs()) {

                LogAnalyzer logAnalyzer = new LogAnalyzer();
                LogParser logParser = new LogParser();

                Stream<LogRecord> logRecords = stream.map(logLine -> {
                    try {
                        return logParser.parse(logLine);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Ошибка при парсинге строки лога: " + e.getMessage());
                        return null; // Пропуск некорректной строки
                    }
                }).filter(Objects::nonNull);

                LogReport logReport =
                    logAnalyzer.analyze(logRecords, clp.filterField(), clp.filterValue(), clp.fromDate(), clp.toDate());

                LogReportFormatter logReportFormatter = new LogReportFormatter(logReport);
                String formattedReport = logReportFormatter.formatReport(clp.format());

                ReportFileWriter writer = "adoc".equalsIgnoreCase(clp.format())
                    ? new AdocReportFileWriter() : new MarkdownReportFileWriter();
                writer.writeReportToFile(formattedReport);
            } catch (IOException | InterruptedException e) {
                System.err.println("Ошибка чтения файла: " + e.getMessage());
            }

        }
    }
}
