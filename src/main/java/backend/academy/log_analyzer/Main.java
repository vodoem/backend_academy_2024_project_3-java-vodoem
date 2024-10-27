package backend.academy.log_analyzer;

import backend.academy.log_analyzer.analyzer.LogAnalyzer;
import backend.academy.log_analyzer.data.LogRecord;
import backend.academy.log_analyzer.data.LogReport;
import backend.academy.log_analyzer.file_writer.AdocReportFileWriter;
import backend.academy.log_analyzer.file_writer.MarkdownReportFileWriter;
import backend.academy.log_analyzer.file_writer.ReportFileWriter;
import backend.academy.log_analyzer.formatter.ReportFormatter;
import backend.academy.log_analyzer.parser.CommandLineParser;
import backend.academy.log_analyzer.parser.LogParser;
import backend.academy.log_analyzer.reader.LogReader;
import lombok.experimental.UtilityClass;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@UtilityClass
public class Main {

    private static final String OUTPUT_DIRECTORY = "reports";

    public static void main(String[] args) {

        CommandLineParser clp = new CommandLineParser(args);

        if(clp.parse()){

            LogReader logReader = new LogReader();

            try(Stream<String> stream = logReader.readLogs(clp.path())){

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

                LogReport logReport = logAnalyzer.analyze(logRecords, clp.filterField(), clp.filterValue(), clp.fromDate(), clp.toDate());

                ReportFormatter reportFormatter = new ReportFormatter(logReport);
                String formattedReport = reportFormatter.formatReport(clp.format());

                ReportFileWriter writer = clp.format().equalsIgnoreCase("adoc") ?
                    new AdocReportFileWriter() : new MarkdownReportFileWriter();
                writer.writeReportToFile(OUTPUT_DIRECTORY, formattedReport);
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла: " + e.getMessage());
            }

        }
    }
}
